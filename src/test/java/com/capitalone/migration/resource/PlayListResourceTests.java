package com.capitalone.migration.resource;

import com.capitalone.migration.LaunchApplication;
import com.capitalone.migration.constants.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = LaunchApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-non-prod.properties")
public class PlayListResourceTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testLendingApplicationService_invalidContentIdentifier_returnFailure() {
        try {
            mvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "")
                    .param(Constants.COUNTRY_CODE, "US")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(400)))
                    .andExpect(jsonPath("entity", is(Constants.INVALID_CONTENT_IDENTIFIER)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLendingApplicationService_invalidCountryCode_returnFailure() {
        try {
            mvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI3")
                    .param(Constants.COUNTRY_CODE, "")
                    .contentType(MediaType.APPLICATION_JSON))
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
    public void testLendingApplicationService_legalPlayListNotPossible_returnFailure() {
        try {
            mvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI3")
                    .param(Constants.COUNTRY_CODE, "US")
                    .contentType(MediaType.APPLICATION_JSON))
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
    public void testLendingApplicationService_invalidDataForPlayList_returnFailure() {
        try {
            mvc.perform(get(Constants.PLAYLIST_RESOURCE_PATH)
                    .param(Constants.CONTENT_IDENTIFIER, "MI1")
                    .param(Constants.COUNTRY_CODE, "US")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content()
                            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("status", is(400)))
                    .andExpect(jsonPath("entity", is(Constants.INVALID_DATA_FOR_PLAYLIST)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}