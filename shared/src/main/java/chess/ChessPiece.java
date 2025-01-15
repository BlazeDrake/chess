package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    private ChessPosition pos;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor=pieceColor;
        this.type=type;
        this.pos=null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type && Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type, pos);
    }

    @Override
    public String toString(){
        String pieceInfo=pieceColor+" "+type+"; ";
        if(pos!=null){
            pieceInfo+= "at "+pos.toString();
        }
        else{
            pieceInfo+=" not placed";
        }
        return pieceInfo;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * @return the current position of this piece
     */
    public ChessPosition getPos() {
        return pos;
    }

    /**
     *
     * @param pos the position to set this chess piece to
     */
    public void setPos(ChessPosition pos){
        this.pos=pos;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch(type){
            case BISHOP -> {
                return getBishopMoves(board,myPosition);
            }
            case ROOK -> {
                return getRookMoves(board,myPosition);
            }
            case QUEEN -> {
                return getQueenMoves(board,myPosition);
            }
            case KNIGHT -> {
                return getKnightMoves(board,myPosition);
            }
            case PAWN -> {
                return getPawnMoves(board,myPosition);
            }
            case KING -> {
                return getKingMoves(board,myPosition);
            }
            default ->{
                return null;
            }
        }
    }


    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves= new HashSet<ChessMove>();
        final int col= myPosition.getColumn();
        final int row= myPosition.getRow();
        //down and to the left
        for(int i=1;i<Math.min(row,col);i++){
            var testPos=new ChessPosition(row-i,col-i);

            var blockingPiece=board.getPiece(testPos);

            if(blockingPiece!=null){
                if(blockingPiece.getTeamColor()!=getTeamColor()){
                    validMoves.add(new ChessMove(myPosition,testPos,null));
                }
                break;
            }
            else{
                validMoves.add(new ChessMove(myPosition,testPos,null));
            }
        }
        //down and to the right
        for(int i=1;i<Math.min(row,9-col);i++){
            var testPos=new ChessPosition(row-i,col+i);

            var blockingPiece=board.getPiece(testPos);

            if(blockingPiece!=null){
                if(blockingPiece.getTeamColor()!=getTeamColor()){
                    validMoves.add(new ChessMove(myPosition,testPos,null));
                }
                break;
            }
            else{
                validMoves.add(new ChessMove(myPosition,testPos,null));
            }
        }
        //up and to the right
        for(int i=1;i<Math.min(9-row,9-col);i++){
            var testPos=new ChessPosition(row+i,col+i);

            var blockingPiece=board.getPiece(testPos);

            if(blockingPiece!=null){
                if(blockingPiece.getTeamColor()!=getTeamColor()){
                    validMoves.add(new ChessMove(myPosition,testPos,null));
                }
                break;
            }
            else{
                validMoves.add(new ChessMove(myPosition,testPos,null));
            }
        }
        //up and to the left
        for(int i=1;i<Math.min(9-row,col);i++){
            var testPos=new ChessPosition(row+i,col-i);

            var blockingPiece=board.getPiece(testPos);

            if(blockingPiece!=null){
                if(blockingPiece.getTeamColor()!=getTeamColor()){
                    validMoves.add(new ChessMove(myPosition,testPos,null));
                }
                break;
            }
            else{
                validMoves.add(new ChessMove(myPosition,testPos,null));
            }
        }

        return validMoves;
    }
    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves= new HashSet<ChessMove>();
        final int col= myPosition.getColumn();
        final int row= myPosition.getRow();
        //Get all valid, unblocked positions in current column
        int i;
        for(i=row+1;i<=8;i++){
            var testPos=new ChessPosition(i,col);

            var blockingPiece=board.getPiece(testPos);

            if(blockingPiece!=null){
                if(blockingPiece.getTeamColor()!=getTeamColor()){
                    validMoves.add(new ChessMove(myPosition,testPos,null));
                }
                break;
            }
            else{
                validMoves.add(new ChessMove(myPosition,testPos,null));
            }
        }
        for(int j=row-1;j>=1;j--){
            var testPos=new ChessPosition(j,col);

            var blockingPiece=board.getPiece(testPos);

            if(blockingPiece!=null){
                if(blockingPiece.getTeamColor()!=getTeamColor()){
                    validMoves.add(new ChessMove(myPosition,testPos,null));
                }
                break;
            }
            else{
                validMoves.add(new ChessMove(myPosition,testPos,null));
            }
        }

        //Get all valid, unblocked positions in current row
        for(int k=col+1;k<=8;k++){
            var testPos=new ChessPosition(row,k);

            var blockingPiece=board.getPiece(testPos);

            if(blockingPiece!=null){
                if(blockingPiece.getTeamColor()!=getTeamColor()){
                    validMoves.add(new ChessMove(myPosition,testPos,null));
                }
                break;
            }
            else{
                validMoves.add(new ChessMove(myPosition,testPos,null));
            }
        }
        for(int l=col-1;l>=1;l--){
            var testPos=new ChessPosition(row,l);

            var blockingPiece=board.getPiece(testPos);

            if(blockingPiece!=null){
                if(blockingPiece.getTeamColor()!=getTeamColor()){
                    validMoves.add(new ChessMove(myPosition,testPos,null));
                }
                break;
            }
            else{
                validMoves.add(new ChessMove(myPosition,testPos,null));
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves=getRookMoves(board,myPosition);
        validMoves.addAll(getBishopMoves(board,myPosition));
        return validMoves;
    }

    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves= new HashSet<ChessMove>();
        final int col= myPosition.getColumn();
        final int row= myPosition.getRow();
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves= new HashSet<ChessMove>();
        final int col= myPosition.getColumn();
        final int row= myPosition.getRow();
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves= new HashSet<ChessMove>();
        final int col= myPosition.getColumn();
        final int row= myPosition.getRow();
        throw new RuntimeException("Not implemented");
    }
}
