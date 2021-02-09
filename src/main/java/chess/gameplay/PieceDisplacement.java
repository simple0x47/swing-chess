package chess.gameplay;

import chess.gameplay.world.Board;
import chess.gameplay.world.BoardCell;
import chess.gameplay.world.ChessPiece;
import chess.gameplay.world.Player;

// Rule provider for all the chess pieces.
public class PieceDisplacement   
{
    // Id of the chess piece being moved.
    private final String chessPieceId;

    public String getChessPieceId() {
        return chessPieceId;
    }

    // Instance of Player which has requested the move.
    private final Player sourcePlayer;
    public Player getSourcePlayer() {
        return sourcePlayer;
    }

    // Column towards where the piece is being moved.
    private final int targetColumn;

    public int getTargetColumn() {
        return targetColumn;
    }

    // Row towards where the piece is being moved.
    private final int targetRow;

    public int getTargetRow() {
        return targetRow;
    }

    //
    // Creates an instance of PieceDisplacement.
    //
    public PieceDisplacement(String chessPieceId, Player player, int targetColumn, int targetRow)  {
        // Initializes the properties with the passed arguments.
        this.chessPieceId = chessPieceId;
        this.sourcePlayer = player;
        this.targetColumn = targetColumn;
        this.targetRow = targetRow;
    }

    //
    //	Provides a reply whether or not the specified movement is legal.
    //
    public static boolean isMovementLegal(Board board, ChessPiece piece, Player player,
                                          int targetColumn, int targetRow)  {
        // Check if arguments are valid.
        if (targetColumn < 0 || targetColumn >= Board.CHESS_COLUMN_COUNT)
        {
            return false;
        }
         
        if (targetRow < 0 || targetRow >= Board.CHESS_ROW_COUNT)
        {
            return false;
        }
         
        if (piece == null)
        {
            throw new IllegalStateException("Tried to check whether movement was legal or not," +
                    " by using a chess piece which is not available on the board.");
        }
         
        // Only knights can move on non-diagonal ways.
        if ((piece.getType() != ChessPiece.PieceType.KNIGHT) && (targetRow != piece.getRow()) &&
                (targetColumn != piece.getColumn()))
        {
            int slope = (targetRow - piece.getRow()) / (targetColumn - piece.getColumn());

            if ((slope != 1) && (slope != -1))
            {
                return false;
            }
        }
         
        // Check the movement depending on the chess piece's type.
        switch(piece.getType())
        {
            case PAWN: 
                return checkMovementForPawn(board,piece,player,targetColumn,targetRow);
            case KNIGHT: 
                return checkMovementForKnight(board,piece,player,targetColumn,targetRow);
            case BISHOP: 
                return checkMovementForBishop(board,piece,player,targetColumn,targetRow);
            case ROOK: 
                return checkMovementForRook(board,piece,player,targetColumn,targetRow);
            case QUEEN: 
                return (checkMovementForBishop(board,piece,player,targetColumn,targetRow) ||
                        checkMovementForRook(board,piece,player,targetColumn,targetRow));
            case KING: 
                return checkMovementForKing(board,piece,player,targetColumn,targetRow);
        
        }
        return false;
    }

    //
    // Specifies whether or not the described movement
    // can be made by a Pawn.
    //
    private static boolean checkMovementForPawn(Board board, ChessPiece piece, Player player,
                                                int targetColumn, int targetRow)  {
        // We check the direction of the pawn movement.
        int movementDirection = (player.getId() == 0) ? 1 : -1;
        BoardCell targetCell = board.getCell(targetColumn,targetRow);
        int playerPawnsRow = (Board.CHESS_PREVIOUS_TOP_PAWNS_ROW * player.getId()) + 1;
        // If movement is made on the same column, then the space in front
        // must be empty.
        if (piece.getColumn() == targetColumn)
        {
            if (piece.getRow() + movementDirection == targetRow)
            {
                return !(targetCell.hasPiece());
            }
            else if (piece.getRow() == playerPawnsRow)
            {
                int rowsDifferential = Math.abs(piece.getRow() - targetRow);
                if (rowsDifferential == 2)
                {
                    ChessPiece pieceInPath = board.detectFirstPieceWithinLine(piece.getColumn(),
                            piece.getRow(),targetColumn,targetRow);
                    return (pieceInPath == null);
                }
            }
        }
        else if ((piece.getColumn() + 1 == targetColumn) || (piece.getColumn() - 1 == targetColumn))
        {
            // If the movement its a diagonal one, then there must be an
            // enemy piece located on them.
            if (piece.getRow() + movementDirection == targetRow)
            {
                if (targetCell.hasPiece())
                {
                    return (targetCell.getPiece().getOwner() != player);
                }
                 
            }
             
        }
          
        return false;
    }

