package service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;

import network.requests.LogoutRequest;

public class LogoutService {
    private AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(LogoutRequest request) throws DataAccessException {
        var authData = authDAO.authenticate(request.authToken());

        authDAO.deleteAuth(authData);
    }
}
