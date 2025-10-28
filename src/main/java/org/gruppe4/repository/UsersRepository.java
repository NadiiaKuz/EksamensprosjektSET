package org.gruppe4.repository;

import org.gruppe4.enums.Role;
import org.gruppe4.enums.UserType;
import org.gruppe4.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsersRepository {

    private Connection connection;

    public UsersRepository(Connection connection) {
        this.connection = connection;
    }

    public User getUserByEmail(String userEmail) {
        String sql = "SELECT u.idUser, u.firstName, u.lastName, u.email, u.password, r.roleType, ut.userType " +
        "FROM Users AS u " +
        "JOIN Roles AS r ON u.Roles_idRole=r.idRole " +
        "JOIN UserTypes AS ut ON u.UserTypes_idUserType=ut.idUserType " +
        "WHERE u.email=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userEmail);

            ResultSet resultSet = preparedStatement.executeQuery();

            int count = 0;
            User user = null;

            while (resultSet.next()) {
                count++;

                if (count>1){
                    throw new UserRepositoryException("Multiple users found with email: " + userEmail);
                }

                int userId = resultSet.getInt("idUser");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                Role roleType = Role.valueOf(resultSet.getString("roleType").toUpperCase());
                UserType userType = UserType.valueOf(resultSet.getString("userType").toUpperCase());

                user = new User(userId, firstName, lastName, email, password, userType, roleType);
            }

            return user;

        } catch (SQLException e) {
            throw new UserRepositoryException("Could not retrieve user by email " + userEmail, e);
        }
    }

    public void createUser(User newUser) {
        String sql = "INSERT INTO Users (firstName, lastName, email, password, Roles_idRole, UserTypes_idUserType) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newUser.getFirstName());
            preparedStatement.setString(2, newUser.getLastName());
            preparedStatement.setString(3, newUser.getMail());
            preparedStatement.setString(4, newUser.getPassword());
            preparedStatement.setInt(5, mapRoleToId(newUser.getRole()));
            preparedStatement.setInt(6, mapUserTypeToId(newUser.getUserType()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0){
                throw new UserRepositoryException("User creation failed");
            }

        } catch (SQLException e) {
            throw new UserRepositoryException("Could not create user " + newUser.getMail(), e);
        }
    }

    private int mapRoleToId(Role role) {
        return switch (role) {
            case USER -> 1;
            case ADMIN -> 2;
            case DEVELOPER -> 3;
        };
    }

    private int mapUserTypeToId(UserType userType) {
        return switch (userType) {
            case STUDENT -> 1;
            case ADULT -> 2;
            case SENIOR -> 3;
            case CHILD -> 4;
        };
    }
}
