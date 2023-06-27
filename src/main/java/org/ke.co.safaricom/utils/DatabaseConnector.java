package org.javasparkips.utils;

import org.sql2o.Sql2o;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnector {
    private Sql2o sql2o;

    public DatabaseConnector() {
        // Default constructor
    }

    public void init() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        sql2o = new Sql2o(url, username, password);
    }

    public Sql2o getSql2o() {
        return sql2o;
    }
}
