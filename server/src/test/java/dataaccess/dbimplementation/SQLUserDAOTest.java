package dataaccess.dbimplementation;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import network.datamodels.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class SQLUserDAOTest {

    Connection connection;
    SQLUserDAO sqlUserDAO;

    UserData testUser = new UserData("pattern", "lies", "mmm@roshar.com");

    @BeforeEach
    void setup() throws DataAccessException, SQLException {

        DatabaseManager.createDatabase();
        connection = DatabaseManager.getConnection();

        DatabaseManager.reset();

        sqlUserDAO = new SQLUserDAO(connection);
    }

    private void addUser() throws DataAccessException {
        clearTestUser();
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, testUser.username());
            stmt.setString(2, testUser.password());
            stmt.setString(3, testUser.email());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private void clearTestUser() throws DataAccessException {
        String sql = "DELETE FROM users WHERE username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, testUser.username());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private boolean attemptFindUser(String username) throws DataAccessException {

        boolean returnVal;
        String sql = "select username, password, email from users where username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            returnVal = stmt.executeQuery().next();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return returnVal;
    }

    private int getCount() throws DataAccessException {
        int count = 0;
        String sql = "select username, password, email from users;";
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
    void getUserValid() throws DataAccessException {
        addUser();
        Assertions.assertNotNull(sqlUserDAO.getUser(testUser.username()));
    }

    @Test
    void getUserInvalid() throws DataAccessException {
        clearTestUser();
        Assertions.assertNull(sqlUserDAO.getUser(testUser.username()));
    }

    @Test
    void createUserValid() throws DataAccessException {
        clearTestUser();
        int expectedCount = getCount() + 1;
        sqlUserDAO.createUser(testUser);
        Assertions.assertEquals(expectedCount, getCount());
    }

    @Test
    void createUserInvalid() throws DataAccessException {
        addUser();
        Assertions.assertThrows(DataAccessException.class, () -> sqlUserDAO.createUser(testUser));
    }

    @Test
    void clearValid() throws DataAccessException {
        addUser();
        sqlUserDAO.clear();
        Assertions.assertEquals(0, getCount());
    }
}