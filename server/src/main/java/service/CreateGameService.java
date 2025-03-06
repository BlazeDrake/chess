package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;

import network.requests.CreateGameRequest;
import network.results.CreateGameResult;

public class CreateGameService {
    AuthDAO authDAO;
    GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {
        if (request == null || request.authToken() == null || request.gameName() == null) {
            throw new BadRequestException("Error: bad request");
        }

        var auth = authDAO.authenticate(request.authToken());
        int id = gameDAO.createGame(auth, request.gameName());
        return new CreateGameResult(id);
    }
}
