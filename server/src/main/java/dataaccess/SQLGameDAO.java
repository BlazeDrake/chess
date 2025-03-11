package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.interfaces.GameDAO;
import network.datamodels.AuthData;
import network.datamodels.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    Connection connection;

    String tableVal;

    Gson gson;

    public SQLGameDAO(Connection connection) {
        this.connection = connection;
        gson = new Gson();

        tableVal = DatabaseManager.TABLES[DatabaseManager.TableName.Games.ordinal()];
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();

        String sql = "select id, whiteUsername, blackUsername, gameName, chessGame from games;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            var res = stmt.executeQuery();
            while (res.next()) {
                int id = res.getInt(1);
                String whiteUsername = res.getString(2);
                String blackUsername = res.getString(3);
                String gameName = res.getString(4);
                ChessGame game = gson.fromJson(res.getString(5), ChessGame.class);
                games.add(new GameData(id, whiteUsername, blackUsername, gameName, game));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        return games;
    }

    @Override
    public int createGame(AuthData auth, String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(AuthData data, int id) throws DataAccessException {
        GameData gameData = null;

        String sql = "select whiteUsername, blackUsername, gameName, chessGame from " + tableVal + " where id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            var res = stmt.executeQuery();
            if (res.next()) {
                String whiteUsername = res.getString(1);
                String blackUsername = res.getString(2);
                String gameName = res.getString(3);
                ChessGame game = gson.fromJson(res.getString(4), ChessGame.class);
                gameData = new GameData(id, whiteUsername, blackUsername, gameName, game);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        if (gameData == null) {
            throw new DataAccessException("Error: Game with ID " + id + " doesn't exist");
        }
        return gameData;
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "truncate table " + tableVal + ";";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
