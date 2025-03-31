package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ClientMessage {
    ClientMessageType commandType;
    private String authToken;
    private int gameId;

    public enum ClientMessageType {
        CONNECT,
        MAKE_MOVE,
        LEAVE;
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
        return commandType == that.commandType && Objects.equals(authToken, that.authToken) && Objects.equals(gameId, that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandType, authToken, gameId);
    }

    public ClientMessage(ClientMessageType type, String authToken, int gameId) {
        this.commandType = type;
        this.authToken = authToken;
        this.gameId = gameId;
    }

    public ClientMessageType getCommandType() {
        return this.commandType;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public int getGameId() {
        return this.gameId;
    }
}
