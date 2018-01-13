package com.cloud.controller;

import com.cloud.constants.Constants;
import com.cloud.exception.CustomException;
import com.cloud.model.UserDO;
import com.cloud.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Primary controller class that interfaces with clients with enforcement of security
 *
 */
@RestController
@RequestMapping(Constants.USERS_SECURE)
public class UserSecureController {

    private static final Logger LOGGER = LogManager.getLogger(UserSecureController.class);

    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = Constants.USER_RESOURCE_PATH, produces = MediaType.APPLICATION_JSON, consumes = {Constants.API_VERSION_1})
    public Response retrieveUserByUserId(@PathVariable(value = "userId") String userId, @Context HttpServletResponse httpServletResponse) {
        try {
            UserDO userDO = userService.retrieveUserByUserId(userId);

            if (userDO != null) {
                return Response.ok(HttpServletResponse.SC_OK).entity(userDO).build();
            } else {
                return Response.status(HttpServletResponse.SC_NOT_FOUND).entity(Constants.USER_NOT_FOUND).build();
            }
        } catch (CustomException ex) {
            LOGGER.error(Constants.INTERNAL_SERVER_ERROR);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(Constants.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON, consumes = {Constants.API_VERSION_1})
    public Response retrieveUserByEmailAddress(@QueryParam("emailAddress") String emailAddress, @Context HttpServletResponse httpServletResponse) {
        try {
            UserDO userDO = userService.retrieveUserByEmailAddress(emailAddress);

            if (userDO != null) {
                return Response.ok(HttpServletResponse.SC_OK).entity(userDO).build();
            } else {
                return Response.status(HttpServletResponse.SC_NOT_FOUND).entity(Constants.USER_NOT_FOUND).build();
            }
        } catch (CustomException ex) {
            LOGGER.error(Constants.INTERNAL_SERVER_ERROR);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(Constants.INTERNAL_SERVER_ERROR).build();
        }
    }

}