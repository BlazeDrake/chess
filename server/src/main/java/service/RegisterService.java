package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.TakenException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;

import network.requests.RegisterRequest;
import network.results.RegisterResult;

public class RegisterService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        var userData = request.userData();
        if (userData == null || userData.email() == null || userData.password() == null || userData.username() == null) {
            throw new BadRequestException("Error: bad request");
        }

        if (userDAO.getUser(userData.username()) != null) {
            throw new TakenException("Error: already taken");
        }
        else {
            userDAO.createUser(userData);

            var token = AuthDAO.generateAuth(userData.username());
            authDAO.createAuth(token);
            return new RegisterResult(userData.username(), token.authToken());
        }

    }
}
