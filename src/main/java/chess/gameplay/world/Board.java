package chess.gameplay.world;

import chess.gameplay.PieceDisplacement;

import java.util.ArrayList;

// Stores the different movements made within the game.
public class Board   
{
    // Size of the side of the chess board.
    public static final int SIDE_SIZE = 8;

    // Number of rows on a chess board.
    public static final int CHESS_ROW_COUNT = 8;

    // Number of columns on a chess board.
    public static final int CHESS_COLUMN_COUNT = 8;

    // Number of the highest row where a king is located.
    public static final int CHESS_TOP_KING_ROW = 7;

    // Number of the previous row of the highest row where pawns
    // are located.
    public static final int CHESS_PREVIOUS_TOP_PAWNS_ROW = 5;

    // Cell holder.
    private final BoardCell[][] cells;

    // List containing chess pieces in order to avoid checking each board cell.
    private ArrayList<ChessPiece> chessPieces;

    public ArrayList<ChessPiece> getChessPieces() {
        return chessPieces;
    }

    public void setChessPieces(ArrayList<ChessPiece> value) {
        chessPieces = value;
    }

    //
    // Initializes an instance of Board by creating its inner cells and
    // the holder for the chess pieces.
    //
    public Board() {
        // Initializer for each board cell.
        cells = new BoardCell[SIDE_SIZE][SIDE_SIZE];
        for (int column = 0;column < CHESS_COLUMN_COUNT;column++)
        {
            for (int row = 0;row < CHESS_ROW_COUNT;row++)
            {
                cells[column][row] = new BoardCell();
            }
        }
        // Initializer of the holder for the chess pieces.
        setChessPieces(new ArrayList<ChessPiece>());
    }

    //
    //	Returns the cell found at the specified column and row.
    //
    public BoardCell getCell(int column, int row) {
        return cells[column][row];
    }

    //
    //	Adds a chess piece to the board on a cell defined
    //	by it's column and row.
    //
    public void addPieceToCell(ChessPiece piece, int column, int row) {
        // Arguments validation.
        if (piece == null)
        {
            throw new NullPointerException("'piece' cannot be null.");
        }
         
        if ((column < 0) || (column >= CHESS_COLUMN_COUNT))
        {
            throw new IllegalArgumentException("'column' holds an invalid value: " + column);
        }
         
        if ((row < 0) || (row >= CHESS_ROW_COUNT))
        {
            throw new IllegalArgumentException("'row' holds an invalid value: " + row);
        }
         
        if (cells[column][row].hasPiece())
        {
            throw new IllegalStateException("Cell is occupied already.");
        }
         
        // Addition of the pieces to the according board cell.
        cells[column][row].setPiece(piece);
        getChessPieces().add(piece);
    }

    //
    // Applies a movement to the board chess pieces.
    //
    public void applyMovement(PieceDisplacement movement) {
        // Retrieve chess piece by using its ID.
        ChessPiece piece = getChessPieceById(movement.getChessPieceId());
        // Piece cannot be null.
        if (piece == null)
        {
            throw new IllegalStateException("'movement' contains an 'ChessPieceId': " +
                    movement.getChessPieceId());
        }
         
        // Retrieves the target cell located at a certain column and row.
        BoardCell targetCell = getCell(movement.getTargetColumn(),movement.getTargetRow());
        // In case cell has a piece, its owner must be the other player.
        if (targetCell.hasPiece())
        {
            if (targetCell.getPiece().getOwner() != piece.getOwner())
            {
                removePiece(targetCell.getPiece());
            }
            else
            {
                throw new IllegalStateException("Tried to apply an illegal movement.");
            } 
        }
         
        // Retrieve the cell from which the piece has originated the move.
        BoardCell sourceCell = getCell(piece.getColumn(),piece.getRow());
        // We remove the piece from the cell from which one we departed.
        sourceCell.setPiece(null);
        // Update the status of the piece
        piece.setColumn(movement.getTargetColumn());
        piece.setRow(movement.getTargetRow());
        targetCell.setPiece(piece);
    }

