package service;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import network.datamodels.AuthData;
import network.requests.CreateGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameServiceTest {


    CreateGameService service;
    AuthData auth;

    GameDAO gameDAO;
    AuthDAO authDAO;

    @BeforeEach
    void setUp() throws DataAccessException {
        service = new CreateGameService();

        auth = new AuthData("abc123", "nightblood");
        authDAO.createAuth(auth);
    }

    @Test
    void testCreateGameValid() {
        try {
            service.createGame(new CreateGameRequest(auth.authToken(), "roshar"));
            service.createGame(new CreateGameRequest(auth.authToken(), "scadrial"));
            service.createGame(new CreateGameRequest(auth.authToken(), "sel"));
            service.createGame(new CreateGameRequest(auth.authToken(), "nalthis"));

            Assertions.assertNotNull(gameDAO.getGame(auth, 4));
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