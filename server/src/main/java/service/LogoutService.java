package service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.localimplementation.MemoryAuthDAO;
import dataaccess.localimplementation.MockDatabase;
import network.requests.LogoutRequest;

public class LogoutService {
    private AuthDAO authDAO;

    public LogoutService(MockDatabase db) {
        authDAO = new MemoryAuthDAO(db);
    }

    public void logout(LogoutRequest request) throws DataAccessException {
        var authData = authDAO.authenticate(request.authToken());

        authDAO.deleteAuth(authData);
    }
}
