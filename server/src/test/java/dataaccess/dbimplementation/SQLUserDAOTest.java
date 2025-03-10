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


        sqlUserDAO = new SQLUserDAO(connection);
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