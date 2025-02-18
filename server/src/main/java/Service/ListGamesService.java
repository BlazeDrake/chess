package Service;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.localImplementation.MemoryGameDAO;
import dataaccess.localImplementation.MockDatabase;
import network.results.ListGamesResult;

public class ListGamesService {
    MockDatabase db;
    AuthDAO authDAO;
    GameDAO gameDAO;
    public ListGamesService(MockDatabase db){
        this.db=db;
        gameDAO = new MemoryGameDAO(db);
    }

    /*public ListGamesResult listGames(String authToken){
        var authData =
    }*/
}