    //
    // Describes whether or not the specified movement can
    // be made by a Knight.
    //
    private static boolean checkMovementForKnight(Board board, ChessPiece piece,
                                                  Player player, int targetColumn, int targetRow)  {
        // Amount of difference between the current position of the piece
        // and the target position.
        int columnDifferential = Math.abs(piece.getColumn() - targetColumn);
        int rowDifferential = Math.abs(piece.getRow() - targetRow);
        // Knight movement can move with a distance of 2 columns and 1 row,
        // or 2 rows and 1 column.
        if (((columnDifferential == 2) && (rowDifferential == 1)) ||
                ((columnDifferential == 1) && (rowDifferential == 2)))
        {
            // Check if the target position has a piece on it.
            BoardCell targetCell = board.getCell(targetColumn,targetRow);
            if (targetCell.hasPiece())
            {
                return (targetCell.getPiece().getOwner() != player);
            }
            else
            {
                return true;
            } 
        }
         
        return false;
    }

    //
    // Describes whether or not the movement can be made by a Bishop.
    //
    private static boolean checkMovementForBishop(Board board, ChessPiece piece, Player player, int targetColumn, int targetRow)  {
        if ((targetRow != piece.getRow()) && (targetColumn != piece.getColumn()))
        {
            // If movement is not diagonal, then the movement it's illegal.
            if (Math.abs(targetRow - piece.getRow()) != Math.abs(targetColumn - piece.getColumn()))
            {
                return false;
            }
             
            // Detect if there's any chess piece on the path of the bishop.
            ChessPiece pieceInPath = board.detectFirstPieceWithinLine(piece.getColumn(),piece.getRow(),targetColumn,targetRow);
            // If there's no piece in the path, then the movement can be made.
            if (pieceInPath == null)
            {
                BoardCell targetCell = board.getCell(targetColumn,targetRow);
                if (targetCell.hasPiece())
                {
                    return (targetCell.getPiece().getOwner() != player);
                }
                 
                return true;
            }
             
        }
         
        return false;
    }

    //
    // Describes whether or not the movement can be made by a Rook.
    //
    private static boolean checkMovementForRook(Board board, ChessPiece piece,
                                                Player player, int targetColumn, int targetRow)  {
        // The movement must be linear on an axis.
        if ((piece.getColumn() == targetColumn) || (piece.getRow() == targetRow))
        {
            // Detect if there's any piece on the path.
            ChessPiece pieceInPath = board.detectFirstPieceWithinLine(piece.getColumn(),
                    piece.getRow(),targetColumn,targetRow);
            // There must be no piece on the path, since only the Knight
            // jumps over chess pieces.
            if (pieceInPath == null)
            {
                // Get the cell at the target position.
                BoardCell targetCell = board.getCell(targetColumn,targetRow);
                // Check whether or not there's a piece on the target cell.
                if (targetCell.hasPiece())
                {
                    return (targetCell.getPiece().getOwner() != player);
                }
                else
                {
                    return true;
                } 
            }
             
        }
         
        return false;
    }

    // If there's a piece, it must be the rival's one.
    //
    // Specifies whether or not the movement can be made by the King.
    //
    private static boolean checkMovementForKing(Board board, ChessPiece piece,
                                                Player player, int targetColumn, int targetRow)  {
        // Displacement desired to be made on the axes.
        int columnDifferential = Math.abs(piece.getColumn() - targetColumn);
        int rowDifferential = Math.abs(piece.getRow() - targetRow);
        // The king can only move within a radius of 1 cell.
        if ((columnDifferential <= 1) && (rowDifferential <= 1))
        {
            // The king cannot move on a cell which is being attacked.
            if (isCellUnderAttack(board,player,targetColumn,targetRow))
            {
                return false;
            }
             
            // Get the target cell.
            BoardCell targetCell = board.getCell(targetColumn,targetRow);
            if (targetCell.hasPiece())
            {
                // If there's a piece on the target cell, that is not
                // another king, and the owner of the piece is the rival,
                // then the movement can be made.
                return (targetCell.getPiece().getOwner() != player) &&
                        (targetCell.getPiece().getType() != ChessPiece.PieceType.KING);
            }
            else
            {
                return true;
            } 
        }
         
        return false;
    }

