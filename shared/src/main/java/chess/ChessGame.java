package chess;

import piece_calculators.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor curPlayer;
    private ChessPiece whiteKing;
    private ChessPiece blackKing;

    private ArrayList<CheckCalculator> checkCalculators;

    public ChessGame() {
        board=new ChessBoard();
        board.resetBoard();

        curPlayer=TeamColor.WHITE;

        checkCalculators = new ArrayList<>();
        checkCalculators.add(new CheckCalculator(new BishopMovesCalculator(), List.of(new ChessPiece.PieceType[]{ChessPiece.PieceType.QUEEN,ChessPiece.PieceType.BISHOP})));
        checkCalculators.add(new CheckCalculator(new RookMovesCalculator(), List.of(new ChessPiece.PieceType[]{ChessPiece.PieceType.QUEEN,ChessPiece.PieceType.ROOK})));
        checkCalculators.add(new CheckCalculator(new KnightMovesCalculator(), List.of(new ChessPiece.PieceType[]{ChessPiece.PieceType.KNIGHT})));
        checkCalculators.add(new CheckCalculator(new KingMovesCalculator(),List.of(new ChessPiece.PieceType[]{ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN})));
        checkCalculators.add(new CheckCalculator(new PawnMovesCalculator(),List.of(new ChessPiece.PieceType[]{ChessPiece.PieceType.PAWN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN})));

        //Get the kings
        whiteKing=board.getPiece(new ChessPosition(1,5));
        blackKing=board.getPiece(new ChessPosition(8,5));
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return curPlayer;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        curPlayer=team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && curPlayer == chessGame.curPlayer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, curPlayer);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private boolean isValidMove(ChessMove move){
        var pos=move.getStartPosition();
        var piece=board.getPiece(pos);
        boolean returnVal=true;
        if(piece==null){
            return false;
        }
        //temporarily move the piece, then test for check
        var baseOldPiece=board.getPiece(move.getEndPosition());
        var tempTestPiece = board.movePiece(move);
        if(tempTestPiece.getPieceType()== ChessPiece.PieceType.KING){
            if(tempTestPiece.getTeamColor()==TeamColor.WHITE){
                whiteKing=tempTestPiece;
            }
            else{
                blackKing=tempTestPiece;
            }
        }

        if(isInCheck(piece.getTeamColor())){
            returnVal=false;
        }
        //System.out.println(board.toString());

        var returnedTestPiece=board.movePiece(new ChessMove(move.getEndPosition(),pos,null));
        if(returnedTestPiece.getPieceType()== ChessPiece.PieceType.KING){
            if(returnedTestPiece.getTeamColor()==TeamColor.WHITE){
                whiteKing=returnedTestPiece;
            }
            else{
                blackKing=returnedTestPiece;
            }
        }
        if(baseOldPiece!=null){
            var oldPiece=new ChessPiece(baseOldPiece.getTeamColor(),baseOldPiece.getPieceType());
            board.addPiece(move.getEndPosition(), oldPiece);
        }
      //  System.out.println(returnVal);
      //  System.out.println(board.toString());
        return returnVal;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        var testPiece=board.getPiece(startPosition);
        if(testPiece==null){
            return null;
        }

        ArrayList<ChessMove> testMoves=new ArrayList<>(testPiece.pieceMoves(board,startPosition));
        /*
         foreach move in the chosen pieces available moves, run isvalidMove
         If it is, remove it from the list of available moves
         REturn whatever remains
         */
        for(int i=0;i< testMoves  .size();i++){
            if(!isValidMove(testMoves.get(i))){
                testMoves.remove(i);
                i--;
            }
        }
        return testMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        //run isValidMove on the move, then tests if it's the piece's teams turn. Moves the piece to endPos if both are true
        var possibleMoves=validMoves(move.getStartPosition());
        if(possibleMoves==null||!possibleMoves.contains(move)){
            throw new InvalidMoveException("Invalid move: "+move.toString());
        }
        else if(curPlayer!=board.getPiece(move.getStartPosition()).getTeamColor()){
            throw new InvalidMoveException("It is not this piece team's turn!");
        }
        else{
            var movedPiece=board.movePiece(move);
            //Update the turn
            curPlayer = curPlayer==TeamColor.WHITE?TeamColor.BLACK:TeamColor.WHITE;
            //If the piece is a king, update the king variable to the new one
            if(movedPiece!=null&&movedPiece.getPieceType()== ChessPiece.PieceType.KING){
                if(movedPiece.getTeamColor()==TeamColor.BLACK){
                    blackKing=movedPiece;
                }
                else{
                    whiteKing=movedPiece;
                }


            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        for(var calculator: checkCalculators){
            ChessPosition kingPos=teamColor==TeamColor.BLACK?blackKing.getPos():whiteKing.getPos();
            for(var pos: calculator.getCalculator().pieceMoves(board, kingPos)){
                //If a piece could move to the king's position(in line & a kind of piece that can make the move) & isn't the same team, the king is in check
                var testPiece=board.getPiece(pos.getEndPosition());
                if(testPiece!=null&&testPiece.getTeamColor()!=teamColor){
                    if(calculator.getValidPieceTypes().contains(testPiece.getPieceType())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean teamCanMove(TeamColor teamColor){
        for(int i=1;i<=8;i++){
            for(int j=1;j<=8;j++){
                var testPos=new ChessPosition(i,j);
                var testPiece=board.getPiece(testPos);
                if(testPiece!=null&&testPiece.getTeamColor()==teamColor){
                    var move = validMoves(testPos);
                    if(move!=null&&!move.isEmpty()){
                        return true;
                    }
                }

            }
        }

        return false;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor)&&!teamCanMove(teamColor);
        //Check if there are no valid moves for the king, and teh king is in check
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor)&&!teamCanMove(teamColor);
        //Check if there are no valid moves for the king, and teh king isn't in check
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board=board;
        //find the king
        for(int i=1;i<=8;i++){
            for(int j=1;j<=8;j++){
                var testPiece=board.getPiece(new ChessPosition(i,j));
                if(testPiece!=null&&testPiece.getPieceType()==ChessPiece.PieceType.KING){
                    if(testPiece.getTeamColor()==TeamColor.BLACK){
                        blackKing=testPiece;
                    }
                    else{
                        whiteKing=testPiece;
                    }
                }
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