    //
    // Remove the piece from the holder of chess pieces.
    //
    private void removePiece(ChessPiece piece) {
        getChessPieces().remove(piece);
    }

    //
    // Retrieves the chess piece by looking for its Id.
    //
    public ChessPiece getChessPieceById(String pieceId) {
        // Assure piece ID is not empty.
        if (pieceId.length() == 0)
        {
            throw new IllegalArgumentException("'pieceID' is empty.");
        }

        for (BoardCell[] column : cells)
        {
            for (BoardCell cell : column) {
                if (cell.hasPiece())
                {
                    if (pieceId.equals(cell.getPiece().getId()))
                    {
                        return cell.getPiece();
                    }

                }
            }
        }

        return null;
    }

    //
    // Detects the first piece within the defined linear path.
    //
    public ChessPiece detectFirstPieceWithinLine(int startColumn, int startRow, int endColumn, int endRow) {
        // Check for pieces on the same column.
        if (startColumn == endColumn)
        {
            int rowDirection = (int)Math.signum(endRow - startRow);
            int row = startRow + rowDirection;
            while (true)
            {
                // Go through all rows.
                if (((rowDirection == 1) && (row < endRow)) || ((rowDirection == -1) && (row > endRow)))
                {
                    BoardCell cell = getCell(startColumn,row);
                    if (cell.hasPiece())
                    {
                        return cell.getPiece();
                    }
                     
                }
                else
                {
                    break;
                } 
                row += rowDirection;
            }
        }
        else if (startRow == endRow)
        {
            // Check on the same row.
            // We define the direction in which we will progress.
            int columnDirection = (int)Math.signum(endColumn - startColumn);
            int column = startColumn + columnDirection;
            while (true)
            {
                // Go through all columns.
                if (((columnDirection == 1) && (column < endColumn)) || ((columnDirection == -1) &&
                        (column > endColumn)))
                {
                    BoardCell cell = getCell(column,startRow);
                    if (cell.hasPiece())
                    {
                        return cell.getPiece();
                    }
                     
                }
                else
                {
                    break;
                } 
                column += columnDirection;
            }
        }
        else
        {
            // If movement is not diagonal, then the movement it's illegal.
            if (Math.abs(endRow - startRow) != Math.abs(endColumn - startColumn))
            {
                throw new IllegalArgumentException("Line can be only straight or diagonal.");
            }
             
            // We define the direction in which we will progress.
            int columnDirection = (int)Math.signum(endColumn - startColumn);
            int rowDirection = (int)Math.signum(endRow - startRow);
            // Check for diagonal movements on all columns and rows.
            int column = startColumn + columnDirection;
            int row = startRow + rowDirection;
            while (true)
            {
                if (((columnDirection == 1) && (column < endColumn)) || ((columnDirection == -1) &&
                        (column > endColumn)))
                {
                    if (((rowDirection == 1) && (row < endRow)) || ((rowDirection == -1) && (row > endRow)))
                    {
                        BoardCell cell = getCell(column,row);
                        if (cell.hasPiece())
                        {
                            return cell.getPiece();
                        }
                         
                    }
                    else
                    {
                        break;
                    } 
                }
                else
                {
                    break;
                } 
                column += columnDirection;
                row += rowDirection;
            }
        }  
        return null;
    }

    //
    //	Retrieves the king chess piece of the specified player.
    //
    public ChessPiece getKingOfPlayer(Player player)  {
        for (BoardCell[] column : cells)
        {
            for (BoardCell cell : column) {
                if (cell.hasPiece()) {
                    if ((cell.getPiece().getOwner() == player) &&
                            (cell.getPiece().getType() == ChessPiece.PieceType.KING)) {
                        return cell.getPiece();
                    }
                }
            }
        }
        return null;
    }
}


