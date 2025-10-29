package org.gruppe4.test.databaseTest;

import org.gruppe4.database.MySQLDatabaseConnection;
import org.gruppe4.test.databaseTest.testDB.TestDatabase;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import java.sql.Connection;

public class MySQLDatabaseConnectionTest extends TestDatabase {

    private static final MySQLContainer<?> mysql =
            new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpassword");

    private MySQLDatabaseConnection dbConnection;

    @Override
    public Connection startDB() throws Exception {
        mysql.start();

        dbConnection = new MySQLDatabaseConnection(
                mysql.getJdbcUrl(),
                mysql.getUsername(),
                mysql.getPassword()
        );

        connection = dbConnection.startDB();
        createTables();
        createDummyData();
        return connection;
    }

    @Override
    public void stopDB() throws Exception {
        dbConnection.stopDB();
        mysql.stop();
    }
}

