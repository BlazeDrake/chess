package piece_calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator{
     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves= new HashSet<ChessMove>();
        final int col= myPosition.getColumn();
        final int row= myPosition.getRow();
        var piece=board.getPiece(myPosition);

        //Check moves one column at a time
        for(int i=-1;i<=1;i++){
           tryAddGenericMove(board,new ChessMove(myPosition, new ChessPosition(row+1,col+i),null),validMoves,piece);
           tryAddGenericMove(board,new ChessMove(myPosition, new ChessPosition(row,col+i),null),validMoves,piece);
           tryAddGenericMove(board,new ChessMove(myPosition, new ChessPosition(row-1,col+i),null),validMoves,piece);
        }


        return validMoves;
    }
}
