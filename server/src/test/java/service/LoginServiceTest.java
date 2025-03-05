package service;

import dataaccess.DataAccessException;
import dataaccess.SQLUserDAO;
import dataaccess.UnauthorizedException;

import dataaccess.interfaces.UserDAO;
import network.datamodels.UserData;
import network.requests.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {


    LoginService service;
    UserDAO userDAO;

    @BeforeEach
    void setup() throws DataAccessException {
        service = new LoginService();
        userDAO = new SQLUserDAO();

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