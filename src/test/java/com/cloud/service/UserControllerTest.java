package com.cloud.service;

import com.cloud.constants.Constants;
import com.cloud.controller.UserController;
import com.cloud.exception.CustomException;
import com.cloud.model.UserDO;
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
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController = new UserController();

    private UserDO userDO;

    @Before
    public void setUp() throws FileNotFoundException {
        userDO = new UserDO("firstName4", "lastName4", "user4", "password4", "emailAddress4@gmail.com");
    }

    @Test
    public void testUserService_userSignUp_returnSuccess() throws CustomException {
        when(userService.createUser(any(UserDO.class))).thenReturn("10");

        Response response = userController.userSignUp(userDO, new MockHttpServletResponse());

        Assert.assertEquals(response.getEntity(), "10");
        Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
    }

    @Test
    public void testUserService_userSignUp_returnFailure() throws CustomException {
        when(userService.createUser(any(UserDO.class))).thenThrow(CustomException.class);

        Response response = userController.userSignUp(userDO, new MockHttpServletResponse());

        Assert.assertEquals(response.getEntity(), Constants.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testUserService_userSignIn_returnSuccess() throws CustomException {
        when(userService.retrieveUserByCredentials(any(String.class), any(String.class))).thenReturn(true);

        Response response = userController.userSignIn(userDO, new MockHttpServletResponse());

        Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_OK);
    }

    @Test
    public void testUserService_userSignIn_returnFailure() throws CustomException {
        when(userService.retrieveUserByCredentials(any(String.class), any(String.class))).thenReturn(false);

        Response response = userController.userSignIn(userDO, new MockHttpServletResponse());

        Assert.assertEquals(response.getEntity(), Constants.INVALID_CREDENTIALS);
        Assert.assertEquals(response.getStatus(), HttpServletResponse.SC_UNAUTHORIZED);
    }
}
