package dataaccess.localimplementation;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.interfaces.GameDAO;
import network.datamodels.AuthData;
import network.datamodels.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    MockDatabase db;

    public MemoryGameDAO(MockDatabase db) {
        this.db = db;
    }

    @Override
    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        return db.getGames();
    }

    @Override
    public int createGame(AuthData auth, String gameName) throws DataAccessException {
        var games = db.getGames();
        int id = games.size() + 1;
        games.add(new GameData(id, null, null, gameName, new ChessGame()));
        db.setGames(games);
        return id;
    }

    @Override
    public GameData getGame(AuthData data, int id) throws DataAccessException {
        if (db == null) {
            throw new DataAccessException("Error: Database can't be accessed");
        }
        var games = db.getGames();

        if (id > games.size() || id <= 0) {
            throw new DataAccessException("Error: Game with ID " + id + " doesn't exist");
        }
        return games.get(id - 1);
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {
        var games = db.getGames();
        var id = data.gameID();

        if (id > games.size() || id <= 0) {
            throw new DataAccessException("Error: Game with ID " + id + " doesn't exist");
        }
        games.set(id - 1, data);
        db.setGames(games);
    }

    public void clear() throws DataAccessException {

        if (db == null) {
            throw new DataAccessException("Error: Database can't be accessed");
        }
        db.setGames(new ArrayList<>());
    }
}
