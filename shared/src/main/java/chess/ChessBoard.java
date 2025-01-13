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
     * Adds 4 copies of pieces that are repeated to the board. Used for resetting the board only
     * @param col the column on the right to add the piece to (must not be larger than 4)
     * @param piece the type of piece to add
     */
    private void addQuadruplePiece(int col, ChessPiece.PieceType piece){
        //sanitize data
        if(col>4){
            col=8-col;
        }
        addPiece(new ChessPosition(1,col),new ChessPiece(ChessGame.TeamColor.WHITE,piece));
        addPiece(new ChessPosition(1,9-col),new ChessPiece(ChessGame.TeamColor.WHITE,piece));
        addPiece(new ChessPosition(8,col),new ChessPiece(ChessGame.TeamColor.BLACK,piece));
        addPiece(new ChessPosition(8,9-col),new ChessPiece(ChessGame.TeamColor.BLACK,piece));
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
        for(int i=1;i<=8;i++){
            addPiece(new ChessPosition(2,i),new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7,i),new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN));
        }
        //all other pieces
        addQuadruplePiece(1, ChessPiece.PieceType.ROOK);
        addQuadruplePiece(2, ChessPiece.PieceType.KNIGHT);
        addQuadruplePiece(3, ChessPiece.PieceType.BISHOP);

        addPiece(new ChessPosition(1,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));

        addPiece(new ChessPosition(8,4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        //System.out.println(this);
    }
    public String toString(){
        StringBuilder output= new StringBuilder();
        for(int i=8;i>=1;i--){
            for(int j=1;j<=8;j++){
                var piece=getPiece(new ChessPosition(i,j));
                if(piece==null){
                    output.append("_");
                }
                else if(piece.getTeamColor()== ChessGame.TeamColor.WHITE){
                    output.append(switch(piece.getPieceType()){
                        case KING -> "K";
                        case PAWN -> "P";
                        case KNIGHT -> "N";
                        case ROOK -> "R";
                        case QUEEN -> "Q";
                        case BISHOP -> "B";
                        default -> "_";
                    });
                }
                //Piece is black
                else{
                    output.append(switch(piece.getPieceType()){
                        case KING -> "k";
                        case PAWN -> "p";
                        case KNIGHT -> "n";
                        case ROOK -> "r";
                        case QUEEN -> "q";
                        case BISHOP -> "b";
                        default -> "_";
                    });
                }
            }
            output.append("\n");
        }
        return output.toString();
    }
}
