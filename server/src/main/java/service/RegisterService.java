package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.TakenException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.localimplementation.MemoryAuthDAO;
import dataaccess.localimplementation.MemoryUserDAO;
import dataaccess.localimplementation.MockDatabase;
import network.requests.RegisterRequest;
import network.results.RegisterResult;

public class RegisterService {
    UserDAO userDAO;
    AuthDAO authDAO;

    public RegisterService(MockDatabase db) {
        userDAO = new MemoryUserDAO(db);
        authDAO = new MemoryAuthDAO(db);
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
