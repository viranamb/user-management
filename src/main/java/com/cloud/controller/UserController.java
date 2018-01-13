package com.cloud.controller;

import com.cloud.constants.Constants;
import com.cloud.exception.CustomException;
import com.cloud.model.UserDO;
import com.cloud.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Primary controller class that interfaces with clients
 *
 */
@RestController
@RequestMapping(Constants.USERS)
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = {Constants.API_VERSION_1})
    public Response userSignUp(@RequestBody UserDO userDO, @Context HttpServletResponse httpServletResponse) {
        try {
            String userId = userService.createUser(userDO);
            return Response.ok(HttpServletResponse.SC_OK).entity(userId).build();
        } catch (CustomException ex) {
            LOGGER.error(Constants.INTERNAL_SERVER_ERROR);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(Constants.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = Constants.USER_SIGN_IN, produces = MediaType.APPLICATION_JSON, consumes = {Constants.API_VERSION_1})
    public Response userSignIn(@RequestBody UserDO userDO, @Context HttpServletResponse httpServletResponse) {
        try {
            boolean isValidUser = userService.retrieveUserByCredentials(userDO.getUserName(), userDO.getPassword());

            if (isValidUser) {
                String jwtToken = Jwts.builder().setSubject(userDO.getUserName()).claim("roles", "user").setIssuedAt(new Date())
                        .signWith(SignatureAlgorithm.HS256, "secretkey").compact();
                return Response.ok(HttpServletResponse.SC_OK).entity(jwtToken).build();
            } else {
                return Response.status(HttpServletResponse.SC_UNAUTHORIZED).entity(Constants.INVALID_CREDENTIALS).build();
            }

        } catch (CustomException ex) {
            LOGGER.error(Constants.INTERNAL_SERVER_ERROR);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).entity(Constants.INTERNAL_SERVER_ERROR).build();
        }
    }

}