package chess;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    //FIXME: consider using a dict. This will make removing pieces O(n), but will make finding piece by position, which is more often, O(1)
    private ArrayList<ChessPiece> pieces;
    //2d array to store all positions. Form of (row, col). (1,1) bottom left, (8,8) top right
    private ChessPosition[][] positions=new ChessPosition[8][8];

    public ChessBoard() {
        //Set up the positions
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                positions[i][j]=new ChessPosition(i+1,j+1);
            }
        }
        //Set up the pieces
        pieces=new ArrayList<>();
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieces.add(piece);
        piece.setPos(position);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        for(var piece: pieces){
            if(position.equals(piece.getPos())){
                return piece;
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        pieces.clear();

        //pawns
        //The rest
    }
}
