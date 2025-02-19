package dataaccess.interfaces;

import dataaccess.DataAccessException;
import network.dataModels.AuthData;
import network.dataModels.GameData;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> listGames(String authToken) throws DataAccessException;
    void createGame(AuthData auth, String gameName) throws DataAccessException;
    GameData getGame(AuthData data,int id)throws DataAccessException;
    void updateGame(GameData data) throws DataAccessException;
    void clear() throws DataAccessException;
}
