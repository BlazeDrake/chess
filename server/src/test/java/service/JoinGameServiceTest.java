package service;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.TakenException;
import dataaccess.localimplementation.MockDatabase;
import network.datamodels.AuthData;
import network.datamodels.GameData;
import network.requests.CreateGameRequest;
import network.requests.JoinGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class JoinGameServiceTest {

    MockDatabase db;
    JoinGameService service;

    ArrayList<GameData> gamesList;
    AuthData auth;

    @BeforeEach
    void setUp() {
        db = new MockDatabase();
        service = new JoinGameService(db);

        gamesList = new ArrayList<>(List.of(
                new GameData(1, null, "syl", "bridge4", new ChessGame())
        ));
        db.setGames(gamesList);

        auth = new AuthData("storms123", "kaladin");
        var authTokens = new TreeMap<String, AuthData>();
        authTokens.put(auth.authToken(), auth);
        db.setAuthTokens(authTokens);
    }

    @Test
    void testJoinGameValid() {
        try {
            var request = new JoinGameRequest(auth.authToken(), "WhItE", 1);
            service.joinGame(request);
            var joinedGame = db.getGames().get(0);
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