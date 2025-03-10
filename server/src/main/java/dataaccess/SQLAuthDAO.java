package dataaccess;

import dataaccess.interfaces.AuthDAO;
import network.datamodels.AuthData;

import java.sql.Connection;

public class SQLAuthDAO implements AuthDAO {

    Connection connection;

    public SQLAuthDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        
    }

    @Override
    public AuthData authenticate(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
