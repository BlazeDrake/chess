package Service;

import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.localImplementation.MemoryAuthDAO;
import dataaccess.localImplementation.MockDatabase;
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
