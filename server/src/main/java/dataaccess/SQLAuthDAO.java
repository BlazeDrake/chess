package dataaccess;

import dataaccess.interfaces.AuthDAO;
import network.datamodels.AuthData;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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
