package org.gruppe4.test.databaseTest;

import org.gruppe4.database.MySQLDatabaseConnection;
import org.gruppe4.test.databaseTest.testDB.TestDatabase;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import java.sql.Connection;

public class MySQLDatabaseConnectionTest extends TestDatabase {

    //Lager container til bruk i Docker Desktop
    private static final MySQLContainer<?> mysql =
            new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpassword");

    //Lager et objekt av MySQLDatabaseConnection klassen
    private MySQLDatabaseConnection dbConnection;

    //Lager override av startDB() metoden, slik at den kan brukes med testcontaineren
    @Override
    public Connection startDB() throws Exception {
        mysql.start();

        //Starter docker containeren, dette krever at man har Docker Desktop installert
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
    //Lukker testdatabasen
    @Override
    public void stopDB() throws Exception {
        dbConnection.stopDB();
        mysql.stop();
    }
}

