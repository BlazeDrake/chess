package dataaccess.localImplementation;

import dataaccess.DataAccessException;
import dataaccess.interfaces.GameDAO;
import network.dataModels.AuthData;
import network.dataModels.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    MockDatabase db;
    public MemoryGameDAO(MockDatabase db){
        this.db=db;
    }
    @Override
    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        return db.getGames();
    }

    @Override
    public void createGame(AuthData auth, String gameName) throws DataAccessException {

    }

    @Override
    public GameData getGame(AuthData data, int id) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {

    }

    public void clear() throws DataAccessException{

        if(db==null){
            throw new DataAccessException("Database can't be accessed");
        }
        db.setGames(new ArrayList<>());
    }
}
