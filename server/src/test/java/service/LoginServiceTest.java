package service;

import dataaccess.*;

import dataaccess.interfaces.UserDAO;
import network.datamodels.UserData;
import network.requests.LoginRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {


    LoginService service;
    UserDAO userDAO;

    Connection connection;

    @BeforeEach
    void setup() throws DataAccessException, SQLException {

        DatabaseManager.createDatabase();
        connection = DatabaseManager.getConnection();


        userDAO = new SQLUserDAO(connection);
        service = new LoginService(userDAO, new SQLAuthDAO(connection));

        DatabaseManager.reset();

        userDAO.createUser(new UserData("nightblood", "evil", "ex@roshar.com"));
    }


    @Test
    void loginValid() throws DataAccessException {
        var request = new LoginRequest("nightblood", "evil");
        var result = service.login(request);
        assertNotNull(result);
        assertNotNull(userDAO.getUser("nightblood"));
    }

    @Test
    void loginInvalidBadInfo() {
        var request = new LoginRequest("nightblood", "pancakes");
        assertThrows(UnauthorizedException.class, () -> service.login(request));
    }

    @Test
    void loginInvalidNonexistent() {
        var request = new LoginRequest("pattern", "lies");
        assertThrows(UnauthorizedException.class, () -> service.login(request));
    }
}