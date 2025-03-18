import chess.*;
import server.Server;
import server.ServerFacade;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        var server = new Server();
        server.run(8080);
        var facade = new ServerFacade("http://localhost:8080");
    }
}