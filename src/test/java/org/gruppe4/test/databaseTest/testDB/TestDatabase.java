package org.gruppe4.test.databaseTest.testDB;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import org.gruppe4.enums.UserType;
import org.gruppe4.enums.Role;


public abstract class TestDatabase {

    protected Connection connection;

    public abstract void startDB() throws Exception;

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
                    "userType ENUM('STUDENT', 'ADULT', 'SENIOR', 'CHILD') NOT NULL)");

            statement.execute("CREATE TABLE Roles (idRole INT AUTO_INCREMENT PRIMARY KEY, " +
                    "roleType ENUM('USER', 'ADMIN') NOT NULL)");

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

            //Legger til dummydata for noen brukere
            insertIntoUsers("Karl", "Andersen", "karan@example.com", "lkmasdf",
                    "Oslo-Drøbak", 1, 2);
            insertIntoUsers("Kari", "Olsen", "karol@example.com", "kariolsen",
                    "Alta-Hammerfest", 2, 1);
            insertIntoUsers("Per", "Buljo", "pebul@example.com", "asdflkj",
                    "Fredrikstad-Sarpsborg", 4, 1);

        }
    }



    //Metode for å legge userType inn i databasen
    public void insertIntoUserTypes(String name) throws Exception{
        String sql = "INSERT INTO UserTypes (userType) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            UserType userType = UserType.valueOf(name);
            preparedStatement.setString(1, userType.name());
            preparedStatement.executeUpdate();
        }
    }

    //Metode for å legge roleType inn i databasen
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

        //Jeg følger forelesningseksempelet her, men får warning om bruk av String i sql
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    public String getUserFirstName(int idUser) throws Exception{
        //Lager en sql spørring, hvor idUser er placeholder først indeks(?)
        String sql = "SELECT firstName FROM Users WHERE idUser=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //Setter den første placeholderen i sql variabelen til å være vår parameter idUser
            preparedStatement.setInt(1, idUser);

            //Kjører sql-spørringen og returnnewrer et "resultSet" vi kan iterere gjennom
            //Ettersom at hver idUser er unik skal det kun være ett resultat
            ResultSet resultSet = preparedStatement.executeQuery();

            //Hvis et resultat finnes returnerer vi det som ligger i kolonnen med navn "firstName"
            //Ellers kaster vi en exception
            if (resultSet.next()) {
                return resultSet.getString("firstName");
            } else throw new Exception("No user found");
        }
    }

    public String getUserLastName(int idUser) throws Exception{
        String sql = "SELECT lastName FROM Users WHERE idUser=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("lastName");
            }else throw new Exception("No user found");
        }
    }

    public String getUserEmail(int idUser) throws Exception{
        String sql = "SELECT email FROM Users WHERE idUser=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("email");
            }else throw new Exception("No user found");

        }
    }

    public String getUserFavoriteRoute(int idUser) throws Exception{
        String sql = "SELECT favoriteRoute FROM Users WHERE idUser=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUser);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("favoriteRoute");
            }else throw new Exception("No user found");

        }
    }

    public String getUserType(int idUserType) throws Exception{
        String sql = "SELECT userType FROM UserTypes WHERE idUserType=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUserType);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("userType");
            }else throw new Exception("No userType found");
        }
    }

    public String getRoleType(int idRole) throws Exception{
        String sql = "SELECT roleType FROM Roles WHERE idRole=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idRole);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("roleType");
            }else throw new Exception("No roleType found");
        }
    }
}


