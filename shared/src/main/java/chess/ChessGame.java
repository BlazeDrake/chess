package chess;

import java.util.ArrayList;
import java.util.Collection;
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
    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;

    public ChessGame() {
        board=new ChessBoard();
        board.resetBoard();

        curPlayer=TeamColor.WHITE;
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
        board.movePiece(move);

        if(isInCheck(piece.getTeamColor())){
            returnVal=false;
        }
        System.out.println(board.toString());

        board.movePiece(new ChessMove(move.getEndPosition(),pos,null));
        if(baseOldPiece!=null){
            var oldPiece=new ChessPiece(baseOldPiece.getTeamColor(),baseOldPiece.getPieceType());
            board.addPiece(move.getEndPosition(), oldPiece);
        }
        System.out.println(returnVal);
        System.out.println(board.toString());
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
        for(int i=0;i< testMoves.size();i++){
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
        throw new RuntimeException("Not implemented");
        //run isValidMove on the move, then tests if it's the piece's teams turn. Moves the piece to endPos if both are true
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return true;
        //Maybe also lists or sets that contain all potential threats to kings? This would make checkmate tests easy.
        //The other alternative is to check down paths where a piece could get the king from the king, and check whatever specific path a piece is blocking when it tries to move

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
        throw new RuntimeException("Not implemented");
        //Check if there are no valid moves for the king, and teh king isn't in check
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board=board;
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
