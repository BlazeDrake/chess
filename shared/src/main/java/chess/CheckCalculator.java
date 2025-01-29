package chess;


import calculators.PieceMovesCalculator;

import java.util.Collection;
import java.util.HashSet;

public class CheckCalculator {
    private PieceMovesCalculator calculator;
    private HashSet<ChessPiece.PieceType> validPieceTypes;

    public CheckCalculator(PieceMovesCalculator calculator, Collection<ChessPiece.PieceType> pieceTypes) {
        this.calculator = calculator;
        this.validPieceTypes= new HashSet<ChessPiece.PieceType>();
        validPieceTypes.addAll(pieceTypes);
    }

    public HashSet<ChessPiece.PieceType> getValidPieceTypes() {
        return validPieceTypes;
    }

    public PieceMovesCalculator getCalculator() {
        return calculator;
    }

}
