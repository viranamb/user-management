package com.netflix.cloud.controller;

import com.netflix.cloud.PlayListService;
import com.netflix.cloud.constants.Constants;
import com.netflix.cloud.exception.CustomException;
import com.netflix.cloud.model.PlaylistDO;
import com.netflix.cloud.utils.CountryEnum;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Primary controller class that interfaces with clients
 *
 */
@RestController
@RequestMapping(Constants.PLAYLIST_RESOURCE_PATH)
public class PlaylistController {

    private static final Logger LOGGER = LogManager.getLogger(PlaylistController.class);

    @Inject
    private PlayListService playListService;

    @Inject
    private Executor playlistExecutor;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public DeferredResult<Response> retrievePlayListData(@QueryParam(Constants.CONTENT_IDENTIFIER) String contentIdentifier,
                                         @QueryParam(Constants.COUNTRY_CODE) String countryCode,
                                         @Context HttpServletResponse httpServletResponse) throws Exception {

        /**
         * For increasing the scalability of our API to allow many more concurrent incoming client requests, we are returning a DeferredResult object.
         * This approach will allow the playlist computation logic to be executed in a separate thread, thus freeing up the server to allow more incoming client requests.
         *
         * We create an asynchronous task with CompletableFuture which creates a new thread where our longer running playlist computation task will be executed
         */
        DeferredResult<Response> deferredPlaylistResult = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> retrievePlaylist(contentIdentifier, countryCode, httpServletResponse), playlistExecutor).thenAccept((Response playlistResponse) -> {
            deferredPlaylistResult.setResult(playlistResponse);
        });
        return deferredPlaylistResult;
    }

    private Response retrievePlaylist(String contentIdentifier, String countryCode, HttpServletResponse httpServletResponse){

        // First, confirm the validity of the input parameters provided by the client
        Response validationResponse = validateRequestParameters(contentIdentifier, countryCode, httpServletResponse);
        if (validationResponse != null) {
            return validationResponse;
        }

        // Next, invoke the playlist service to compute the applicable playlist/s
        List<PlaylistDO> playlistDOs = null;
        try {
            playlistDOs = playListService.retrievePlaylist(contentIdentifier, countryCode);
        } catch (CustomException ex) {
            LOGGER.error(Constants.INTERNAL_SERVER_ERROR);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(Constants.INTERNAL_SERVER_ERROR).build();
        }

        // Check for failures in the response from the playlist service
        Response failureResponse = verifyFailure(httpServletResponse, playlistDOs);
        if (failureResponse != null) {
            return failureResponse;
        }

        return Response.ok(HttpServletResponse.SC_OK).entity(constructPrettyResponse(playlistDOs)).build();
    }

    private Response validateRequestParameters(String contentIdentifier, String countryCode, HttpServletResponse response) {
        // Assumption: Both contentIdentifier and countryCode are mandatory
        if (StringUtils.isEmpty(contentIdentifier)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(Constants.INVALID_CONTENT_IDENTIFIER).build();
        }

        if (StringUtils.isEmpty(countryCode) || !CountryEnum.isValidEnum(countryCode)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(Constants.INVALID_COUNTRY_CODE).build();
        }
        return null;
    }

    private Response verifyFailure(HttpServletResponse response, List<PlaylistDO> playlistDOs) {
        if (playlistDOs == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(Constants.INVALID_DATA_FOR_PLAYLIST).build();
        }

        if (playlistDOs == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(Constants.INVALID_DATA_FOR_PLAYLIST).build();
        }

        if (playlistDOs.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Response.status(HttpServletResponse.SC_BAD_REQUEST).entity(Constants.LEGAL_PLAYLIST_NOT_POSSIBLE).build();
        }
        return null;
    }

    private String constructPrettyResponse(List<PlaylistDO> playlistDOs) {
        StringBuilder playList = new StringBuilder();
        if (!CollectionUtils.isEmpty(playlistDOs)) {
            int size = playlistDOs.size();
            for (int i = 0; i < size; i++) {
                if (size == 1) {
                    playList.append("Playlist");
                } else {
                    playList.append("Playlist" + (i + 1));
                }
                playList.append(playlistDOs.get(i).getVideos());
                if (i != size - 1) {
                    playList.append(",");
                }
            }
        }
        return playList.toString();
    }

}