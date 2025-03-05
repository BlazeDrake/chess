package service;

import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.TakenException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.localimplementation.MemoryAuthDAO;
import dataaccess.localimplementation.MemoryGameDAO;

import network.datamodels.GameData;
import network.requests.JoinGameRequest;

import java.util.HashSet;
import java.util.List;

public class JoinGameService {
    final HashSet<String> validTeams = new HashSet<>(List.of(
            "WHITE",
            "BLACK"
    ));

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public JoinGameService() {
        authDAO = new MemoryAuthDAO(db);
        gameDAO = new MemoryGameDAO(db);
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException {
        var team = request.playerColor();
        if (request.authToken() == null || team == null || request.gameID() == 0) {
            throw new BadRequestException("Error: bad request");
        }

        team = team.toUpperCase();
        if (!validTeams.contains(team)) {
            throw new BadRequestException("Error: bad request");
        }

        var auth = authDAO.authenticate(request.authToken());

        var oldGame = gameDAO.getGame(auth, request.gameID());
        var blkUser = oldGame.blackUsername();
        var whtUser = oldGame.whiteUsername();
        GameData newGame;

        if ("WHITE".equals(team)) {
            if (whtUser != null) {
                throw new TakenException("Error: already taken");
            }
            newGame = new GameData(oldGame.gameID(), auth.username(), blkUser, oldGame.gameName(), oldGame.game());
        }
        else {
            if (blkUser != null) {
                throw new TakenException("Error: already taken");
            }
            newGame = new GameData(oldGame.gameID(), whtUser, auth.username(), oldGame.gameName(), oldGame.game());
        }

        gameDAO.updateGame(newGame);
    }
}
