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
    ClientMessageType commandType;
    private String authToken;
    private int gameId;
    ChessMove move;

    public enum ClientMessageType {
        CONNECT,
        MAKE_MOVE,
        RESIGN,
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
        this.move = null;
    }

    public ClientMessage(ClientMessageType type, String authToken, int gameId, ChessMove move) {
        this.commandType = type;
        this.authToken = authToken;
        this.gameId = gameId;
        this.move = move;
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

    public ChessMove getMove() {
        return this.move;
    }
}
