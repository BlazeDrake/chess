package service;

import data_access.DataAccessException;
import data_access.UnauthorizedException;
import data_access.interfaces.AuthDAO;
import data_access.interfaces.UserDAO;
import data_access.local_implementation.MemoryAuthDAO;
import data_access.local_implementation.MemoryUserDAO;
import data_access.local_implementation.MockDatabase;
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
