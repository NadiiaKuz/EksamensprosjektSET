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

    public User getUserByName(String userName) {
        String sql = "SELECT u.idUser, u.name, u.`e-mail`, u.password, r.roleType, ut.userType " +
                "FROM User AS u " +
                "JOIN Role AS r ON u.Role_idRole=r.idRole " +
                "JOIN UserType AS ut ON u.UserType_idUserType=ut.idUserType " +
                "WHERE u.name=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);

            ResultSet resultSet = preparedStatement.executeQuery();

            int count = 0;
            User user = null;

            while (resultSet.next()) {
                count++;

                if (count>1){
                    throw new UserRepositoryException("Multiple users found with name: " + userName);
                }

                int userId = resultSet.getInt("idUser");
                String name = resultSet.getString("name");
                String email = resultSet.getString("e-mail");
                String password = resultSet.getString("password");
                Role roleType = Role.valueOf(resultSet.getString("roleType").toUpperCase());
                UserType userType = UserType.valueOf(resultSet.getString("userType").toUpperCase());

                user = new User(userId, name, email, password, userType, roleType);
            }

            return user;

        } catch (SQLException e) {
            throw new UserRepositoryException("Could not retrieve user by name " + userName, e);
        }
    }
}
