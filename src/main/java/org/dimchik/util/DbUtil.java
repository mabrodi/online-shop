package org.dimchik.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static final DbUtil INSTANCE = new DbUtil();

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private DbUtil() {
        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        dbUser = System.getenv("DB_USER");
        dbPassword = System.getenv("DB_PASSWORD");
        dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
    }

    public static DbUtil getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
