package org.gruppe4.test.databaseTest.testDB;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.gruppe4.enums.UserType;
import org.gruppe4.enums.Role;


public abstract class TestDatabase {

    protected Connection connection;

    public abstract Connection startDB() throws Exception;

    public abstract void stopDB() throws Exception;



    public void createTables() throws Exception{
        try (Statement statement = connection.createStatement()) {

            //Dropper tabeller om de eksisterer, så alle testene har samme utgangspunkt
            statement.execute("DROP TABLE IF EXISTS Users");
            statement.execute("DROP TABLE IF EXISTS Roles");
            statement.execute("DROP TABLE IF EXISTS UserTypes");

            // Lager tabeller i samme format som vi har dem i databasen
            // Starter med UserTypes og Role fordi Users har fremmednøkler fra dem
            statement.execute("CREATE TABLE UserTypes (idUserType INT AUTO_INCREMENT PRIMARY KEY, " +
                    "userType VARCHAR(45))");

            statement.execute("CREATE TABLE Roles (idRole INT AUTO_INCREMENT PRIMARY KEY, " +
                    "roleType VARCHAR(45))");

            statement.execute("CREATE TABLE Users (idUser INT AUTO_INCREMENT PRIMARY KEY, " +
                    "firstName VARCHAR(45), " + "lastName VARCHAR(45), " + "email VARCHAR(45), " +
                    "password VARCHAR(45), " + "favoriteRoute VARCHAR(45), " +
                    "UserTypes_idUserType INT," +
                    "Roles_idRole INT," +
                    "FOREIGN KEY (UserTypes_idUserType) references UserTypes(idUserType), " +
                    "FOREIGN KEY (Roles_idRole) references Roles(idRole))");
        }
    }

    public void createDummyData() throws Exception{
        try (Statement statement = connection.createStatement()) {

            //Legger inn brukertypene i dummydata
            insertIntoUserTypes("STUDENT");
            insertIntoUserTypes("ADULT");
            insertIntoUserTypes("SENIOR");
            insertIntoUserTypes("CHILD");

            //Legger inn rollene i dummydata
            insertIntoRoles("USER");
            insertIntoRoles("ADMIN");

            //Legger til dummydata for et par brukere
            insertIntoUsers("Karl", "Andersen", "karan@example.com", "lkmasdf",
                    "Oslo-Drøbak", 1, 2);
            insertIntoUsers("Kari", "Olsen", "karol@example.com", "kariolsen",
                    "Alta-Hammerfest", 2, 1);


        }
    }




    public void insertIntoUserTypes(String name) throws Exception{
        String sql = "INSERT INTO UserTypes (userType) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            UserType userType = UserType.valueOf(name);
            preparedStatement.setString(1, userType.name());
            preparedStatement.executeUpdate();
        }
    }

    public void insertIntoRoles(String name) throws Exception{
        String sql = "INSERT INTO Roles (roleType) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            Role role = Role.valueOf(name);
            preparedStatement.setString(1, role.name());
            preparedStatement.executeUpdate();
        }
    }

    public void insertIntoUsers(String firstName, String lastName, String email, String password, String favoriteRoute,
                                int UserTypes_idUserType, int Roles_idRole) throws Exception{
        String sql = "INSERT INTO Users (firstName, lastName, email, password, favoriteRoute," +
                "UserTypes_idUserType," + "Roles_idRole ) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, favoriteRoute);
            preparedStatement.setInt(6,UserTypes_idUserType);
            preparedStatement.setInt(7,Roles_idRole);
            preparedStatement.executeUpdate();
        }
    }

    public int countRowsInTable(String tableName) throws Exception{
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public String getUserFirstName(int idUser) throws Exception{
        String sql = "SELECT firstName FROM Users WHERE idUser=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("firstName");
        }
    }
}


