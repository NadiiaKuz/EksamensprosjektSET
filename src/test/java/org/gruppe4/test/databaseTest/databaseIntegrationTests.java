package org.gruppe4.test.databaseTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;




public class databaseIntegrationTests {

    private static MySQLDatabaseConnectionTest testDB;

    @BeforeAll
    public static void setUpTestDB() throws Exception {
        // Starter testdatabasen
        testDB = new MySQLDatabaseConnectionTest();
        testDB.startDB();

    }


    //Oppretter ny dummydata for hver test
    @BeforeEach
    public void setUpTest() throws Exception {
        testDB.createDummyData();
    }


    // Stopper databasen når alle testene har kjørt
    @AfterAll
    public static void tearDownAfterTest() throws Exception {
        testDB.stopDB();
    }

    //Tester om det er det ble lagt inn dummydata databasen
    @Test
    public void testUserInsert() throws Exception {
        int userCount = testDB.countRowsInTable("Users");
        assertTrue(userCount > 0, "The users table has rows");
    }

    //Tester om det er mulig å hente ut fornavnet fra databasen
    @Test
    public void testGetUserFirstNameById() throws Exception {
        String firstName = testDB.getUserFirstName(1);
        assertNotNull(firstName);
    }

}
