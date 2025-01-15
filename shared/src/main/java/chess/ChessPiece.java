package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import piece_calculators.KingMovesCalculator;
import piece_calculators.KnightMovesCalculator;

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
                return new KnightMovesCalculator().pieceMoves(board,myPosition);
            }
            case PAWN -> {
                return getPawnMoves(board,myPosition);
            }
            case KING -> {
                return new KingMovesCalculator().pieceMoves(board,myPosition);
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

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition){
        var validMoves= new HashSet<ChessMove>();
        final int col= myPosition.getColumn();
        final int row= myPosition.getRow();

        //Define dir, up by default (white pieces)
        int dir = 1;

        //Go down if black, add forward moves
        if(getTeamColor()== ChessGame.TeamColor.BLACK){
            dir=-1;
        }
        //Add forward moves. It's ok that this would allow double moves for pawns at the end, as they can't move down twice anyway
        boolean unblocked=tryAddPawnMove(board,myPosition,new ChessPosition(row+dir, col), validMoves,false);
        if((row==2||row==7)&&unblocked){
            tryAddPawnMove(board,myPosition,new ChessPosition(row+(2*dir), col), validMoves,false);
        }


        //Check for captures
        tryAddPawnMove(board,myPosition,new ChessPosition(row+dir, col+1),validMoves,true);
        tryAddPawnMove(board,myPosition,new ChessPosition(row+dir, col-1),validMoves,true);

        return validMoves;
    }
    /**
     * Testing a move for a pawn. Includes promotion
     * @param board chessboard used
     * @param startPos the start position
     * @param endPos the end position
     * @param moveSet the collection to add the move to if successful
     * @param capture if this move is for capturing
     * @return true if successful, false if not
     */
    private boolean tryAddPawnMove(ChessBoard board, ChessPosition startPos, ChessPosition endPos, Collection<ChessMove> moveSet, boolean capture){
        //Test for in bounds
        var col=endPos.getColumn();
        var row=endPos.getRow();

        if(row>8||row<1||col>8||col<1){
            return false;
        }

        //check for promotions
        var moves=new HashSet<ChessMove>();
        if(row==8||row==1){
            moves.add(new ChessMove(startPos,endPos,PieceType.BISHOP));
            moves.add(new ChessMove(startPos,endPos,PieceType.ROOK));
            moves.add(new ChessMove(startPos,endPos,PieceType.KNIGHT));
            moves.add(new ChessMove(startPos,endPos,PieceType.QUEEN));
        }
        else{
            moves.add(new ChessMove(startPos,endPos,null));
        }

        //Test for pieces
        var testPiece=board.getPiece(endPos);
        if(testPiece==null){
            if(!capture){
                moveSet.addAll(moves);
                return true;
            }
            else{
                return false;
            }
        }
        else if(testPiece.getTeamColor()!=getTeamColor()&&capture){
            moveSet.addAll(moves);
            return true;
        }
        else{
            return false;
        }
    }
}
