package piece_calculators;

import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessBoard;
import chess.ChessPiece;
import java.util.Collection;

public interface PieceMovesCalculator {
    /**
     *
     * @param board the chess board
     * @param position the position to do the move from
     * @return a set of chess moves that are viable
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    /**
     * Testing a move for the knight and king
     * @param board chessboard used
     * @param move the move to test
     * @param moveSet the collection to add the move to if successful
     * @param movePiece the piece that is moved
     * @return true if successful, false if not
     */
    default boolean tryAddGenericMove(ChessBoard board, ChessMove move, Collection<ChessMove> moveSet, ChessPiece movePiece){
        //Test for in bounds
        var endPos=move.getEndPosition();
        var col=endPos.getColumn();
        var row=endPos.getRow();

        if(row>8||row<1||col>8||col<1){
            return false;
        }

        //Test for pieces
        var testPiece=board.getPiece(endPos);
        if(testPiece==null||testPiece.getTeamColor()!=movePiece.getTeamColor()){
            moveSet.add(move);
            return true;
        }
        else{
            return false;
        }
    }
}
