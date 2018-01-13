package com.cloud.controller;

import com.cloud.constants.Constants;
import com.cloud.exception.CustomException;
import com.cloud.model.UserDO;
import com.cloud.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserSecureControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserSecureController userController = new UserSecureController();

    private UserDO userDO;

    @Before
    public void setUp() throws FileNotFoundException {
        userDO = new UserDO("firstName4", "lastName4", "user4", "password4", "emailAddress4@gmail.com");
    }

    @Test
    public void testUserService_retrieveUserByUserId_returnSuccess() throws CustomException {
        when(userService.retrieveUserByUserId(any(String.class))).thenReturn(userDO);

        Response response = userController.retrieveUserByUserId("5", new MockHttpServletResponse());

        Assert.assertEquals(response.getEntity(), userDO);
        Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
    }

    @Test
    public void testUserService_retrieveUserByUserId_returnFailure() throws CustomException {
        when(userService.retrieveUserByUserId(any(String.class))).thenReturn(null);

        Response response = userController.retrieveUserByUserId("5", new MockHttpServletResponse());

        Assert.assertEquals(response.getEntity(), Constants.USER_NOT_FOUND);
        Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void testUserService_retrieveUserByEmailAddress_returnSuccess() throws CustomException {
        when(userService.retrieveUserByEmailAddress("emailAddress4@gmail.com")).thenReturn(userDO);

        Response response = userController.retrieveUserByEmailAddress("emailAddress4@gmail.com", new MockHttpServletResponse());

        Assert.assertEquals(response.getEntity(), userDO);
        Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
    }

    @Test
    public void testUserService_retrieveUserByEmailAddress_returnFailure() throws CustomException {
        when(userService.retrieveUserByEmailAddress("emailAddress4@gmail.com")).thenThrow(CustomException.class);

        Response response = userController.retrieveUserByEmailAddress("emailAddress4@gmail.com", new MockHttpServletResponse());

        Assert.assertEquals(response.getEntity(), Constants.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

}
