package dataaccess.interfaces;

import dataaccess.DataAccessException;
import network.dataModels.AuthData;


public interface AuthDAO {
    void createAuth(AuthData data) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(AuthData data) throws DataAccessException;
    void clear() throws DataAccessException;
}
