package org.dimchik.util;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DbUtil {
    private static final DbUtil INSTANCE = new DbUtil();
    private DataSource dataSource;

    private DbUtil() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(System.getenv("DB_URL"));
        pgSimpleDataSource.setUser(System.getenv("DB_USER"));
        pgSimpleDataSource.setPassword(System.getenv("DB_PASSWORD"));

        dataSource = pgSimpleDataSource;
    }

    public static DbUtil getInstance() {
        return INSTANCE;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
