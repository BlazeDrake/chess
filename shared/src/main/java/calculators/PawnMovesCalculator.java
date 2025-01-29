package calculators;

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
      boolean unblocked=tryAddPawnMove(board,myPosition,new ChessPosition(row+dir, col), validMoves, false);
      if((row==2||row==7)&&unblocked){
         tryAddPawnMove(board,myPosition,new ChessPosition(row+(2*dir), col), validMoves, false);
      }


      //Check for captures
      tryAddPawnMove(board,myPosition,new ChessPosition(row+dir, col+1),validMoves, true);
      tryAddPawnMove(board,myPosition,new ChessPosition(row+dir, col-1),validMoves, true);

      return validMoves;
   }
   /**
    * Testing a move for a pawn. Includes promotion
    * @param board chessboard used
    * @param start the start position
    * @param end the end position
    * @param moves the collection to add the move to if successful
    * @param capture if this move is for capturing
    * @return true if successful, false if not
    */
   public boolean tryAddPawnMove(ChessBoard board, ChessPosition start, ChessPosition end, Collection<ChessMove> moves, boolean capture){
      //Test for in bounds
      var col=end.getColumn();
      var row=end.getRow();

      if(row>8||row<1||col>8||col<1){
         return false;
      }

      //test if pawn is valid
      var pawn=board.getPiece(start);
      if(pawn==null){
         return false;
      }

      //check for promotions
      var testMoves=new HashSet<ChessMove>();
      if(row==8||row==1){
         testMoves.add(new ChessMove(start,end, ChessPiece.PieceType.BISHOP));
         testMoves.add(new ChessMove(start,end, ChessPiece.PieceType.ROOK));
         testMoves.add(new ChessMove(start,end, ChessPiece.PieceType.KNIGHT));
         testMoves.add(new ChessMove(start,end, ChessPiece.PieceType.QUEEN));
      }
      else{
         testMoves.add(new ChessMove(start,end,null));
      }

      //Test for pieces
      var testPiece=board.getPiece(end);
      if(testPiece==null){
         if(!capture){
            moves.addAll(testMoves);
            return true;
         }
         else{
            return false;
         }
      }
      else if(testPiece.getTeamColor()!=pawn.getTeamColor()&&capture){
         moves.addAll(testMoves);
         return true;
      }
      else{
         return false;
      }
   }
}

