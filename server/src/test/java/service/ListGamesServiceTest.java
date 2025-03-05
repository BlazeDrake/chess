package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.UnauthorizedException;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import network.datamodels.AuthData;
import network.datamodels.GameData;
import network.requests.ListGamesRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.junit.jupiter.api.Assertions;

class ListGamesServiceTest {


    ListGamesService service;
    ArrayList<GameData> gamesList;
    AuthData auth;

    GameDAO gameDAO;
    AuthDAO authDAO;

    @BeforeEach
    void setup() throws DataAccessException {
        service = new ListGamesService();
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();

        auth = new AuthData("abc123", "nightblood");
        authDAO.createAuth(auth);

        gamesList = new ArrayList<>(List.of(
                new GameData(0, "a", "b", "air", new ChessGame()),
                new GameData(1, "c", "d", "sick", new ChessGame()),
                new GameData(2, "e", "f", "low", new ChessGame())
        ));
        for (GameData gameData : gamesList) {
            gameDAO.createGame(auth, gameData.gameName());
            gameDAO.updateGame(gameData);
        }


    }

    @Test
    void listGamesValid() throws DataAccessException {
        var result = service.listGames(new ListGamesRequest(auth.authToken()));
        Assertions.assertEquals((Collection<GameData>) gamesList, result.games());
    }

    @Test
    void listGamesInvalid() {
        Assertions.assertThrows(UnauthorizedException.class, () -> service.listGames(new ListGamesRequest("Odium")));
    }
}