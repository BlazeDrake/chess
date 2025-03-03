package service;

import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.localimplementation.MemoryAuthDAO;
import dataaccess.localimplementation.MemoryUserDAO;
import dataaccess.localimplementation.MockDatabase;
import network.requests.LoginRequest;
import network.results.LoginResult;

public class LoginService {
    UserDAO loginDAO;
    AuthDAO authDAO;

    public LoginService(MockDatabase db) {
        loginDAO = new MemoryUserDAO(db);
        authDAO = new MemoryAuthDAO(db);
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
