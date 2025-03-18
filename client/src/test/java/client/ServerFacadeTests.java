package client;

import dataaccess.*;
import network.datamodels.UserData;
import org.junit.jupiter.api.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    private static Connection connection;

    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);


        connection = DatabaseManager.getConnection();
    }

    @BeforeEach
    public void prep() throws DataAccessException {
        String sql = "truncate table games;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        sql = "truncate table auth;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        sql = "truncate table users;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testRegisterValid() {
        var req = new UserData("kal", "storms", "roshar.com");
        Assertions.assertDoesNotThrow(() -> facade.register(req));
    }

    @Test
    public void testRegisterInvalid() {
        var req = new UserData("kal", "storms", "roshar.com");
        Assertions.assertDoesNotThrow(() -> facade.register(req));
        try {
            facade.register(req);
            Assertions.fail("Did not throw an error fro duplicate registration");
        } catch (ResponseException e) {
            Assertions.assertEquals(403, e.StatusCode());
        }
    }

}
