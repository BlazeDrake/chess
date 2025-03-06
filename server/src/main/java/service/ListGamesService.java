package service;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.UnauthorizedException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;

import network.requests.ListGamesRequest;
import network.results.ListGamesResult;

public class ListGamesService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {
        var auth = authDAO.authenticate(request.authToken());
        return new ListGamesResult(gameDAO.listGames(auth.authToken()));
    }
}
