package org.gruppe4.test.databaseTest.testDB;

import org.testcontainers.containers.MySQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;


//Denne klassen er basert på forelesningseksempelet til Ole Edvard

public class TestContainerDatabase extends TestDatabase {

    public final static String DB_NAME = "testdb";
    public final static String USERNAME = "user";
    public final static String PASSWORD = "password";

    //Konfigurerer en MySQLContainer med navnet og innloggingsvariablene
    private final static MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    @Override
    public Connection startDB() throws Exception {
        //Starter containeren, dette krever å ha Docker Desktop kjørende på maskinen
        container.start();
        connection = DriverManager.getConnection(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        return connection;
    }

    @Override
    public void stopDB() throws Exception {
        //Lukker containeren
        connection.close();
        container.stop();
    }
}

