package Service;

import dataaccess.DataAccessException;
import dataaccess.TakenException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.localImplementation.MemoryAuthDAO;
import dataaccess.localImplementation.MemoryUserDAO;
import dataaccess.localImplementation.MockDatabase;
import network.requests.RegisterRequest;
import network.results.RegisterResult;

public class RegisterService {
    MockDatabase db;
    UserDAO userDAO;
    AuthDAO authDAO;
    public RegisterService(MockDatabase db){
        this.db=db;
        userDAO = new MemoryUserDAO(db);
        authDAO = new MemoryAuthDAO(db);
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        var userData=request.userData();
        if(userDAO.getUser(userData.username())!=null){
            throw new TakenException("Error: already taken");
        }
        else{
            userDAO.createUser(userData);

            var token=AuthDAO.generateAuth(userData.username());
            authDAO.createAuth(token);
            return new RegisterResult(userData.username(), token.authToken());
        }

    }
}
