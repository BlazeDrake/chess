package client;

import dataaccess.*;
import network.datamodels.UserData;
import network.results.LoginResult;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
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


    private static void insertUser(UserData userData) throws DataAccessException {
        String hash = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userData.username());
            stmt.setString(2, hash);
            stmt.setString(3, userData.email());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

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

    @Test
    public void testLoginValid() throws DataAccessException {
        var expectedResult = new LoginResult("kal", "a");
        var req = new UserData("kal", "storms", "roshar.com");

        insertUser(req);

        var ref = new Object() {
            LoginResult actualResult;
        };
        Assertions.assertDoesNotThrow(() -> {
            ref.actualResult = facade.login(req);
        });
        Assertions.assertEquals(ref.actualResult.username(), expectedResult.username());
    }


    @Test
    public void testLoginInvalid() throws DataAccessException {
        var inserted = new UserData("kal", "mmm", "roshar.com");
        var req = new UserData("kal", "storms", "roshar.com");
        insertUser(inserted);

        try {
            facade.login(req);
            Assertions.fail("Did not throw an error fro duplicate registration");
        } catch (ResponseException e) {
            Assertions.assertEquals(401, e.StatusCode());
        }
    }

}
