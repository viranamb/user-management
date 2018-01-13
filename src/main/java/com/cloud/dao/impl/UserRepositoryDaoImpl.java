package com.cloud.dao.impl;

import com.cloud.dao.UserRepositoryDao;
import com.cloud.model.UserDO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class acts as the facade to the database.
 */
@Repository
public class UserRepositoryDaoImpl implements UserRepositoryDao {

    private static final Logger LOGGER = LogManager.getLogger(UserRepositoryDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String createUser(UserDO userDO) {
        {
            final String sql = "insert into users(firstName, lastName, userName, password, emailAddress) values(?, ?, ?, ?, ?)";

            KeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, userDO.getFirstName());
                    ps.setString(2, userDO.getLastName());
                    ps.setString(3, userDO.getUserName());
                    ps.setString(4, userDO.getPassword());
                    ps.setString(5, userDO.getEmailAddress());
                    return ps;
                }
            }, holder);

            int userId = holder.getKey().intValue();
            return userId + "";
        }
    }

    @Override
    public UserDO retrieveUserByCredentials(String userName, String password) {
        return jdbcTemplate.queryForObject(
                "select * from users where userName=? and password=?",
                new Object[]{userName, password}, new UserRowMapper());
    }

    @Override
    public UserDO retrieveUserByUserId(String userId) {
        return jdbcTemplate.queryForObject(
                "select * from users where id=?",
                new Object[]{userId}, new UserRowMapper());
    }

    @Override
    public UserDO retrieveUserByEmailAddress(String emailAddress) {
        return jdbcTemplate.queryForObject(
                "select * from users where emailAddress=?",
                new Object[]{emailAddress}, new UserRowMapper());
    }

    class UserRowMapper implements RowMapper<UserDO>
    {
        @Override
        public UserDO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserDO user = new UserDO();
            user.setId(rs.getInt("id") + "");
            user.setFirstName(rs.getString("firstName"));
            user.setLastName(rs.getString("lastName"));
            user.setUserName(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            user.setEmailAddress(rs.getString("emailAddress"));
            return user;
        }
    }

}

