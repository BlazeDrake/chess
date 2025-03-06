package dataaccess.dbimplementation;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLAuthDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    Connection connection;
    SQLAuthDAO authDAO;

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
    }

    @AfterEach
    void cleanup() throws SQLException {
        connection.rollback();
    }

    @Test
    void createAuthValid() {
    }

    @Test
    void createAuthInvalid() {
    }

    @Test
    void authenticateValid() {
    }

    @Test
    void authenticateInvalid() {
    }

    @Test
    void deleteAuthValid() {
    }

    @Test
    void deleteAuthInvalid() {
    }

    @Test
    void clearValid() {
    }

}