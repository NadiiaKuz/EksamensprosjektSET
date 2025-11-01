package org.gruppe4.test.databaseTest;

import org.gruppe4.enums.Role;
import org.gruppe4.enums.UserType;
import org.gruppe4.model.User;
import org.gruppe4.repository.UsersRepository;
import org.gruppe4.test.databaseTest.testDB.TestContainerDatabase;
import org.gruppe4.test.databaseTest.testDB.TestDatabase;
import org.junit.jupiter.api.*;


import java.sql.Connection;

public class UsersRepositoryIntegrationTests {


    private final static TestDatabase testDB = new TestContainerDatabase();

    private static Connection connection;

    private static UsersRepository usersRepository;


    @BeforeAll
    public static void setUpTestDB() throws Exception{
        // Starter test-databasen
        connection = testDB.startDB();
        testDB.createTables();

        // Tillater å senere kunne rulle tilbake data-endringer.
        connection.setAutoCommit(false);

        usersRepository = new UsersRepository(connection);
    }


    @BeforeEach
    public void prepareTest() throws Exception{
        testDB.createDummyData();
    }


    @AfterEach
    public void cleanUpTest() throws Exception{
        connection.rollback();
    }


    @AfterAll
    public static void tearDownTestDB() throws Exception {
        testDB.stopDB();
    }

    @Test
    @DisplayName("createUser(): User is created successfully")
    public void createUser_UserIsCreatedSuccessfully() throws Exception{
        // Arrange
        User user = new User("Ola", "Nordmann", "olanor@example.com", "asdflkj", UserType.ADULT, Role.DEVELOPER);

        // Act
        usersRepository.createUser(user);

        // Assert
        Assertions.assertEquals("olanor@example.com", usersRepository.getUserByEmail("olanor@example.com").getMail());
    }

}
