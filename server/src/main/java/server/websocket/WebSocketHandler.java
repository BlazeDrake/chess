package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.interfaces.*;
import network.datamodels.AuthData;
import network.datamodels.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ClientMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private AuthDAO auth;
    private GameDAO games;
    private Gson gson;

    public WebSocketHandler(AuthDAO auth, GameDAO games) {
        this.auth = auth;
        this.games = games;
        this.gson = new Gson();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        ClientMessage msg = gson.fromJson(message, ClientMessage.class);
        String username;
        try {
            username = auth.authenticate(msg.getAuthToken()).username();
        } catch (DataAccessException e) {
            sendError(session);
            return;
        }
        var type = msg.getCommandType();
        try {
            switch (type) {
                case ClientMessage.ClientMessageType.CONNECT ->
                        join(username, msg.getAuthToken(), msg.getGameID(), session);
                case ClientMessage.ClientMessageType.LEAVE -> leave(username, msg.getAuthToken(), msg.getGameID());
                case ClientMessage.ClientMessageType.RESIGN -> resign(username, msg.getAuthToken(), msg.getGameID());
                case ClientMessage.ClientMessageType.MAKE_MOVE ->
                        move(username, msg.getAuthToken(), msg.getGameID(), msg.getMove(), session);
            }
        } catch (IOException e) {
            sendError(session);
        }
    }

    private void join(String username, String authToken, int id, Session session) throws IOException {

        try {
            var gameData = games.getGame(new AuthData(authToken, username), id);
            var updateNotif = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, gameData.game());
            session.getRemote().sendString(gson.toJson(updateNotif));
        } catch (DataAccessException e) {
            throw new IOException(e);
        }

        //finish message
        connections.add(username, id, session);
        var message = String.format("%s connected", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null);
        connections.broadcast(username, id, notification);

        //var response = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Connected");
        //session.getRemote().sendString(gson.toJson(response));
    }

    private void leave(String username, String authToken, int id) throws IOException {
        connections.remove(username);
        var message = String.format("%s disconnected", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null);
        connections.broadcast(username, id, notification);

        //update db
        try {
            var game = games.getGame(new AuthData(authToken, username), id);
            if (username.equals(game.whiteUsername())) {
                games.updateGame(new GameData(id, null, game.blackUsername(), game.gameName(), game.game()));
            }
            else if (username.equals(game.blackUsername())) {
                games.updateGame(new GameData(id, game.whiteUsername(), null, game.gameName(), game.game()));
            }

            if (connections.getGameConnections(id).isEmpty()) {
                games.deleteGame(id);
            }
        } catch (DataAccessException e) {
            throw new IOException(e);
        }
    }

    private void resign(String username, String authToken, int id) throws IOException {
        //set the game to not allow moves
        try {
            var gameData = games.getGame(new AuthData(authToken, username), id);
            var chessGame = gameData.game();
            if (username.equals(gameData.whiteUsername())) {
                chessGame.setCurState(ChessGame.GameState.BLACK_WIN);
            }
            else {
                chessGame.setCurState(ChessGame.GameState.WHITE_WIN);
            }
            var updatedData = new GameData(id, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);
            games.updateGame(updatedData);
        } catch (DataAccessException e) {
            throw new IOException(e);
        }
        var message = String.format("%s resigned. No more moves may be done on this game", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, null);
        connections.broadcast(null, id, notification);
    }

    private void move(String username, String authToken, int id, ChessMove move, Session session) throws IOException {
        String errorMsg = null;
        try {
            var gameData = games.getGame(new AuthData(authToken, username), id);
            var chessGame = gameData.game();

            if (chessGame.getCurState() != ChessGame.GameState.IN_PROGRESS) {
                errorMsg = "Error: Game is already finished";
            }
            else if (chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK && username.equals(gameData.blackUsername()) ||
                    chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE && username.equals(gameData.whiteUsername())) {
                chessGame.makeMove(move);
                var updatedData = new GameData(id, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);
                updateGame(id, updatedData, chessGame);
            }
            else {
                errorMsg = "Error: It is either not your turn or you are an observer";
            }
        } catch (DataAccessException | InvalidMoveException e) {
            errorMsg = "Error: invalid move";
        }

        if (errorMsg != null) {
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMsg, null);
            session.getRemote().sendString(gson.toJson(error));
        }
    }

    private void updateGame(int id, GameData updatedData, ChessGame chessGame) throws DataAccessException, IOException {
        games.updateGame(updatedData);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, chessGame);
        connections.broadcast(null, id, notification);
    }

    private void sendError(Session session) throws IOException {
        var err = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Internal error; try again", null);
        session.getRemote().sendString(gson.toJson(err));
    }
}
