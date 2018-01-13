package com.cloud.service;

import com.cloud.dao.UserRepositoryDao;
import com.cloud.exception.CustomException;
import com.cloud.model.UserDO;
import com.cloud.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataAccessException;

import java.io.FileNotFoundException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepositoryDao userRepositoryDao;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    private UserDO userDO;

    @Before
    public void setUp() throws FileNotFoundException {
        userDO = new UserDO("firstName4", "lastName4", "user4", "password4", "emailAddress4@gmail.com");
    }

    @Test
    public void testUserService_createUser_returnSuccess() throws CustomException {
        when(userRepositoryDao.createUser(any(UserDO.class))).thenReturn("10");

        String userId = userService.createUser(userDO);

        Assert.assertEquals(userId, "10");
        verify(userRepositoryDao, times(1)).createUser(any(UserDO.class));
    }

    @Test(expected = CustomException.class)
    public void testUserService_createUser_returnFailure() throws CustomException {
        when(userRepositoryDao.createUser(any(UserDO.class))).thenThrow(new DataAccessException("Error") {});

        userService.createUser(userDO);
    }

    @Test
    public void testUserService_retrieveUserByCredentials_returnSuccess() throws CustomException {
        when(userRepositoryDao.retrieveUserByCredentials("user4", "password4")).thenReturn(userDO);

        boolean isUserExists = userService.retrieveUserByCredentials("user4", "password4");

        Assert.assertTrue(isUserExists);
        verify(userRepositoryDao, times(1)).retrieveUserByCredentials(any(String.class), any(String.class));
    }

    @Test(expected = CustomException.class)
    public void testUserService_retrieveUserByCredentials_returnFailure() throws CustomException {
        when(userRepositoryDao.retrieveUserByCredentials("user4", "password4")).thenThrow(new DataAccessException("Error") {});

        userService.retrieveUserByCredentials("user4", "password4");
    }

    @Test
    public void testUserService_retrieveUserByEmailAddress_returnSuccess() throws CustomException {
        when(userRepositoryDao.retrieveUserByEmailAddress("emailAddress4@gmail.com")).thenReturn(userDO);

        UserDO userDO = userService.retrieveUserByEmailAddress("emailAddress4@gmail.com");

        Assert.assertNotNull(userDO);
        verify(userRepositoryDao, times(1)).retrieveUserByEmailAddress(any(String.class));
    }

    @Test(expected = CustomException.class)
    public void testUserService_retrieveUserByEmailAddress_returnFailure() throws CustomException {
        when(userRepositoryDao.retrieveUserByEmailAddress("emailAddress4@gmail.com")).thenThrow(new DataAccessException("Error") {});

        userService.retrieveUserByEmailAddress("emailAddress4@gmail.com");
    }

    @Test
    public void testUserService_retrieveUserByUserId_returnSuccess() throws CustomException {
        when(userRepositoryDao.retrieveUserByUserId("5")).thenReturn(userDO);

        UserDO userDO = userService.retrieveUserByUserId("5");

        Assert.assertNotNull(userDO);
        verify(userRepositoryDao, times(1)).retrieveUserByUserId(any(String.class));
    }

    @Test(expected = CustomException.class)
    public void testUserService_retrieveUserByUserId_returnFailure() throws CustomException {
        when(userRepositoryDao.retrieveUserByUserId("5")).thenThrow(new DataAccessException("Error") {});

        userService.retrieveUserByUserId("5");
    }
}
