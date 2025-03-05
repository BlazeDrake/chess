package service;

import chess.ChessGame;
import dataaccess.*;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import network.datamodels.AuthData;
import network.datamodels.GameData;
import network.datamodels.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

class ClearServiceTest {


    ClearService clearService;

    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;

    @BeforeEach
    void setup() {
        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();

        clearService = new ClearService();
    }


    @Test
    void clearValidNonempty() throws DataAccessException {
        var auth = new AuthData("eeeeeee", "testUser");
        authDAO.createAuth(auth);

        gameDAO.createGame(auth, "test");

        userDAO.createUser(new UserData("user", "e", "hi@email.com"));

        clearService.clear();

        Assertions.assertThrows(UnauthorizedException.class, () -> authDAO.authenticate(auth.authToken()));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.getGame(auth, 0));
        Assertions.assertNull(userDAO.getUser("user"));
    }
}