package org.gruppe4;

import org.gruppe4.database.MySQLDatabaseConnection;
import org.gruppe4.database.MySQLDatabaseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class dbMain {
    public static void main(String[] args){

        // Laster inn innstillinger fra dbConfig.properties fil
        Properties properties = new Properties();
        try (FileInputStream file = new FileInputStream("dbConfig.properties")) {
            properties.load(file);
        } catch (IOException e) {
            System.err.println("Could not load dbConfig.properties: " + e.getMessage());
            return;
        }

        // Mottar data
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        // Lager kobling
        MySQLDatabaseConnection dbConnection = new MySQLDatabaseConnection(url, user, password);
        Connection conn = null;

        try {
            conn = dbConnection.startDB();
            System.out.println("Connected successful");
        } catch (MySQLDatabaseException e) {
            System.err.println("DB connection failed: " + e.getMessage());
        } finally {
            dbConnection.stopDB();
        }

    }
}
