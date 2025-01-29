package calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator implements PieceMovesCalculator{
     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves = new HashSet<ChessMove>();
        final int col = myPosition.getColumn();
        final int row = myPosition.getRow();
        var piece=board.getPiece(myPosition);

        //down and to the left
        for (int i = 1; i < Math.min(row, col); i++) {
           var testPos = new ChessPosition(row - i, col - i);

           var blockingPiece = board.getPiece(testPos);

           if (blockingPiece != null) {
              if (blockingPiece.getTeamColor() != piece.getTeamColor()) {
                 validMoves.add(new ChessMove(myPosition, testPos, null));
              }
              break;
           } else {
              validMoves.add(new ChessMove(myPosition, testPos, null));
           }
        }
        //down and to the right
        for (int i = 1; i < Math.min(row, 9 - col); i++) {
           var testPos = new ChessPosition(row - i, col + i);

           var blockingPiece = board.getPiece(testPos);

           if (blockingPiece != null) {
              if (blockingPiece.getTeamColor() != piece.getTeamColor()) {
                 validMoves.add(new ChessMove(myPosition, testPos, null));
              }
              break;
           } else {
              validMoves.add(new ChessMove(myPosition, testPos, null));
           }
        }
        //up and to the right
        for (int i = 1; i < Math.min(9 - row, 9 - col); i++) {
           var testPos = new ChessPosition(row + i, col + i);

           var blockingPiece = board.getPiece(testPos);

           if (blockingPiece != null) {
              if (blockingPiece.getTeamColor() != piece.getTeamColor()) {
                 validMoves.add(new ChessMove(myPosition, testPos, null));
              }
              break;
           } else {
              validMoves.add(new ChessMove(myPosition, testPos, null));
           }
        }
        //up and to the left
        for (int i = 1; i < Math.min(9 - row, col); i++) {
           var testPos = new ChessPosition(row + i, col - i);

           var blockingPiece = board.getPiece(testPos);

           if (blockingPiece != null) {
              if (blockingPiece.getTeamColor() != piece.getTeamColor()) {
                 validMoves.add(new ChessMove(myPosition, testPos, null));
              }
              break;
           } else {
              validMoves.add(new ChessMove(myPosition, testPos, null));
           }
        }

        return validMoves;
    }
}
