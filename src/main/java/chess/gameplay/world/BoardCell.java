package chess.gameplay.world;

// Container for only one chess piece.
public class BoardCell   
{
    // Chess piece being stored (may be null).
    private ChessPiece piece;

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece value) {
        piece = value;
    }

    // Checks whether this cell has a chess piece stored inside.
    public boolean hasPiece() {
        return (getPiece() != null);
    }
}


