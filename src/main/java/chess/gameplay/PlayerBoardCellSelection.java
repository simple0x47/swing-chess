package chess.gameplay;

// Represents the selected positions, of the game board, made by a player.
public class PlayerBoardCellSelection   
{
    // Selected column.
    private int column;
    public int getColumn() {
        return column;
    }

    public void setColumn(int value) {
        column = value;
    }

    // Selected row.
    private int row;
    public int getRow() {
        return row;
    }

    public void setRow(int value) {
        row = value;
    }

    //
    // Initializes a selection with the passed position.
    //
    public PlayerBoardCellSelection(int column, int row)  {
        setColumn(column);
        setRow(row);
    }
}