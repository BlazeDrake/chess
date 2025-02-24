package data_access.local_implementation;

import data_access.DataAccessException;
import data_access.interfaces.GameDAO;
import network.data_models.AuthData;
import network.data_models.GameData;

import java.util.ArrayList;
import java.util.Collection;

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
        if(db==null){
            throw new DataAccessException("Error: Database can't be accessed");
        }
        return null;
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {

    }

    public void clear() throws DataAccessException{

        if(db==null){
            throw new DataAccessException("Error: Database can't be accessed");
        }
        db.setGames(new ArrayList<>());
    }
}
