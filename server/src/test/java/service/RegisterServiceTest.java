package service;

import dataaccess.*;
import dataaccess.interfaces.UserDAO;
import network.datamodels.UserData;
import network.requests.RegisterRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {
    RegisterService service;

    UserDAO userDAO;
    Connection connection;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {

        DatabaseManager.createDatabase();
        connection = DatabaseManager.getConnection();

        connection.setAutoCommit(false);
        String sql = "truncate table ?";
        for (int i = 0; i < DatabaseManager.TABLES.length; i++) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, DatabaseManager.TABLES[i]);
                stmt.executeUpdate();
            }
        }

        userDAO = new SQLUserDAO(connection);

        service = new RegisterService(userDAO, new SQLAuthDAO(connection));
    }

    @AfterEach
    void cleanup() throws SQLException {
        connection.rollback();
    }

    @Test
    public void testAddUser() {
        var usersToAdd = new ArrayList<UserData>(List.of(
                new UserData("username", "password", "email@test,com"),
                new UserData("next", "cool", "email@test,com"),
                new UserData("final", "password", "evil@nightblood,com")
        ));
        try {
            for (var user : usersToAdd) {
                service.register(new RegisterRequest(user));

            }
            //Check to ensure lists line up
            for (var user : usersToAdd) {
                assertNotNull(userDAO.getUser(user.username()));
            }
        } catch (DataAccessException e) {
            fail("Unexpect ed error: " + e.getMessage());
        }
    }

    @Test
    public void testTakenUsername() {

        var user1 = new UserData("username", "password", "email@test,com");
        var user2 = new UserData("username", "cool", "email@test,com");
        try {
            service.register(new RegisterRequest(user1));
        } catch (DataAccessException e) {
            fail("Unexpected error: " + e.getMessage());
        }

        assertThrows(TakenException.class, () -> service.register(new RegisterRequest(user2)));
    }
}