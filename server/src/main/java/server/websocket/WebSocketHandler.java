package server.websocket;

import com.google.gson.Gson;
import dataaccess.interfaces.*;
import network.ResponseException;
import jdk.jshell.spi.ExecutionControl;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ClientMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        ClientMessage msg = new Gson().fromJson(message, ClientMessage.class);
        switch (msg.getClientMessageType()) {
            case CONNECT -> join(msg.getUsername(), session);
            case LEAVE -> {//implement
            }
            case MAKE_MOVE -> {//implement
            }
        }
    }

    private void join(String username, Session session) throws IOException {
        //finish message
        connections.add(username, session);
        var message = String.format("%s connected", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, notification);
    }
}
