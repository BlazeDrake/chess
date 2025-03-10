package service;

import com.google.gson.Gson;
import dataaccess.*;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import network.datamodels.AuthData;
import network.requests.CreateGameRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class CreateGameServiceTest {


    CreateGameService service;
    AuthData auth;

    GameDAO gameDAO;
    AuthDAO authDAO;
    Connection connection;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        connection = DatabaseManager.getConnection();


        authDAO = new SQLAuthDAO(connection);
        gameDAO = new SQLGameDAO(connection);

        service = new CreateGameService(authDAO, gameDAO);

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