    //
    // Indicates whether or not a cell is in the path of a certain
    // rival's chess piece.
    //
    private static boolean isCellUnderAttack(Board board, Player player, int targetColumn, int targetRow)  {
        for (ChessPiece piece : board.getChessPieces())
        {
            if (piece.getOwner() != player)
            {
                // Check if enemy can move any of its pieces towards
                // the target position.
                if (isMovementLegal(board,piece,piece.getOwner(),targetColumn,targetRow))
                {
                    return true;
                }
                 
            }
             
        }
        return false;
    }

    //
    // Indicates whether or not the King has any room left where
    // he could go.
    //
    public static boolean hasKingAnyMovementsLeft(Board board, ChessPiece king)  {
        // Lowest available position.
        int minColumn = Math.max(0,king.getColumn() - 1);
        int minRow = Math.max(0,king.getRow() - 1);
        // Highest available position.
        int maxColumn = Math.min(7,king.getColumn() + 1);
        int maxRow = Math.min(7,king.getRow() + 1);
        // Retrieve how many cells are unavailable.
        BooleanFlag beingAttacked = new BooleanFlag();
        int unavailableCells = retrieveOccupiedCellsOnSurrounding(board,king);
        unavailableCells += retrieveAttackedCellsOnSurrounding(board,king,beingAttacked);

        // Inclusive positioning implemented by + 1, and exclusion of the
        // position of the chess piece with the -1.
        int maxKingMovements = ((Math.abs(maxColumn - minColumn) + 1) * (Math.abs(maxRow - minRow) + 1)) - 1;
        return (!beingAttacked.value) || (unavailableCells < maxKingMovements);
    }

    //
    // Retrieves the amount of occupied cells in the surrounding
    // of a king
    //
    private static int retrieveOccupiedCellsOnSurrounding(Board board, ChessPiece king)  {
        // Lowest available position.
        int minColumn = Math.max(0,king.getColumn() - 1);
        int minRow = Math.max(0,king.getRow() - 1);

        // Highest available position.
        int maxColumn = Math.min(7,king.getColumn() + 1);
        int maxRow = Math.min(7,king.getRow() + 1);

        // Amount of occupied cells.
        int occupiedCount = 0;
        for (int targetColumn = minColumn;targetColumn <= maxColumn;targetColumn++)
        {
            for (int targetRow = minRow;targetRow <= maxRow;targetRow++)
            {
                // We check the occupied cells in the surrounding of the King piece.
                // Get the cell within the radius of movement of the king.
                BoardCell cell = board.getCell(targetColumn,targetRow);
                // Check whether there is a piece or not.
                if (cell.hasPiece())
                {
                    ChessPiece pieceOnBoard = cell.getPiece();
                    if (pieceOnBoard != king)
                    {
                        // If there's the rival king on the cell,
                        // then the occupied cells amount increase.
                        if (pieceOnBoard.getType() == ChessPiece.PieceType.KING)
                        {
                            occupiedCount++;
                        }
                        else if (pieceOnBoard.getOwner() == king.getOwner())
                        {
                            occupiedCount++;
                        }
                          
                    }
                     
                }
                 
            }
        }
        return occupiedCount;
    }

    //
    // Retrieves the amount of attacked cells in the surrounding of
    // a king.
    //
    private static int retrieveAttackedCellsOnSurrounding(Board board, ChessPiece king, BooleanFlag beingAttacked)  {
        // Lowest available position.
        int minColumn = Math.max(0,king.getColumn() - 1);
        int minRow = Math.max(0,king.getRow() - 1);

        // Highest available position.
        int maxColumn = Math.min(7,king.getColumn() + 1);
        int maxRow = Math.min(7,king.getRow() + 1);

        // Indicate whether or not the king is being attacked.
        beingAttacked.value = false;

        // Indicates the amount of attacked cells in the surrounding
        // of the king.
        int attackedCount = 0;

        for (ChessPiece pieceOnBoard : board.getChessPieces())
        {
            // Check every rival's chess piece on the table, whether or not
            // it's attacking the king or his surroundings.
            // Check only if the piece, its a rival's one.
            if (pieceOnBoard.getOwner() != king.getOwner())
            {
                for (int targetColumn = minColumn;targetColumn <= maxColumn;targetColumn++)
                {
                    for (int targetRow = minRow;targetRow <= maxRow;targetRow++)
                    {
                        // Check whether or not the cell is being attacked.
                        if (isCellUnderAttack(board,king.getOwner(),targetColumn,targetRow))
                        {
                            if ((king.getColumn() == targetColumn) && (king.getRow() == targetRow))
                            {
                                // The king is being attacked if
                                // his position is being attacked by
                                // the rival's chess piece.
                                beingAttacked.value = true;
                            }
                            else
                            {
                                attackedCount++;
                            } 
                        }
                         
                    }
                }
            }
             
        }
        return attackedCount;
    }
}