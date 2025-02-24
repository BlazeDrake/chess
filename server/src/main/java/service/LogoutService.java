package service;

import data_access.DataAccessException;
import data_access.UnauthorizedException;
import data_access.interfaces.AuthDAO;
import data_access.local_implementation.MemoryAuthDAO;
import data_access.local_implementation.MockDatabase;
import network.requests.LogoutRequest;

public class LogoutService {
    private MockDatabase db;
    private AuthDAO authDAO;
    public LogoutService(MockDatabase db){

        this.db=db;
        authDAO = new MemoryAuthDAO(db);
    }

    public void logout(LogoutRequest request) throws DataAccessException{
        var authData = authDAO.getAuth(request.authToken());
        if(authData==null){
            throw new UnauthorizedException("Error: unauthorized");
        }

        authDAO.deleteAuth(authData);
    }
}
