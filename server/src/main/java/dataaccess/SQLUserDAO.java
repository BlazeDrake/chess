package dataaccess;

import dataaccess.interfaces.UserDAO;
import network.datamodels.UserData;

import java.sql.Connection;

public class SQLUserDAO implements UserDAO {

    Connection connection;

    public SQLUserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
