package piece_calculators;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator{
   public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
      var validMoves= new HashSet<ChessMove>();
      final int col= myPosition.getColumn();
      final int row= myPosition.getRow();
      var piece=board.getPiece(myPosition);

      //Define dir, up by default (white pieces)
      int dir = 1;

      //Go down if black, add forward moves
      if(piece.getTeamColor()== ChessGame.TeamColor.BLACK){
         dir=-1;
      }
      //Add forward moves. It's ok that this would allow double moves for pawns at the end, as they can't move down twice anyway
      boolean unblocked=tryAddPawnMove(board,myPosition,new ChessPosition(row+dir, col), validMoves,piece, false);
      if((row==2||row==7)&&unblocked){
         tryAddPawnMove(board,myPosition,new ChessPosition(row+(2*dir), col), validMoves,piece, false);
      }


      //Check for captures
      tryAddPawnMove(board,myPosition,new ChessPosition(row+dir, col+1),validMoves,piece, true);
      tryAddPawnMove(board,myPosition,new ChessPosition(row+dir, col-1),validMoves,piece, true);

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
   private boolean tryAddPawnMove(ChessBoard board, ChessPosition startPos, ChessPosition endPos, Collection<ChessMove> moveSet, ChessPiece piece, boolean capture){
      //Test for in bounds
      var col=endPos.getColumn();
      var row=endPos.getRow();

      if(row>8||row<1||col>8||col<1){
         return false;
      }

      //check for promotions
      var moves=new HashSet<ChessMove>();
      if(row==8||row==1){
         moves.add(new ChessMove(startPos,endPos, ChessPiece.PieceType.BISHOP));
         moves.add(new ChessMove(startPos,endPos, ChessPiece.PieceType.ROOK));
         moves.add(new ChessMove(startPos,endPos, ChessPiece.PieceType.KNIGHT));
         moves.add(new ChessMove(startPos,endPos, ChessPiece.PieceType.QUEEN));
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
      else if(testPiece.getTeamColor()!=piece.getTeamColor()&&capture){
         moveSet.addAll(moves);
         return true;
      }
      else{
         return false;
      }
   }
}

