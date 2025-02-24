package service;

import data_access.DataAccessException;
import data_access.UnauthorizedException;
import data_access.interfaces.AuthDAO;
import data_access.interfaces.GameDAO;
import data_access.local_implementation.MemoryAuthDAO;
import data_access.local_implementation.MemoryGameDAO;
import data_access.local_implementation.MockDatabase;
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
