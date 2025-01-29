package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import piece_calculators.*;

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

    private boolean hasMoved;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.pos = null;
        this.hasMoved=false;
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
    public String toString() {
        String pieceInfo = pieceColor + " " + type + "; ";
        if (pos != null) {
            pieceInfo += "at " + pos.toString();
        } else {
            pieceInfo += " not placed";
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
     * @param pos the position to set this chess piece to
     */
    public void setPos(ChessPosition pos) {
        this.pos = pos;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean value){
        hasMoved=value;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case BISHOP -> {
                return new BishopMovesCalculator().pieceMoves(board, myPosition);
            }
            case ROOK -> {
                return new RookMovesCalculator().pieceMoves(board, myPosition);
            }
            case QUEEN -> {
                return new QueenMovesCalculator().pieceMoves(board, myPosition);
            }
            case KNIGHT -> {
                return new KnightMovesCalculator().pieceMoves(board, myPosition);
            }
            case PAWN -> {
                return new PawnMovesCalculator().pieceMoves(board, myPosition);
            }
            case KING -> {
                return new KingMovesCalculator().pieceMoves(board, myPosition);
            }
            default -> {
                return null;
            }
        }
    }

}