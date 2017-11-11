package com.capitalone.migration.resource;

import com.capitalone.migration.PlayListService;
import com.capitalone.migration.constants.Constants;
import com.capitalone.migration.model.Playlist;
import com.capitalone.migration.utils.CountryEnum;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * @author msz519
 */
@RestController
@RequestMapping(Constants.PLAYLIST_RESOURCE_PATH)
public class PlayListResource {

    private static final Logger LOGGER = LogManager.getLogger(PlayListResource.class);

    @Inject
    private PlayListService playListService;

    @RequestMapping(method = RequestMethod.GET)
    @Produces("application/json")
    public Response retrievePlayListData(@QueryParam(Constants.CONTENT_IDENTIFIER) String contentIdentifier,
                                         @QueryParam(Constants.COUNTRY_CODE) String countryCode) throws Exception {

        // Assumption: Both contentIdentifier and countryCode are mandatory
        if (StringUtils.isEmpty(contentIdentifier)){
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(Constants.INVALID_CONTENT_IDENTIFIER).build();
        }

        if (StringUtils.isEmpty(countryCode) || !CountryEnum.isValidEnum(countryCode)){
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(Constants.INVALID_COUNTRY_CODE).build();
        }

        List<Playlist> playlists = playListService.retrievePlaylist(contentIdentifier, countryCode);

        if (playlists == null) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(Constants.INVALID_DATA_FOR_PLAYLIST).build();
        }

        if (playlists.isEmpty()) {
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(Constants.LEGAL_PLAYLIST_NOT_POSSIBLE).build();
        }

        return Response.ok(HttpURLConnection.HTTP_OK).entity(constructResponse(playlists)).build();
    }


    private String constructResponse(List<Playlist> playlists) {
        return playlists.toString();
    }

}

/*
 * Copyright 2016 Capital One Financial Corporation All Rights Reserved.
 * 
 * This software contains valuable trade secrets and proprietary information of
 * Capital One and is protected by law. It may not be copied or distributed in
 * any form or medium, disclosed to third parties, reverse engineered or used in
 * any manner without prior written authorization from Capital One.
 */