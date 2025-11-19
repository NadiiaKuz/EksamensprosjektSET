import org.gruppe4.enums.Role;
import org.gruppe4.model.User;
import org.gruppe4.repository.UserRepositoryException;
import org.gruppe4.repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryUnitTests {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    public void setup() throws Exception{
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        mockUserResultSet(mockResultSet);
    }

    @Test
    public void getUserByEmail_ReturnsUser_OneUserFound() throws Exception{

        // Arrange
        when(mockResultSet.next()).thenReturn(true, false); // simulerer bare en user
        UsersRepository repository = new UsersRepository(mockConnection);

        // Act
        User resultUser = repository.getUserByEmail("ola@example.com");

        // Assert
        Assertions.assertNotNull(resultUser);
        Assertions.assertEquals("Ola", resultUser.getFirstName());
        Assertions.assertEquals(Role.USER, resultUser.getRole());
    }

    @Test
    public void getUserByEmail_ReturnThrow_MultipleUserFound() throws Exception{
        // Arrange
        when(mockResultSet.next()).thenReturn(true, true); // simulerer mange users
        UsersRepository repository = new UsersRepository(mockConnection);

        // Assert
        Assertions.assertThrows(UserRepositoryException.class,
                () -> repository.getUserByEmail("dumy@example.com"));
    }

    private void mockUserResultSet(ResultSet resultSet) throws SQLException{
        when(resultSet.getInt("idUser")).thenReturn(1);
        when(resultSet.getString("firstName")).thenReturn("Ola");
        when(resultSet.getString("lastName")).thenReturn("Nordman");
        when(resultSet.getString("email")).thenReturn("ola@example.com");
        when(resultSet.getString("password")).thenReturn("pass123");
        when(resultSet.getString("roleType")).thenReturn("USER");
        when(resultSet.getString("userType")).thenReturn("ADULT");
    }
}
