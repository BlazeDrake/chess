package Service;

import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.localImplementation.MemoryAuthDAO;
import dataaccess.localImplementation.MemoryGameDAO;
import dataaccess.localImplementation.MockDatabase;
import network.dataModels.AuthData;
import network.requests.ListGamesRequest;
import network.results.ListGamesResult;

public class ListGamesService {
    MockDatabase db;
    AuthDAO authDAO;
    GameDAO gameDAO;
    public ListGamesService(MockDatabase db){
        this.db=db;
        gameDAO = new MemoryGameDAO(db);
        authDAO = new MemoryAuthDAO(db);
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        var auth=authDAO.getAuth(request.authToken());
        if(auth==null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        return new ListGamesResult(gameDAO.listGames(auth.authToken()));
    }
}
