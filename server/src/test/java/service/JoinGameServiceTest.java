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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest {


    JoinGameService service;

    ArrayList<GameData> gamesList;
    AuthData auth;
    AuthDAO authDAO;
    GameDAO gameDAO;

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

        authDAO = new SQLAuthDAO(connection);
        gameDAO = new SQLGameDAO(connection);

        service = new JoinGameService(authDAO, gameDAO);

        gamesList = new ArrayList<>(List.of(
                new GameData(1, null, "syl", "bridge4", new ChessGame())
        ));

        auth = new AuthData("storms123", "kaladin");
        authDAO.createAuth(auth);

    }

    @AfterEach
    void cleanup() throws SQLException {
        connection.rollback();
    }

    @Test
    void testJoinGameValid() {
        try {
            var request = new JoinGameRequest(auth.authToken(), "WhItE", 1);
            service.joinGame(request);
            var joinedGame = gameDAO.getGame(auth, 0);
            assertEquals(auth.username(), joinedGame.whiteUsername());
        } catch (Exception e) {
            Assertions.fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    void testJoinGameTaken() {
        var request = new JoinGameRequest(auth.authToken(), "BLACK", 1);
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