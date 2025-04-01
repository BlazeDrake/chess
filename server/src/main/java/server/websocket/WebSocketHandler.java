package server.websocket;

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

    public WebSocketHandler(AuthDAO auth, GameDAO games) {
        this.auth = auth;
        this.games = games;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        ClientMessage msg = new Gson().fromJson(message, ClientMessage.class);
        String username;
        try {
            username = auth.authenticate(msg.getAuthToken()).username();
        } catch (DataAccessException e) {
            throw new IOException(e);
        }
        var type = msg.getCommandType();
        switch (type) {
            case ClientMessage.ClientMessageType.CONNECT -> join(username, msg.getGameId(), session);
            case ClientMessage.ClientMessageType.LEAVE -> leave(username, msg.getAuthToken(), msg.getGameId(), session);
            case ClientMessage.ClientMessageType.RESIGN -> resign(username, msg.getGameId(), session);
            case ClientMessage.ClientMessageType.MAKE_MOVE -> {//implement
            }
        }
    }

    private void join(String username, int id, Session session) throws IOException {
        //finish message
        connections.add(username, id, session);
        var message = String.format("%s connected", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, id, notification);
    }

    private void leave(String username, String authToken, int id, Session session) throws IOException {
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

    private void resign(String username, int id, Session session) throws IOException {
        var message = String.format("%s resigned. No more moves may be done on this game", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, id, notification);

        //set the game to not allow moves
    }
}
