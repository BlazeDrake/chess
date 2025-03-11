package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import network.datamodels.AuthData;
import network.datamodels.GameData;
import network.requests.CreateGameRequest;
import network.requests.JoinGameRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest {


    JoinGameService service;

    int testID;
    AuthData auth;
    AuthDAO authDAO;
    GameDAO gameDAO;

    Connection connection;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        connection = DatabaseManager.getConnection();

        DatabaseManager.reset();

        authDAO = new SQLAuthDAO(connection);
        gameDAO = new SQLGameDAO(connection);

        service = new JoinGameService(authDAO, gameDAO);

        Gson gson = new Gson();

        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, null);
            stmt.setString(2, "syl");
            stmt.setString(3, "bridge4");
            stmt.setString(4, gson.toJson(new ChessGame()));
            if (stmt.executeUpdate() == 1) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    generatedKeys.next();
                    testID = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }

        auth = new AuthData("storms123", "kaladin");
        authDAO.createAuth(auth);

    }


    @Test
    void testJoinGameValid() {
        try {
            var request = new JoinGameRequest(auth.authToken(), "WhItE", 1);
            service.joinGame(request);
            var joinedGame = gameDAO.getGame(auth, testID);
            assertEquals(auth.username(), joinedGame.whiteUsername());
        } catch (Exception e) {
            Assertions.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    void testJoinGameTaken() {
        var request = new JoinGameRequest(auth.authToken(), "BLACK", testID);
        Assertions.assertThrows(TakenException.class, () -> service.joinGame(request));
    }

    @Test
    void testJoinGameBadRequest() {
        Gson gson = new Gson();
        String nullAuthJson = "{\"playerColor\":\"WHITE\",\"gameID\":1}";
        String nullColorJson = "{\"authToken\":\"storms123\",\"gameID\":1}";

        var nullAuth = gson.fromJson(nullAuthJson, JoinGameRequest.class);
        var nullColor = gson.fromJson(nullColorJson, JoinGameRequest.class);

        Assertions.assertThrows(BadRequestException.class, () -> service.joinGame(nullAuth));
        Assertions.assertThrows(BadRequestException.class, () -> service.joinGame(nullColor));
    }

    @Test
    void testJoinNonexistentGame() {
        var request = new JoinGameRequest(auth.authToken(), "BLACK", 1);
        Assertions.assertThrows(DataAccessException.class, () -> service.joinGame(request));
    }
}