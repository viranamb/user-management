package com.capitalone.migration.impl;

import com.capitalone.migration.PlayListService;
import com.capitalone.migration.constants.Constants;
import com.capitalone.migration.model.Content;
import com.capitalone.migration.model.Playlist;
import com.capitalone.migration.model.PreRoll;
import com.capitalone.migration.model.Video;
import com.capitalone.migration.dao.DataRepository;
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

import static com.capitalone.migration.constants.Constants.ASPECT;
import static com.capitalone.migration.constants.Constants.LANGUAGE;

@Service
public class PlayListServiceImpl implements PlayListService {

    private static final Logger LOGGER = LogManager.getLogger(PlayListServiceImpl.class);

    @Autowired
    private DataRepository dataRepository;

    @Override
    public List<Playlist> retrievePlaylist(String contentIdentifier, String countryCode) {

        List<Playlist> playlists = null;

        Content content = dataRepository.getContent(contentIdentifier);

        // If content does not exist, there cannot be a valid playlist, hence returning empty playlist.
        // (Assumption: Playlists need to have combinations of pre-roll and content videos)
        // Similarly, if no pre-rolls exist for the content, there cannot be a valid playlist, hence returning empty playlist
        if (Objects.isNull(content) || CollectionUtils.isEmpty(content.getVideos()) || CollectionUtils.isEmpty(content.getPreroll())) {
            LOGGER.error(Constants.INVALID_DATA_FOR_PLAYLIST);
            return playlists;
        }

        playlists = new ArrayList<>();

        Set<Video> filteredCountrySpecificContentVideos = getContentVideosForCountry(countryCode, content);

        final Set<Video> filteredCountrySpecificPreRollVideos = getPreRollVideosForCountry(countryCode, content);

        computePlayLists(playlists, filteredCountrySpecificContentVideos, filteredCountrySpecificPreRollVideos);

        return playlists;
    }

    /**
     * Filter content videos by country code provided
     *
     * @param countryCode : Country code
     * @param content : Content identifier
     *
     * @return Videos filtered on the basis on country and content (Videos contained in a LinkedHashSet to maintain insertion order and uniqueness)
     */
    private Set<Video> getContentVideosForCountry(String countryCode, Content content) {
        return content.getVideos().stream()
                    .parallel().filter(video -> ((List<String>) video.getAttributes().get(Constants.COUNTRIES)).contains(countryCode))
                    .sequential().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Filter pre-roll videos by country code provided (using LinkedHashSet to maintain insertion order and uniqueness)
     *
     * @param countryCode : Country code
     * @param content : Content identifier
     *
     * @return Videos filtered on the basis on country and content (Videos contained in a LinkedHashSet to maintain insertion order and uniqueness)
     */
    private Set<Video> getPreRollVideosForCountry(String countryCode, Content content) {
        return content.getPreroll().stream().sequential()
                    .flatMap(preRoll -> {
                        PreRoll preRollData = dataRepository.getPreRoll(preRoll.getName());
                        // Check if preRoll data exists for preRoll
                        if (preRollData != null)
                            return preRollData.getVideos().stream();
                        else {
                            return null;
                        }
                    })
                    .filter(video -> ((List<String>) video.getAttributes().get(Constants.COUNTRIES)).contains(countryCode))
                    .sequential().collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Compute list of playlist videos. The playlist will have one or more combinations of preRoll and content videos.
     * For each combination of videos, the preRoll videos will be listed first, followed by  associated content videos
     *
     * @param playlists
     * @param filteredCountrySpecificContentVideos
     * @param filteredCountrySpecificPreRollVideos
     */
    private void computePlayLists(List<Playlist> playlists, Set<Video> filteredCountrySpecificContentVideos, Set<Video> filteredCountrySpecificPreRollVideos) {
        filteredCountrySpecificContentVideos.stream().forEach(contentVideo -> {

                    // Create playlist for each content video
                    Playlist playlist = new Playlist();

                    // Verify for each pre-roll video associated with the content whether language and aspect match and add them to playlist if it matches
                    filteredCountrySpecificPreRollVideos.stream()
                            .filter(preRollVideo -> contentVideo.getAttributes().get(LANGUAGE).equals(preRollVideo.getAttributes().get(LANGUAGE)))
                            .filter(preRollVideo -> contentVideo.getAttributes().get(ASPECT).equals(preRollVideo.getAttributes().get(ASPECT)))
                            .forEach(preRollVideo -> {
                                playlist.addVideos(preRollVideo);
                            });

                    // Add content video to playlist only if there is at least one pre-roll video
                    // (Assumption: Playlists need to have combinations of pre-roll and content videos)
                    // (Assumption: Playlists with only content videos cannot be returned)
                    // (Assumption: Playlists with only pre-roll videos cannot be returned)
                    if (!CollectionUtils.isEmpty(playlist.getVideos())) {
                        playlist.addVideos(contentVideo);
                        playlists.add(playlist);
                    }
                }
        );
    }
}
