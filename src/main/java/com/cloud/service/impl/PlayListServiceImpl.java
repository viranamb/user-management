package com.cloud.service.impl;

import com.cloud.PlayListService;
import com.cloud.constants.Constants;
import com.cloud.dao.DataRepositoryDao;
import com.cloud.exception.CustomException;
import com.cloud.model.ContentDO;
import com.cloud.model.PlaylistDO;
import com.cloud.model.PreRollDO;
import com.cloud.model.VideoDO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class that abstracts the data retrieval implementation details from the Controller class.
 *
 * This class also interfaces with the data repository layer.
 */
@Service
public class PlayListServiceImpl implements PlayListService {

    private static final Logger LOGGER = LogManager.getLogger(PlayListServiceImpl.class);

    @Autowired
    private DataRepositoryDao dataRepository;

    /**
     *
     * @param contentIdentifier Content Identifier
     * @param countryCode Country code
     *
     * @return List of playlists
     *
     * @throws CustomException
     */
    @Override
    public List<PlaylistDO> retrievePlaylist(String contentIdentifier, String countryCode) throws CustomException {

        List<PlaylistDO> playlistDOs = null;

        try {

            ContentDO contentDO = dataRepository.getContent(contentIdentifier);

            // If contentDO does not exist, there cannot be a valid playlist, hence returning empty playlist.
            // Similarly, if no pre-rolls exist for the content, there cannot be a valid playlist, hence returning empty playlist
            // (Assumption: Playlists need to have combinations of pre-roll and contentDO videos)
            if (Objects.isNull(contentDO) || CollectionUtils.isEmpty(contentDO.getVideos()) || CollectionUtils.isEmpty(contentDO.getPreroll())) {
                LOGGER.error(Constants.INVALID_DATA_FOR_PLAYLIST);
                return null;
            }

            playlistDOs = new ArrayList<>();

            Set<VideoDO> filteredContentVideoDOs = getContentVideosByCountryAndContent(countryCode, contentDO);

            final Set<VideoDO> filteredPreRollVideoDOs = getPreRollVideosByCountryAndContent(countryCode, contentDO);

            computePlayLists(playlistDOs, filteredContentVideoDOs, filteredPreRollVideoDOs);

        } catch (Exception ex) {
            LOGGER.error(Constants.PLAYLIST_COMPUTATION_ERROR, ex);
            throw new CustomException(Constants.PLAYLIST_COMPUTATION_ERROR, ex);
        }

        return playlistDOs;
    }

    /**
     * Filter contentDO videos by country code provided
     *
     * @param countryCode : Country code
     * @param contentDO   : ContentDO identifier
     * @return Videos filtered on the basis on country and contentDO (Videos contained in a LinkedHashSet to maintain insertion order and uniqueness)
     */
    private Set<VideoDO> getContentVideosByCountryAndContent(String countryCode, ContentDO contentDO) {
        return contentDO.getVideos().stream()
                .parallel().filter(video -> ((List<String>) video.getAttributes().get(Constants.COUNTRIES)).contains(countryCode))
                .sequential().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Filter pre-roll videos by country code provided (using LinkedHashSet to maintain insertion order and uniqueness)
     *
     * @param countryCode : Country code
     * @param contentDO   : ContentDO identifier
     * @return Videos filtered on the basis on country and contentDO (Videos contained in a LinkedHashSet to maintain insertion order and uniqueness)
     */
    private Set<VideoDO> getPreRollVideosByCountryAndContent(String countryCode, ContentDO contentDO) {
        return contentDO.getPreroll().stream().sequential()
                .flatMap(preRoll -> {
                    PreRollDO preRollDOData = dataRepository.getPreRoll(preRoll.getName());
                    // Check if preRoll data exists for preRoll
                    if (preRollDOData != null)
                        return preRollDOData.getVideos().stream();
                    else {
                        return null;
                    }
                })
                .filter(video -> ((List<String>) video.getAttributes().get(Constants.COUNTRIES)).contains(countryCode))
                .sequential().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Compute list of playlist videos. The playlist will have one or more combinations of preRoll and contentDO videos.
     * For each combination of videos, the preRoll videos will be listed first, followed by the associated contentDO video
     *
     * @param playlistDOs                            : List of playlist objects
     * @param filteredContentVideoDOs : List of contentDO videos filtered by country and content
     * @param filteredPreRollVideoDOs : List of preroll videos associated with content and filtered by country
     */
    private void computePlayLists(List<PlaylistDO> playlistDOs, Set<VideoDO> filteredContentVideoDOs, Set<VideoDO> filteredPreRollVideoDOs) {
        filteredContentVideoDOs.stream().forEach(contentVideo -> {

                    // Create playlistDO for each contentDO video
                    PlaylistDO playlistDO = new PlaylistDO();

                    // Verify for each pre-roll video associated with the contentDO whether language and aspect match and add them to playlistDO if it matches
                    filteredPreRollVideoDOs.stream()
                            .filter(preRollVideo -> contentVideo.getAttributes().get(Constants.LANGUAGE).equals(preRollVideo.getAttributes().get(Constants.LANGUAGE)))
                            .filter(preRollVideo -> contentVideo.getAttributes().get(Constants.ASPECT).equals(preRollVideo.getAttributes().get(Constants.ASPECT)))
                            .forEach(preRollVideo -> {
                                playlistDO.addVideos(preRollVideo);
                            });

                    // Add contentDO video to playlistDO only if there is at least one pre-roll video
                    // (Assumption: Playlists need to have combinations of pre-roll and contentDO videos)
                    // (Assumption: Playlists with only contentDO videos cannot be returned)
                    // (Assumption: Playlists with only pre-roll videos cannot be returned)
                    if (!CollectionUtils.isEmpty(playlistDO.getVideos())) {
                        playlistDO.addVideos(contentVideo);
                        playlistDOs.add(playlistDO);
                    }
                }
        );
    }
}
