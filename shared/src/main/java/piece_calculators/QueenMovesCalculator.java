package piece_calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator{
     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves = new RookMovesCalculator().pieceMoves(board, myPosition);
        validMoves.addAll(new BishopMovesCalculator().pieceMoves(board, myPosition));
        return validMoves;
    }
}
