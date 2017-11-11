package com.capitalone.migration.resource;

import com.capitalone.migration.PlayListService;
import com.capitalone.migration.model.Playlist;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "non-prod")
public class PlayListResourceTests {

    @Autowired
    private PlayListService playListService;

    @Test
    public void testLendingApplicationService_invalidContentIdentifier_returnFailure() {
        List<Playlist> playlists = playListService.retrievePlaylist("Invalid content identifier", "US");

        Assert.assertTrue(playlists == null);
    }

    @Test
    public void testLendingApplicationService_invalidCountryCode_returnFailure() {
        List<Playlist> playlists = playListService.retrievePlaylist("MI3", "Invalid country code");

        Assert.assertTrue(playlists.isEmpty());
    }

    @Test
    public void testLendingApplicationService_validInputForUS_returnSuccess() {
        List<Playlist> playlists = playListService.retrievePlaylist("MI3", "US");

        Assert.assertTrue(playlists.isEmpty());
    }

    @Test
    public void testLendingApplicationService_validInputForUK_returnSuccess() {
        List<Playlist> playlists = playListService.retrievePlaylist("MI3", "UK");

        Assert.assertNotNull(playlists);
    }

    @Test
    public void testLendingApplicationService_validInputForCA_returnSuccess() {
        List<Playlist> playlists = playListService.retrievePlaylist("MI3", "CA");

        Assert.assertNotNull(playlists);
        Assert.assertEquals(playlists.size(), 1);
        Assert.assertEquals(playlists.get(0).getVideos().size(), 1);
    }

    @Test
    public void testLendingApplicationService_validInputForUK2_returnSuccess() {
        List<Playlist> playlists = playListService.retrievePlaylist("MI4", "UK");

        Assert.assertNotNull(playlists);
        Assert.assertEquals(playlists.size(), 2);
    }

}
