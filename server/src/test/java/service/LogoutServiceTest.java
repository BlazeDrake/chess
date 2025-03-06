package service;

import dataaccess.*;

import dataaccess.interfaces.AuthDAO;
import network.datamodels.AuthData;
import network.requests.LogoutRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class LogoutServiceTest {

    private AuthDAO authDAO;
    private LogoutService service;

    Connection connection;

    @BeforeEach
    public void setup() throws DataAccessException, SQLException {

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

        authDAO = new SQLAuthDAO(connection);
        service = new LogoutService(authDAO);

        authDAO.createAuth(new AuthData("abc123", "syl"));
        authDAO.createAuth(new AuthData("mmmmmm", "pattern"));


    }

    @AfterEach
    void cleanup() throws SQLException {
        connection.rollback();
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