package com.cloud.dao;

import com.cloud.model.UserDO;
import org.springframework.stereotype.Repository;

/**
 * Interface to clients wanting to manage user information
 */
@Repository
public interface UserRepositoryDao {

    String createUser(UserDO userDO);

    UserDO retrieveUserByCredentials(String userName, String password);

    UserDO retrieveUserByUserId(String userId);

    UserDO retrieveUserByEmailAddress(String emailAddress);

}

