package Service;

import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.localImplementation.MemoryAuthDAO;
import dataaccess.localImplementation.MemoryUserDAO;
import dataaccess.localImplementation.MockDatabase;
import network.dataModels.AuthData;
import network.requests.LoginRequest;
import network.results.LoginResult;

public class LoginService {
    MockDatabase db;
    UserDAO loginDAO;
    AuthDAO authDAO;
    public LoginService(MockDatabase db){

        this.db=db;
        loginDAO = new MemoryUserDAO(db);
        authDAO = new MemoryAuthDAO(db);
    }
    public LoginResult login(LoginRequest request) throws DataAccessException {
        var user = loginDAO.getUser(request.username());
        if(user ==null || !request.password().equals(user.password())){
            throw new UnauthorizedException("Error: unauthorized");
        }

        var auth = AuthDAO.generateAuth(request.username());

        var authTokens=db.getAuthTokens();
        authTokens.put(auth.authToken(),auth);
        db.setAuthTokens(authTokens);

        return new LoginResult(auth.username(),auth.authToken());
    }
}
