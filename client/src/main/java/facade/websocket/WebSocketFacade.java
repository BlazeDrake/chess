package facade.websocket;


import chess.ChessGame;
import com.google.gson.Gson;
import network.ResponseException;
import network.websocket.ConnectInfo;
import websocket.messages.ClientMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    Gson gson;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            gson = new Gson();

            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String username, int id, ChessGame.TeamColor color) throws ResponseException {
        try {
            ConnectInfo connectInfo;
            if (color == null) {
                connectInfo = new ConnectInfo(id, true, null);
            }
            else {
                connectInfo = new ConnectInfo(id, false, color);
            }
            var msg = new ClientMessage(ClientMessage.ClientMessageType.CONNECT, username, gson.toJson(connectInfo));
            this.session.getBasicRemote().sendText(gson.toJson(msg));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


}