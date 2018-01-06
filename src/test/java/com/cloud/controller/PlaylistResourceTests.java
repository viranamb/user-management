package com.cloud.controller;

import com.cloud.LaunchApplication;
import com.cloud.constants.Constants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LaunchApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-non-prod.properties")
public class PlaylistResourceTests {

    private PlaylistController controller;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        controller = new PlaylistController();
    }

    @Test
    public void testLendingApplicationService_invalidContentIdentifier_returnFailure() {
        try {
           MvcResult result = mockMvc
                    .perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                            .param(Constants.CONTENT_IDENTIFIER, "")
                            .param(Constants.COUNTRY_CODE, "US")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            mockMvc.perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(400)))
                    .andExpect(jsonPath("entity", is(Constants.INVALID_CONTENT_IDENTIFIER)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLendingApplicationService_invalidCountryCode_returnFailure() {
        try {

            MvcResult result = mockMvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                            .param(Constants.CONTENT_IDENTIFIER, "MI3")
                            .param(Constants.COUNTRY_CODE, "US1"))
                            .andReturn();

            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(400)))
                    .andExpect(jsonPath("entity", is(Constants.INVALID_COUNTRY_CODE)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testLendingApplicationService_oneContentVideoAndOnePreRollVideoWithDifferentAspectForUS_returnFailure() {
        try {
            MvcResult result = mockMvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI3")
                    .param(Constants.COUNTRY_CODE, "US")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(400)))
                    .andExpect(jsonPath("entity", is(Constants.LEGAL_PLAYLIST_NOT_POSSIBLE)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLendingApplicationService_nonExistentContentIdentifier_returnFailure() {
        try {
            MvcResult result = mockMvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI1")
                    .param(Constants.COUNTRY_CODE, "US")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(400)))
                    .andExpect(jsonPath("entity", is(Constants.INVALID_DATA_FOR_PLAYLIST)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testLendingApplicationService_noPreRollVideosForContent_returnFailure() {
        try {
            MvcResult result = mockMvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI5")
                    .param(Constants.COUNTRY_CODE, "UK")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(400)))
                    .andExpect(jsonPath("entity", is(Constants.INVALID_DATA_FOR_PLAYLIST)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testLendingApplicationService_oneContentVideoAndOnePreRollVideoWithSameAspectForCA_returnSuccess() {
        try {
            MvcResult result = mockMvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI8")
                    .param(Constants.COUNTRY_CODE, "CA")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(200)))
                    .andExpect(jsonPath("entity", is("Playlist[V82, V80]")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLendingApplicationService_oneContentVideoAndTwoPreRollVideosWithSameAspectForCA_returnSuccess() {
        try {
            MvcResult result = mockMvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI6")
                    .param(Constants.COUNTRY_CODE, "CA")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(200)))
                    .andExpect(jsonPath("entity", is("Playlist[V64, V65, V17]")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLendingApplicationService_oneContentVideoAndTwoPreRollVideosWithSameAspectForUK_returnSuccess() {
        try {
            MvcResult result = mockMvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI6")
                    .param(Constants.COUNTRY_CODE, "UK")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(200)))
                    .andExpect(jsonPath("entity", is("Playlist[V66, V67, V19]")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLendingApplicationService_twoContentVideosAndTwoPreRollVideosWithDifferentAspectsForUK_returnSuccess() {
        try {
            MvcResult result = mockMvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI3")
                    .param(Constants.COUNTRY_CODE, "UK")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(200)))
                    .andExpect(jsonPath("entity", is("Playlist1[V6, V2],Playlist2[V7, V3]")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}