package piece_calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator{
     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves= new HashSet<ChessMove>();
        final int col= myPosition.getColumn();
        final int row= myPosition.getRow();
        var piece = board.getPiece(myPosition);

        tryAddGenericMove(board,new ChessMove(myPosition,new ChessPosition(row+2,col+1),null),validMoves,piece);
        tryAddGenericMove(board,new ChessMove(myPosition,new ChessPosition(row-2,col+1),null),validMoves,piece);
        tryAddGenericMove(board,new ChessMove(myPosition,new ChessPosition(row+2,col-1),null),validMoves,piece);
        tryAddGenericMove(board,new ChessMove(myPosition,new ChessPosition(row-2,col-1),null),validMoves,piece);
        tryAddGenericMove(board,new ChessMove(myPosition,new ChessPosition(row+1,col+2),null),validMoves,piece);
        tryAddGenericMove(board,new ChessMove(myPosition,new ChessPosition(row-1,col+2),null),validMoves,piece);
        tryAddGenericMove(board,new ChessMove(myPosition,new ChessPosition(row+1,col-2),null),validMoves,piece);
        tryAddGenericMove(board,new ChessMove(myPosition,new ChessPosition(row-1,col-2),null),validMoves,piece);

        return validMoves;
    }
}
