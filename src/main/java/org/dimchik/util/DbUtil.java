package org.dimchik.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public DbUtil() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("db.properties not found in classpath");
            }

            prop.load(input);

            String driver = prop.getProperty("db.driver");
            if (driver != null && !driver.isEmpty()) {
                Class.forName(driver);
            }

            dbUrl = prop.getProperty("db.url");
            dbUser = prop.getProperty("db.username");
            dbPassword = prop.getProperty("db.password");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load DB config", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
