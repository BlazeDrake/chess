package network.websocket;

import chess.ChessGame;

public record ConnectInfo(int id, boolean observing, ChessGame.TeamColor color) {
}
