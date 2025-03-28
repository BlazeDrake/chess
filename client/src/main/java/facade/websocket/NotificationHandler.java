package facade.websocket;

import websocket.messages.ServerMessage;

public class NotificationHandler {
    public void notify(ServerMessage notification) {
        //more complex handling here
        System.out.println(notification.getMessage());
    }
}