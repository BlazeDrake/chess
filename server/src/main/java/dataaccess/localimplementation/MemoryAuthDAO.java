package dataaccess.localimplementation;

import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.interfaces.AuthDAO;
import network.datamodels.AuthData;

import java.util.TreeMap;

public class MemoryAuthDAO implements AuthDAO {

    MockDatabase db;

    public MemoryAuthDAO(MockDatabase db) {
        this.db = db;
    }

    @Override
    public void createAuth(AuthData data) throws DataAccessException {
        if (db == null) {
            throw new DataAccessException("Error: Database can't be accessed");
        }
        var curAuth = db.getAuthTokens();
        curAuth.put(data.authToken(), data);
        db.setAuthTokens(curAuth);
    }

    @Override
    public AuthData authenticate(String authToken) throws DataAccessException {
        if (db == null) {
            throw new DataAccessException("Error: Database can't be accessed");
        }
        var auth = db.getAuthTokens().get(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return auth;
    }

    @Override
    public void deleteAuth(AuthData data) throws DataAccessException {
        if (db == null) {
            throw new DataAccessException("Error: Database can't be accessed");
        }
        var tokens = db.getAuthTokens();
        var key = data.authToken();
        tokens.remove(key);
        db.setAuthTokens(tokens);
    }

    @Override
    public void clear() throws DataAccessException {
        if (db == null) {
            throw new DataAccessException("Error: Database can't be accessed");
        }
        db.setAuthTokens(new TreeMap<>());
    }
}
