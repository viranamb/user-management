package com.cloud.service.impl;

import com.cloud.constants.Constants;
import com.cloud.dao.UserRepositoryDao;
import com.cloud.exception.CustomException;
import com.cloud.model.UserDO;
import com.cloud.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * Service class that abstracts the data retrieval implementation details from the Controller class.
 *
 * This class also interfaces with the data repository layer.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepositoryDao userRepositoryDao;

    /**
     *
     * @param userDO
     * @return user identifier (as a String)
     * @throws CustomException
     */
    @Override
    public String createUser(UserDO userDO) throws CustomException {
        try {
            String userId = userRepositoryDao.createUser(userDO);
            return userId;
        } catch (DataAccessException ex) {
            LOGGER.error(Constants.DATABASE_ERROR, ex);
            throw new CustomException(Constants.DATABASE_ERROR, ex);
        }
    }

    @Override
    public boolean retrieveUserByCredentials(String userName, String password) throws CustomException {
        try {
            UserDO userDO = userRepositoryDao.retrieveUserByCredentials(userName, password);
            if (userDO != null) {
                return true;
            }
        } catch (DataAccessException ex) {
            LOGGER.error(Constants.DATABASE_ERROR, ex);
            throw new CustomException(Constants.DATABASE_ERROR, ex);
        }
        return false;
    }

    @Override
    public UserDO retrieveUserByUserId(String userId) throws CustomException {
        try {
            UserDO userDO = userRepositoryDao.retrieveUserByUserId(userId);
            return userDO;
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.error(Constants.USER_NOT_FOUND, ex);
            return null;
        } catch (DataAccessException ex) {
            LOGGER.error(Constants.DATABASE_ERROR, ex);
            throw new CustomException(Constants.DATABASE_ERROR, ex);
        }
    }

    @Override
    public UserDO retrieveUserByEmailAddress(String emailAddress) throws CustomException {
        try {
            UserDO userDO = userRepositoryDao.retrieveUserByEmailAddress(emailAddress);
            return userDO;
        } catch (DataAccessException ex) {
            LOGGER.error(Constants.DATABASE_ERROR, ex);
            throw new CustomException(Constants.DATABASE_ERROR, ex);
        }
    }

}
