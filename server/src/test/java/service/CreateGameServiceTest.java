package service;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.localimplementation.MockDatabase;
import network.datamodels.AuthData;
import network.requests.CreateGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameServiceTest {

    MockDatabase db;
    CreateGameService service;
    AuthData auth;

    @BeforeEach
    void setUp() {
        db = new MockDatabase();
        service = new CreateGameService(db);

        auth = new AuthData("abc123", "nightblood");
        var authTokens = new TreeMap<String, AuthData>();
        authTokens.put(auth.authToken(), auth);
        db.setAuthTokens(authTokens);
    }

    @Test
    void testCreateGameValid() {
        try {
            service.createGame(new CreateGameRequest(auth.authToken(), "roshar"));
            service.createGame(new CreateGameRequest(auth.authToken(), "scadrial"));
            service.createGame(new CreateGameRequest(auth.authToken(), "sel"));
            service.createGame(new CreateGameRequest(auth.authToken(), "nalthis"));

            Assertions.assertEquals(4, db.getGames().size());
        } catch (Exception e) {
            Assertions.fail("Exception: " + e);
        }


    }

    @Test
    void testCreateGameBadRequest() {
        Gson gson = new Gson();

        String nullAuthJson = "{\"gameName\":\"roshar\"}";
        String nullGameJson = "{\"authToken\":\"roshar\"}";
        var nullAuth = gson.fromJson(nullAuthJson, CreateGameRequest.class);
        var nullGame = gson.fromJson(nullGameJson, CreateGameRequest.class);

        Assertions.assertThrows(BadRequestException.class, () -> service.createGame(nullAuth));
        Assertions.assertThrows(BadRequestException.class, () -> service.createGame(nullGame));
    }
}