package client;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import network.datamodels.AuthData;
import network.datamodels.GameData;
import network.datamodels.UserData;
import network.requests.CreateGameRequest;
import network.requests.ListGamesRequest;
import network.results.LoginResult;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import java.sql.*;


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

    private static void insertAuth(AuthData authData) throws DataAccessException {
        String sql = "INSERT INTO auth (authToken, username) VALUES (?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, authData.authToken());
            stmt.setString(2, authData.username());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private static void insertGame(GameData game) throws DataAccessException {
        var gson = new Gson();
        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, game.whiteUsername());
            stmt.setString(2, game.blackUsername());
            stmt.setString(3, game.gameName());
            stmt.setString(4, gson.toJson(game.game()));
            if (stmt.executeUpdate() == 1) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    int id = generatedKeys.getInt(1); // ID of the inserted book
                    game = new GameData(id, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private int getGameCount() throws DataAccessException {
        int count = 0;
        String sql = "select id, whiteUsername, blackUsername, gameName, chessGame from games;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            var res = stmt.executeQuery();
            while (res.next()) {
                count++;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return count;
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
            Assertions.fail("Did not throw an unauthorized error");
        } catch (ResponseException e) {
            Assertions.assertEquals(401, e.StatusCode());
        }
    }

    @Test
    public void testListValid() throws DataAccessException, ResponseException {
        var auth = new AuthData("abc123", "heraldOfWind");
        var game = new GameData(1, "szeth", "nightblood", "syl", new ChessGame());
        insertAuth(auth);
        insertGame(game);

        var res = facade.listGames(new ListGamesRequest(auth.authToken()));
        Assertions.assertEquals(1, res.games().size());
        Assertions.assertTrue(res.games().contains(game));
    }

    @Test
    public void testListInvalid() throws DataAccessException {
        try {
            facade.listGames(new ListGamesRequest("e"));
            Assertions.fail("Did not throw an unauthorized error");
        } catch (ResponseException e) {
            Assertions.assertEquals(401, e.StatusCode());
        }
    }

    @Test
    public void createGameValid() throws DataAccessException, ResponseException {
        var auth = new AuthData("abc123", "heraldOfWind");
        insertAuth(auth);
        facade.createGame(new CreateGameRequest(auth.authToken(), "test"));
        Assertions.assertEquals(1, getGameCount());
    }

    @Test
    public void creategameInvalid() {
        try {
            facade.createGame(new CreateGameRequest("e", "test"));
            Assertions.fail("Did not throw an unauthorized error");
        } catch (ResponseException e) {
            Assertions.assertEquals(401, e.StatusCode());
        }
    }

}
