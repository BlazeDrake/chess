package service;

import chess.ChessGame;
import dataaccess.*;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import network.datamodels.AuthData;
import network.datamodels.GameData;
import network.datamodels.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

class ClearServiceTest {


    ClearService clearService;

    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;
    Connection connection;

    @BeforeEach
    void setup() throws DataAccessException, SQLException {
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
        userDAO = new SQLUserDAO(connection);
        gameDAO = new SQLGameDAO(connection);

        clearService = new ClearService(authDAO, gameDAO, userDAO);
    }

    @AfterEach
    void cleanup() throws SQLException {
        connection.rollback();
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