/*
Noen tester jeg skrev før jeg fant ut at jeg kun trengte å teste createUser() fra branchen feature---createUserInDatabase
Jeg hentet alle nødvendige filer derfra og lagde ny integrasjonstest i UsersRepositoryIntegrationTests.java
package org.gruppe4.test.databaseTest;


import org.gruppe4.test.databaseTest.testDB.TestContainerDatabase;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;




public class DatabaseIntegrationTests {

    private static TestContainerDatabase testDB;

    @BeforeAll
    public static void setUpTestDB() throws Exception {
        // Starter testdatabasen
        testDB = new TestContainerDatabase();
        testDB.startDB();
        testDB.createTables();
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
        assertTrue(userCount > 0, "The Users table has no rows");
    }

    //Tester om det er mulig å hente ut fornavnet fra databasen
    @Test
    public void testGetUserFirstNameById() throws Exception {
        String firstName = testDB.getUserFirstName(1);
        assertNotNull(firstName);
        System.out.println("First Name: " + firstName);
    }

    @Test
    public void testGetUserFirstNameInvalidId() {
        //Hvis den forventede exception blir kastet returneres den
        Exception exception =
                assertThrows(Exception.class,
                        () -> testDB.getUserFirstName(-1));

        //Forventet feilmelding når idUser = -1
        String expectedMessage = "No user found";
        //Den faktiske feilmeldingen
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);

        //asserter at den forventede feilmeldingen og den faktiske feilmeldingen er den samme
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testGetUserLastNameById() throws Exception {
        String lastName = testDB.getUserLastName(2);
        assertNotNull(lastName);
        System.out.println("Last Name: " + lastName);
    }

    @Test
    public void testGetUserLastNameInvalidId() {
        Exception exception =
                assertThrows(Exception.class,
                        () -> testDB.getUserLastName(800));
        String expectedMessage = "No user found";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testGetUserEmailById() throws Exception {
        String email = testDB.getUserEmail(3);
        assertNotNull(email);
        System.out.println("Email: " + email);
    }

    @Test
    public void testGetUserEmailInvalidId() {
        Exception exception =
                assertThrows(Exception.class,
                        () -> testDB.getUserEmail(-500));
        String expectedMessage = "No user found";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetUserFavoriteRouteById() throws Exception {
        String route = testDB.getUserFavoriteRoute(2);
        assertNotNull(route);
        System.out.println("Favorite Route: " + route);
    }

    @Test
    public void testGetUserFavoriteRouteInvalidId() {
        Exception exception =
                assertThrows(Exception.class,
                        () -> testDB.getUserFavoriteRoute(-5000));
        String expectedMessage = "No user found";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
    //Tester om det er mulig å legge til en bruker og å deretter få ut informasjon om dem
    @Test
    public void testInsertAndRetrieveUser() throws Exception {
        testDB.insertIntoUsers("Petter", "Northug", "petsol@example.com",
                "Æ_e_konge", "Byåsen-Moholt",
                2, 2);
        int userCount = testDB.countRowsInTable("Users");

        //Ser om userCount er mer enn de 3 i dummydata
        assertTrue(userCount > 3, "The user was not added to the database");

        //Setter email til å være emailen av brukeren med idUser = userCount (den siste som ble lagt til)
        String email = testDB.getUserEmail(userCount);

        //Asserter at den emailen skal være emailen til Petter Northug
        assertEquals("petsol@example.com", email);
    }

    //Tester om databasen gjenskaper seg selv i henhold til dummydataen
    @Test
    public void testDatabaseRecreation() throws Exception {
        testDB.createTables();
        testDB.createDummyData();

        testDB.createTables();
        testDB.createDummyData();
        int userCount = testDB.countRowsInTable("Users");

        assertTrue(userCount > 0, "The users table has no rows");
        assertTrue(userCount < 4, "The users table has not been cleanly recreated");
    }

    @Test
    public void testGetUserType() throws Exception {
        String userType = testDB.getUserType(4);
        assertNotNull(userType);
        System.out.println("User Type: " + userType);
    }

    @Test
    public void testGetUserTypeInvalidId() {
        Exception exception =
                assertThrows(Exception.class,
                        () -> testDB.getUserType(-1));
        String expectedMessage = "No userType found";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    //Sjekker at enum restriksjonen for UserType fungerer
    @Test
    public void testInsertInvalidUserType(){
        Exception exception
                = assertThrows(Exception.class, () ->
                testDB.insertIntoUserTypes("MILITARY"));
        String expectedMessage = "No enum constant org.gruppe4.enums.UserType.MILITARY";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetRoleType() throws Exception {
        String roleType = testDB.getRoleType(2);
        assertNotNull(roleType);
        System.out.println("Role Type: " + roleType);
    }

    //Sjekker hvordan getRoleType håndterer feil idRole i parameteren
    @Test
    public void testGetRoleTypeInvalidId() {
        Exception exception =
                assertThrows(Exception.class,
                        () -> testDB.getRoleType(5000));
        String expectedMessage = "No roleType found";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    //Sjekker at enum restriksjonen for Roles fungerer
    @Test
    public void testInsertInvalidRoleType(){
        Exception exception =
                assertThrows(Exception.class,
                        () -> testDB.insertIntoRoles("DEVELOPER"));
        String expectedMessage = "No enum constant org.gruppe4.enums.Role.DEVELOPER";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
}


 */