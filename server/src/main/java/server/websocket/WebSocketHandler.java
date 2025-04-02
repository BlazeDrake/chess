package server.websocket;

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
import server.Server;
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
            throw new IOException(e);
        }
        var type = msg.getCommandType();
        switch (type) {
            case ClientMessage.ClientMessageType.CONNECT ->
                    join(username, msg.getAuthToken(), msg.getGameId(), session);
            case ClientMessage.ClientMessageType.LEAVE -> leave(username, msg.getAuthToken(), msg.getGameId());
            case ClientMessage.ClientMessageType.RESIGN -> resign(username, msg.getAuthToken(), msg.getGameId());
            case ClientMessage.ClientMessageType.MAKE_MOVE ->
                    move(username, msg.getAuthToken(), msg.getGameId(), msg.getMove());
        }
    }

    private void join(String username, String authToken, int id, Session session) throws IOException {

        try {
            var gameData = games.getGame(new AuthData(authToken, username), id);
            var gameJson = gson.toJson(gameData.game());
            var updateNotif = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
            session.getRemote().sendString(gson.toJson(updateNotif));
        } catch (DataAccessException e) {
            throw new IOException(e);
        }

        //finish message
        connections.add(username, id, session);
        var message = String.format("%s connected", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, id, notification);
    }

    private void leave(String username, String authToken, int id) throws IOException {
        connections.remove(username);
        var message = String.format("%s disconnected", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
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
        var message = String.format("%s resigned. No more moves may be done on this game", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, id, notification);
        //set the game to not allow moves
    }

    private void move(String username, String authToken, int id, ChessMove move) throws IOException {
        try {
            var gameData = games.getGame(new AuthData(authToken, username), id);
            var chessGame = gameData.game();
            chessGame.makeMove(move);
            var updatedData = new GameData(id, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);
            games.updateGame(updatedData);
            var gameJson = gson.toJson(chessGame);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
            connections.broadcast(username, id, notification);
        } catch (DataAccessException | InvalidMoveException e) {
            throw new IOException(e);
        }
    }
}
