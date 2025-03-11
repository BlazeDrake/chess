package dataaccess.dbimplementation;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLGameDAO;
import network.datamodels.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {
    Connection connection;
    SQLGameDAO gameDAO;
    Gson gson;

    GameData[] testGames = {
            new GameData(1, "wht", "blk", "test1", new ChessGame()),
            new GameData(2, "wht", "blk", "test2", new ChessGame()),
            new GameData(3, "wht", "blk", "test3", new ChessGame()),
    };

    @BeforeEach
    void setup() throws DataAccessException, SQLException {

        DatabaseManager.createDatabase();
        connection = DatabaseManager.getConnection();
        gson = new Gson();

        gameDAO = new SQLGameDAO(connection);
    }

    private void addGames() throws DataAccessException {
        clearTestGames();
        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?);";
        for (var game : testGames) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException(ex.getMessage());
            }
        }
    }

    private void clearTestGames() throws DataAccessException {
        String sql = "DELETE FROM games WHERE id = ?;";
        for (var game : testGames) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, game.gameID());
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    private boolean attemptFindGame(int id) throws DataAccessException {

        boolean returnVal;
        String sql = "select id, whiteUsername, blackUsername, gameName, chessGame from games where id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            returnVal = stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return returnVal;
    }

    private int getCount() throws DataAccessException {
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


    @Test
    void listGamesValid() throws DataAccessException {
        int expectedCount = getCount();

        clearTestGames();
        addGames();

        var gamesList = gameDAO.listGames();

        Assertions.assertEquals(expectedCount, gamesList.size());
    }

    @Test
    void listGamesInvalid() {
    }

    @Test
    void createGameValid() {
    }

    @Test
    void createGameInvalid() {
    }

    @Test
    void getGameValid() {
    }

    @Test
    void getGameInvalid() {
    }

    @Test
    void updateGameValid() {
    }

    @Test
    void updateGameInvalid() {
    }

    @Test
    void clearValid() {
    }

}