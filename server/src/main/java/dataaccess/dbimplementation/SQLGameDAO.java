package dataaccess.dbimplementation;

import dataaccess.DataAccessException;
import dataaccess.interfaces.GameDAO;
import network.datamodels.AuthData;
import network.datamodels.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        return List.of();
    }

    @Override
    public int createGame(AuthData auth, String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(AuthData data, int id) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
