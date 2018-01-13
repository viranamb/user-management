package com.cloud.dao;

import com.cloud.LaunchApplication;
import com.cloud.model.UserDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * UserRepositoryDao test class. To be tested in isolation and not with the spring boot application already running.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LaunchApplication.class)
public class UserRepositoryDaoTests
{
    @Autowired
    private UserRepositoryDao userRepositoryDao;

    @Test
    public void createUser() {
        UserDO user = new UserDO("firstName4", "lastName4", "user4", "password4", "emailAddress4@gmail.com");
        String userId = userRepositoryDao.createUser(user);
        UserDO newUser = userRepositoryDao.retrieveUserByUserId(userId);

        Assert.assertNotNull(newUser);
        Assert.assertEquals("firstName4", newUser.getFirstName());
        Assert.assertEquals("lastName4", newUser.getLastName());
        Assert.assertEquals("user4", newUser.getUserName());
        Assert.assertEquals("password4", newUser.getPassword());
        Assert.assertEquals("emailAddress4@gmail.com", newUser.getEmailAddress());
    }

    @Test
    public void retrieveUserByCredentials() {
        UserDO user = userRepositoryDao.retrieveUserByCredentials("user4", "password4");

        Assert.assertNotNull(user);
        Assert.assertEquals("firstName4", user.getFirstName());
        Assert.assertEquals("lastName4", user.getLastName());
        Assert.assertEquals("emailAddress4@gmail.com", user.getEmailAddress());
    }

    @Test
    public void retrieveUserByUserId() {
        UserDO user = userRepositoryDao.retrieveUserByUserId("3");

        Assert.assertNotNull(user);
        Assert.assertEquals("firstName3", user.getFirstName());
        Assert.assertEquals("lastName3", user.getLastName());
        Assert.assertEquals("user3", user.getUserName());
        Assert.assertEquals("password3", user.getPassword());
        Assert.assertEquals("emailAddress3@gmail.com", user.getEmailAddress());
    }

    @Test
    public void retrieveUserByEmailAddress() {
        UserDO user = userRepositoryDao.retrieveUserByEmailAddress("emailAddress1@gmail.com");

        Assert.assertNotNull(user);
        Assert.assertEquals("firstName1", user.getFirstName());
        Assert.assertEquals("lastName1", user.getLastName());
        Assert.assertEquals("user1", user.getUserName());
        Assert.assertEquals("password1", user.getPassword());
    }

}