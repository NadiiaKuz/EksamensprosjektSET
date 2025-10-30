package org.gruppe4.test.databaseTest.testDB;

import org.gruppe4.database.MySQLDatabaseConnection;
import org.gruppe4.database.MySQLDatabaseException;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class MySQLDatabaseConnectionTest extends TestDatabase {

    //Lager et objekt av MySQLDatabaseConnection klassen
    private MySQLDatabaseConnection dbConnection;

    //Oppretter en testcontainer til bruk i Docker Desktop
    @Container
    private final static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0")) // sjekker om det finnes en local mysql container
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    //Lager override av startDB() metoden, slik at den kan brukes med testcontaineren
    @Override
    public void startDB() throws MySQLDatabaseException {

        if(!mysql.isRunning()){
            mysql.start();
        }
        //Bruker MySQLDatabaseConnection til å lage en forbindelse med credentials fra Docker Containeren
        dbConnection = new MySQLDatabaseConnection(
                mysql.getJdbcUrl(),
                mysql.getUsername(),
                mysql.getPassword()
        );

        //kjører startDB() i MySqlDataBaseConnection med credentials fra dbConnection, som returnerer et Connection objekt vi lagrer i connection (arvet fra TestDatabase)
        connection = dbConnection.startDB();

    }
    //Lukker testdatabasen
    //Brukte å kaste Exception men fikk en commit warning om at den ikke vil kaste exception
    @Override
    public void stopDB() {
        if (dbConnection!= null){
        dbConnection.stopDB();
        mysql.stop();
    }
}}

