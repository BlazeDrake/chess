package service;

import dataaccess.DataAccessException;
import dataaccess.SQLUserDAO;
import dataaccess.UnauthorizedException;

import dataaccess.interfaces.UserDAO;
import network.datamodels.AuthData;
import network.requests.LogoutRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {

    private UserDAO userDAO;
    private LogoutService service;

    @BeforeEach
    public void setup() {
        userDAO = new SQLUserDAO();
        service = new LogoutService();

        var authTokens = new TreeMap<String, AuthData>();
        authTokens.put("abc123", new AuthData("abc123", "syl"));
        authTokens.put("mmmmmm", new AuthData("mmmmmm", "pattern"));


    }

    @Test
    public void logoutValid() throws DataAccessException {

        service.logout(new LogoutRequest("abc123"));
        assertThrows(UnauthorizedException.class, () -> service.logout(new LogoutRequest("abc123")));
        service.logout(new LogoutRequest("mmmmmm"));
        assertThrows(UnauthorizedException.class, () -> service.logout(new LogoutRequest("mmmmmm")));
    }

    @Test
    public void logoutInvalid() {
        assertThrows(UnauthorizedException.class, () -> service.logout(new LogoutRequest("airsick")));
    }
}