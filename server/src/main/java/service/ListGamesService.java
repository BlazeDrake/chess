package service;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.UnauthorizedException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.localimplementation.MemoryAuthDAO;
import dataaccess.localimplementation.MemoryGameDAO;

import network.requests.ListGamesRequest;
import network.results.ListGamesResult;

public class ListGamesService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public ListGamesService() {
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        var auth = authDAO.authenticate(request.authToken());
        return new ListGamesResult(gameDAO.listGames(auth.authToken()));
    }
}
