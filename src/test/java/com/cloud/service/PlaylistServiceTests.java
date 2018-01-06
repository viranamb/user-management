package com.cloud.service;

import com.cloud.constants.Constants;
import com.cloud.model.PlaylistDO;
import com.cloud.model.PreRollDO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.cloud.PlayListService;
import com.cloud.dao.DataRepositoryDao;
import com.cloud.exception.CustomException;
import com.cloud.model.ContentDO;
import com.cloud.model.VideoDO;
import com.cloud.service.impl.PlayListServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlaylistServiceTests {

    @Mock
    private DataRepositoryDao dataRepository;

    private static Map<String, ContentDO> contentListMap;

    private static Map<String, PreRollDO> preRollListMap;

    @InjectMocks
    private PlayListService playListService = new PlayListServiceImpl();

    @BeforeClass
    public static void setUp() throws FileNotFoundException {
        Gson gson = new GsonBuilder().create();

        JsonReader reader = new JsonReader(new FileReader("sample_content_test.json"));
        Type contentData = new TypeToken<List<ContentDO>>() {}.getType();
        List<ContentDO> contentDOs = gson.fromJson(reader, contentData);
        contentListMap = contentDOs.parallelStream().collect(Collectors.toConcurrentMap(content -> content.getName(), content -> content));

        Type preRollData = new TypeToken<List<PreRollDO>>() {}.getType();
        reader = new JsonReader(new FileReader("sample_preroll_test.json"));
        List<PreRollDO> preRollDOs = gson.fromJson(reader, preRollData);
        preRollListMap = preRollDOs.parallelStream().collect(Collectors.toConcurrentMap(preRoll -> preRoll.getName(), preRoll -> preRoll));
    }

    @Test
    public void testPlaylistService_invalidContentIdentifier_returnFailure() throws CustomException {
        List<PlaylistDO> playlistDOs = playListService.retrievePlaylist("Invalid content identifier", "US");

        Assert.assertTrue(playlistDOs == null);
        verify(dataRepository, times(1)).getContent(anyString());
        verify(dataRepository, times(0)).getPreRoll(anyString());
    }

    @Test
    public void testPlaylistService_invalidCountryCode_returnFailure() throws CustomException {
        when(dataRepository.getContent("MI3")).thenReturn(contentListMap.get("MI3"));
        when(dataRepository.getPreRoll("WB1")).thenReturn(preRollListMap.get("WB1"));
        when(dataRepository.getPreRoll("WB3")).thenReturn(preRollListMap.get("WB3"));

        List<PlaylistDO> playlistDOs = playListService.retrievePlaylist("MI3", "Invalid country code");

        Assert.assertTrue(playlistDOs.isEmpty());
        verify(dataRepository, times(1)).getContent(anyString());
        verify(dataRepository, times(2)).getPreRoll(anyString());
    }

    @Test(expected = CustomException.class)
    public void testPlaylistService_internalServerError_throwCustomException() throws CustomException {
        when(dataRepository.getContent(anyString())).thenThrow(Exception.class);

        playListService.retrievePlaylist("Invalid content identifier", "US");
    }

    @Test
    public void testPlaylistService_validInputForUS_returnSuccess() throws CustomException {
        when(dataRepository.getContent("MI3")).thenReturn(contentListMap.get("MI3"));
        when(dataRepository.getPreRoll("WB1")).thenReturn(preRollListMap.get("WB1"));
        when(dataRepository.getPreRoll("WB3")).thenReturn(preRollListMap.get("WB3"));

        List<PlaylistDO> playlistDOs = playListService.retrievePlaylist("MI3", "US");

        Assert.assertTrue(playlistDOs.isEmpty());
        verify(dataRepository, times(1)).getContent(anyString());
        verify(dataRepository, times(2)).getPreRoll(anyString());
    }

    @Test
    public void testPlaylistService_validInputForUK_returnSuccess() throws CustomException {

        when(dataRepository.getContent("MI3")).thenReturn(contentListMap.get("MI3"));
        when(dataRepository.getPreRoll("WB1")).thenReturn(preRollListMap.get("WB1"));
        when(dataRepository.getPreRoll("WB3")).thenReturn(preRollListMap.get("WB3"));

        List<PlaylistDO> playlistDOs = playListService.retrievePlaylist("MI3", "UK");

        Assert.assertNotNull(playlistDOs);
        Assert.assertEquals(playlistDOs.size(), 2);
        verify(dataRepository, times(1)).getContent(anyString());
        verify(dataRepository, times(2)).getPreRoll(anyString());

        Set<VideoDO> videos = playlistDOs.get(0).getVideos();
        Assert.assertEquals(videos.size(), 2);
        for (VideoDO video : videos) {
            Assert.assertTrue(video.getName().equalsIgnoreCase("V6") || video.getName().equalsIgnoreCase("V2"));
            Assert.assertEquals(video.getAttributes().get(Constants.LANGUAGE), "English");
            Assert.assertEquals(((List)video.getAttributes().get(Constants.COUNTRIES)).get(0), "UK");
            Assert.assertEquals(video.getAttributes().get(Constants.ASPECT), "4:3");
        }
        videos = playlistDOs.get(1).getVideos();
        Assert.assertEquals(videos.size(), 2);
        for (VideoDO video : videos) {
            Assert.assertTrue(video.getName().equalsIgnoreCase("V7") || video.getName().equalsIgnoreCase("V3"));
            Assert.assertEquals(video.getAttributes().get(Constants.LANGUAGE), "English");
            Assert.assertEquals(((List)video.getAttributes().get(Constants.COUNTRIES)).get(0), "UK");
            Assert.assertEquals(video.getAttributes().get(Constants.ASPECT), "16:9");
        }
    }

    @Test
    public void testPlaylistService_validInputForCA_returnSuccess() throws CustomException {

        when(dataRepository.getContent("MI3")).thenReturn(contentListMap.get("MI3"));
        when(dataRepository.getPreRoll("WB1")).thenReturn(preRollListMap.get("WB1"));
        when(dataRepository.getPreRoll("WB3")).thenReturn(preRollListMap.get("WB3"));

        List<PlaylistDO> playlistDOs = playListService.retrievePlaylist("MI3", "CA");

        Assert.assertNotNull(playlistDOs);
        Assert.assertEquals(playlistDOs.size(), 1);
        verify(dataRepository, times(1)).getContent(anyString());
        verify(dataRepository, times(2)).getPreRoll(anyString());

        Set<VideoDO> videos = playlistDOs.get(0).getVideos();
        Assert.assertEquals(videos.size(), 2);
        for (VideoDO video : videos) {
            Assert.assertTrue(video.getName().equalsIgnoreCase("V5") || video.getName().equalsIgnoreCase("V1"));
            Assert.assertEquals(video.getAttributes().get(Constants.LANGUAGE), "English");
            Assert.assertTrue(((List) video.getAttributes().get(Constants.COUNTRIES)).contains("CA"));
            Assert.assertEquals(video.getAttributes().get(Constants.ASPECT), "16:9");
        }
    }

    @Test
    public void testPlaylistService_validInputForUK2_returnSuccess() throws CustomException {
        when(dataRepository.getContent("MI4")).thenReturn(contentListMap.get("MI4"));
        when(dataRepository.getPreRoll("WB2")).thenReturn(preRollListMap.get("WB2"));

        List<PlaylistDO> playlistDOs = playListService.retrievePlaylist("MI4", "UK");

        Assert.assertNotNull(playlistDOs);
        Assert.assertEquals(playlistDOs.size(), 2);

        Set<VideoDO> videos = playlistDOs.get(0).getVideos();
        Assert.assertEquals(videos.size(), 2);
        verify(dataRepository, times(1)).getContent(anyString());
        verify(dataRepository, times(1)).getPreRoll(anyString());

        for (VideoDO video : videos) {
            Assert.assertTrue(video.getName().equalsIgnoreCase("V13") || video.getName().equalsIgnoreCase("V9"));
            Assert.assertEquals(video.getAttributes().get(Constants.LANGUAGE), "English");
            Assert.assertEquals(((List)video.getAttributes().get(Constants.COUNTRIES)).get(0), "UK");
            Assert.assertEquals(video.getAttributes().get(Constants.ASPECT), "4:3");
        }
        videos = playlistDOs.get(1).getVideos();
        Assert.assertEquals(videos.size(), 2);
        for (VideoDO video : videos) {
            Assert.assertTrue(video.getName().equalsIgnoreCase("V14") || video.getName().equalsIgnoreCase("V10"));
            Assert.assertEquals(video.getAttributes().get(Constants.LANGUAGE), "English");
            Assert.assertEquals(((List)video.getAttributes().get(Constants.COUNTRIES)).get(0), "UK");
            Assert.assertEquals(video.getAttributes().get(Constants.ASPECT), "16:9");
        }

    }

}
