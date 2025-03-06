package dataaccess.dbimplementation;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLGameDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {
    Connection connection;
    SQLGameDAO gameDAO;

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

        gameDAO = new SQLGameDAO(connection);
    }

    @AfterEach
    void cleanup() throws SQLException {
        connection.rollback();
    }

    @Test
    void listGamesValid() {
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