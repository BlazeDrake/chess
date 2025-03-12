package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.TakenException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;

import network.datamodels.UserData;
import network.requests.RegisterRequest;
import network.results.RegisterResult;
import org.mindrot.jbcrypt.BCrypt;

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
            String hash = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
            var hashedUser = new UserData(userData.username(), hash, userData.email());
            userDAO.createUser(hashedUser);

            var token = AuthDAO.generateAuth(userData.username());
            authDAO.createAuth(token);
            return new RegisterResult(userData.username(), token.authToken());
        }

    }
}
