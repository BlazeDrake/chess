package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.interfaces.*;
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

    public WebSocketHandler(AuthDAO auth) {
        this.auth = auth;
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
            case ClientMessage.ClientMessageType.LEAVE -> {//implement
            }
            case ClientMessage.ClientMessageType.MAKE_MOVE -> {//implement
            }
        }
    }

    private void join(String username, int id, Session session) throws IOException {
        //finish message
        connections.add(username, id, session);
        var message = String.format("%s connected", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast("null"/*username*/, id, notification);
    }
}
