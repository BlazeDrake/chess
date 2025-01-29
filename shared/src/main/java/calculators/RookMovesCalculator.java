package calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator implements PieceMovesCalculator{
     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves = new HashSet<ChessMove>();
        final int col = myPosition.getColumn();
        final int row = myPosition.getRow();
        var piece=board.getPiece(myPosition);
        //Get all valid, unblocked positions in current column
        int i;
        for (i = row + 1; i <= 8; i++) {
           var testPos = new ChessPosition(i, col);

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
        for (int j = row - 1; j >= 1; j--) {
           var testPos = new ChessPosition(j, col);

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

        //Get all valid, unblocked positions in current row
        for (int k = col + 1; k <= 8; k++) {
           var testPos = new ChessPosition(row, k);

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
        for (int l = col - 1; l >= 1; l--) {
           var testPos = new ChessPosition(row, l);

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
