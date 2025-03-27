package websocket.messages;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ClientMessage {
    ClientMessageType clientMessageType;
    private String username;
    private String jsonData;

    public enum ClientMessageType {
        CONNECT,
        MAKE_MOVE,
        LEAVE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientMessage that = (ClientMessage) o;
        return clientMessageType == that.clientMessageType && Objects.equals(username, that.username) && Objects.equals(jsonData, that.jsonData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientMessageType, username, jsonData);
    }

    public ClientMessage(ClientMessageType type, String username, String jsonData) {
        this.clientMessageType = type;
        this.username = username;
        this.jsonData = jsonData;
    }

    public ClientMessageType getClientMessageType() {
        return this.clientMessageType;
    }

    public String getUsername() {
        return this.username;
    }

    public String getJsonData() {
        return this.jsonData;
    }
}
