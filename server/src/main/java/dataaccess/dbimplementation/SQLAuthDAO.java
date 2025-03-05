package dataaccess.dbimplementation;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import network.datamodels.AuthData;

public class SQLAuthDAO implements AuthDAO {
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
