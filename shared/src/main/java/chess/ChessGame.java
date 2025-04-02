package chess;

import calculators.*;

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

    private ChessPosition enPassantPos;

    private GameState curState;


    private static final CheckCalculator[] CHECK_CALCULATORS = {
            new CheckCalculator(new BishopMovesCalculator(), List.of(
                    ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP
            )),
            new CheckCalculator(new RookMovesCalculator(), List.of(
                    ChessPiece.PieceType.QUEEN,
                    ChessPiece.PieceType.ROOK
            )),
            new CheckCalculator(new KnightMovesCalculator(), List.of(
                    ChessPiece.PieceType.KNIGHT
            )),
            new CheckCalculator(new KingMovesCalculator(), List.of(
                    ChessPiece.PieceType.ROOK,
                    ChessPiece.PieceType.KING,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.QUEEN
            )),
            new CheckCalculator(new PawnMovesCalculator(), List.of(
                    ChessPiece.PieceType.PAWN,
                    ChessPiece.PieceType.KING,
                    ChessPiece.PieceType.BISHOP,
                    ChessPiece.PieceType.QUEEN
            ))
    };

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();

        curPlayer = TeamColor.WHITE;

        curState = GameState.IN_PROGRESS;

        //Get the kings
        whiteKing = board.getPiece(new ChessPosition(1, 5));
        blackKing = board.getPiece(new ChessPosition(8, 5));

        enPassantPos = null;
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
        curPlayer = team;
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

    private boolean isValidMove(ChessMove move) {
        var pos = move.getStartPosition();
        var piece = board.getPiece(pos);
        boolean returnVal = true;
        if (piece == null) {
            return false;
        }
        //temporarily move the piece, then test for check
        var baseOldPiece = board.getPiece(move.getEndPosition());
        var tempTestPiece = board.movePiece(move);
        if (tempTestPiece.getPieceType() == ChessPiece.PieceType.KING) {
            if (tempTestPiece.getTeamColor() == TeamColor.WHITE) {
                whiteKing = tempTestPiece;
            }
            else {
                blackKing = tempTestPiece;
            }
        }

        if (isInCheck(piece.getTeamColor())) {
            returnVal = false;
        }
        //System.out.println(board.toString());

        var returnedTestPiece = board.movePiece(new ChessMove(move.getEndPosition(), pos, null));
        if (returnedTestPiece.getPieceType() == ChessPiece.PieceType.KING) {
            if (returnedTestPiece.getTeamColor() == TeamColor.WHITE) {
                whiteKing = returnedTestPiece;
            }
            else {
                blackKing = returnedTestPiece;
            }
        }
        if (baseOldPiece != null) {
            var oldPiece = new ChessPiece(baseOldPiece.getTeamColor(), baseOldPiece.getPieceType());
            board.addPiece(move.getEndPosition(), oldPiece);
        }
        //  System.out.println(returnVal);
        //  System.out.println(board.toString());
        return returnVal;
    }

    /**
     * Tests if a rook can castle with the king
     *
     * @param rookPos the position of the rook
     */
    private boolean rookIsValid(ChessPosition rookPos) {
        var rook = board.getPiece(rookPos);
        //exit early if the rook has moved(if it's not a rook, it would have had to move to be there) or if there's no piece in the rook pos
        if (rook == null || rook.getHasMoved()) {
            return false;
        }
        //check for blocks

        int rookCol = rookPos.getColumn();
        //Set the direction to test ( will move right if in column 1; otherwise it must be in column 8 and so will  move left)
        int dir = rookCol == 1 ? 1 : -1;
        //Start 1 space in the direction of dir, then continue until hitting a piece or the board end
        for (int i = 1; i <= 7; i++) {
            var testPos = new ChessPosition(rookPos.getRow(), rookCol + (i * dir));
            var testPiece = board.getPiece(testPos);
            //if the first piece is a king of the same color, return true. Otherwise, return false
            if (testPiece != null) {
                return testPiece.getPieceType() == ChessPiece.PieceType.KING && testPiece.getTeamColor() == rook.getTeamColor();
            }
        }

        return false;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        var testPiece = board.getPiece(startPosition);
        if (testPiece == null) {
            return null;
        }

        ArrayList<ChessMove> testMoves = new ArrayList<>(testPiece.pieceMoves(board, startPosition));
        /*
         foreach move in the chosen pieces available moves, run isvalidMove
         If it is, remove it from the list of available moves
         Return whatever remains
         */
        for (int i = 0; i < testMoves.size(); i++) {
            if (!isValidMove(testMoves.get(i))) {
                testMoves.remove(i);
                i--;
            }
        }
        //Add en passant (add a variable for en passantable location) and castling (for castling, add a variable for if the king moved)if valid
        if (testPiece.getPieceType() == ChessPiece.PieceType.PAWN && enPassantPos != null) {
            new PawnMovesCalculator().tryAddPawnMove(board, startPosition, enPassantPos, testMoves, false);
        }
        else if (testPiece.getPieceType() == ChessPiece.PieceType.KING) {
            //If the king isn't in check, and hasn't moved, test for potential castling
            if (!testPiece.getHasMoved() && !isInCheck(testPiece.getTeamColor())) {
                var row = startPosition.getRow();
                //test left rook
                if (rookIsValid(new ChessPosition(row, 1))) {
                    testMoves.add(new ChessMove(startPosition, new ChessPosition(row, startPosition.getColumn() - 2), null));
                }
                //test right rook
                if (rookIsValid(new ChessPosition(row, 8))) {
                    testMoves.add(new ChessMove(startPosition, new ChessPosition(row, startPosition.getColumn() + 2), null));
                }
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

        var endPos = move.getEndPosition();
        var startPos = move.getStartPosition();
        //run isValidMove on the move, then tests if it's the piece's teams turn. Moves the piece to endPos if both are true
        var possibleMoves = validMoves(startPos);
        if (curState != GameState.IN_PROGRESS) {
            throw new InvalidMoveException("Game is already finished!");
        }
        else if (possibleMoves == null || !possibleMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move: " + move.toString());
        }
        else if (curPlayer != board.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException("It is not this piece team's turn!");
        }
        else {
            var movedPiece = board.movePiece(move);
            //If the piece is a king, update the king variable to the new one
            if (movedPiece != null) {
                movedPiece.setHasMoved(true);
                if (movedPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    handleCastleMove(endPos, startPos);
                    if (movedPiece.getTeamColor() == TeamColor.BLACK) {
                        blackKing = movedPiece;
                    }
                    else {
                        whiteKing = movedPiece;
                    }
                }
                //handle en passant (if it's a pawn double moving, store it in the en passant variable. Otherwise reset en passant variable)
                if (movedPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    handleEnPassant(move, endPos, startPos);
                }
                else {
                    enPassantPos = null;
                }
            }

            //Update the turn
            curPlayer = curPlayer == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;

            //check if the game is over
            if (isInCheck(TeamColor.WHITE)) {
                setCurState(GameState.BLACK_WIN);
            }
            else if (isInCheck(TeamColor.BLACK)) {
                setCurState(GameState.WHITE_WIN);
            }
            //only check next player, the outcome is the same regardless of team
            else if (isInStalemate(curPlayer)) {
                setCurState(GameState.STALEMATE);
            }
        }
    }

    private void handleCastleMove(ChessPosition endPos, ChessPosition startPos) {
        int colMovement = endPos.getColumn() - startPos.getColumn();
        //right castle
        if (colMovement == 2) {
            var rookPos = new ChessPosition(endPos.getRow(), 8);
            board.getPiece(rookPos).setHasMoved(true);
            board.movePiece(new ChessMove(rookPos, new ChessPosition(endPos.getRow(), endPos.getColumn() - 1), null));
        }
        //left castle
        else if (colMovement == -2) {
            var rookPos = new ChessPosition(endPos.getRow(), 1);
            board.getPiece(rookPos).setHasMoved(true);
            board.movePiece(new ChessMove(rookPos, new ChessPosition(endPos.getRow(), endPos.getColumn() + 1), null));
        }
    }

    private void handleEnPassant(ChessMove move, ChessPosition endPos, ChessPosition startPos) {
        if (Math.abs(endPos.getRow() - startPos.getRow()) == 2) {
            int mult = curPlayer == TeamColor.BLACK ? -1 : 1;
            enPassantPos = new ChessPosition(startPos.getRow() + mult, startPos.getColumn());
        }
        else {
            if (move.getEndPosition().equals(enPassantPos)) {
                //additional checks
                board.removePiece(new ChessPosition(startPos.getRow(), endPos.getColumn()));
            }
            enPassantPos = null;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        for (var calculator : CHECK_CALCULATORS) {
            ChessPosition kingPos = teamColor == TeamColor.BLACK ? blackKing.getPos() : whiteKing.getPos();
            for (var pos : calculator.getCalculator().pieceMoves(board, kingPos)) {
                //If a piece could move to the king's position & isn't the same team, the king is in check
                var testPiece = board.getPiece(pos.getEndPosition());
                if (testPiece != null && testPiece.getTeamColor() != teamColor) {
                    if (calculator.getValidPieceTypes().contains(testPiece.getPieceType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean teamCanMove(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var testPos = new ChessPosition(i, j);
                var testPiece = board.getPiece(testPos);
                if (testPiece != null && testPiece.getTeamColor() == teamColor) {
                    var move = validMoves(testPos);
                    if (move != null && !move.isEmpty()) {
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
        return isInCheck(teamColor) && !teamCanMove(teamColor);
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
        return !isInCheck(teamColor) && !teamCanMove(teamColor);
        //Check if there are no valid moves for the king, and teh king isn't in check
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        //find the king
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                var testPiece = board.getPiece(new ChessPosition(i, j));
                if (testPiece != null && testPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (testPiece.getTeamColor() == TeamColor.BLACK) {
                        blackKing = testPiece;
                    }
                    else {
                        whiteKing = testPiece;
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

    public GameState getCurState() {
        return curState;
    }

    public void setCurState(GameState curState) {
        this.curState = curState;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public enum GameState {
        IN_PROGRESS,
        WHITE_WIN,
        BLACK_WIN,
        STALEMATE
    }

}
