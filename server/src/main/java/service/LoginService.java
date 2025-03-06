package service;

import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;

import network.requests.LoginRequest;
import network.results.LoginResult;

public class LoginService {
    UserDAO loginDAO;
    AuthDAO authDAO;

    public LoginService(UserDAO loginDAO, AuthDAO authDAO) {
        this.loginDAO = loginDAO;
        this.authDAO = authDAO;
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        var user = loginDAO.getUser(request.username());
        if (user == null || !request.password().equals(user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        var auth = AuthDAO.generateAuth(request.username());

        authDAO.createAuth(auth);

        return new LoginResult(auth.username(), auth.authToken());
    }
}
