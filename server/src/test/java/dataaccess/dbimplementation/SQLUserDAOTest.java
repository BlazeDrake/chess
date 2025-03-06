package dataaccess.dbimplementation;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class SQLUserDAOTest {

    Connection connection;
    SQLUserDAO sqlUserDAO;

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

        sqlUserDAO = new SQLUserDAO(connection);
    }

    @AfterEach
    void cleanup() throws SQLException {
        connection.rollback();
    }

    @Test
    void getUserValid() {
    }

    @Test
    void getUserInvalid() {
    }

    @Test
    void createUserValid() {
    }

    @Test
    void createUserInvalid() {
    }

    @Test
    void clearValid() {
    }
}