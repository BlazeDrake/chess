package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    private String message;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, String message) {

        this.serverMessageType = type;
        this.message = message;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType() && getMessage().equals(that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType(), getMessage());
    }
}
