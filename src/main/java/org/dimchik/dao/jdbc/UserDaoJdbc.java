package org.dimchik.dao.jdbc;

import org.dimchik.dao.UserDao;
import org.dimchik.dao.mapper.UserRowMapper;
import org.dimchik.entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoJdbc implements UserDao {
    private final DataSource dataSource;

    private static final String SELECT_BY_EMAIL_AND_PASSWORD = "SELECT id, name, email, password FROM users WHERE email = ?";

    public UserDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User findByEmail(String email) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_EMAIL_AND_PASSWORD)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return UserRowMapper.mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed for login", e);
        }

        return null;
    }
}
