package chess.gameplay.world;

// Abstraction of a chess piece.
public class ChessPiece   
{
    public enum PieceType
    {
        // Different possible types of chess pieces.
        KING,
        QUEEN,
        ROOK,
        KNIGHT,
        BISHOP,
        PAWN
    }

    public enum PieceColor
    {
        BLACK,
        WHITE
    }

    // Used as identifier through the game.
    private final String id;

    public String getId() {
        return id;
    }

    // Defines the player who owns this piece.
    private final Player owner;

    public Player getOwner() {
        return owner;
    }

    // Specifies the type of this piece.
    private PieceType type;

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType value) {
        type = value;
    }

    // Specifies the column on which this piece is located on a board.
    private int column;

    public int getColumn() {
        return column;
    }

    public void setColumn(int value) {
        column = value;
    }

    // Specifies the row on which this piece is located on a board.
    private int row;

    public int getRow() {
        return row;
    }

    public void setRow(int value) {
        row = value;
    }

    public ChessPiece(int pieceNumber, Player player, PieceType type, int column, int row)  {
        if (player == null)
        {
            throw new NullPointerException("'player' is null.");
        }

        id = calculateId(pieceNumber, player, type);
        owner = player;
        this.type = type;
        this.column = column;
        this.row = row;
    }

    //
    // Calculates the Id of a chess piece by making usage
    // of several parameters.
    //
    private static String calculateId(int pieceNumber, Player player, PieceType type)  {
        if (type == PieceType.KING)
        {
            return String.format("{%s}-{%s}", player, type);
        }
        else
        {
            return String.format("{%s}-{%s}-{%d}", player, type, pieceNumber);
        }
    }

    //
    //	Retrieves the symbol used for each type of chess piece.
    //
    private static String getPieceTypeInitialSymbol(PieceType type)  {
        switch(type)
        {
            case KING: 
                return "K";
            case QUEEN: 
                return "Q";
            case ROOK: 
                return "R";
            case KNIGHT: 
                return "N";
            case BISHOP: 
                return "B";
            case PAWN: 
                return "P";
            default: 
                throw new IllegalArgumentException("Invalid 'type' detected.");
        
        }
    }

    //
    // Gets the type of chess piece accordingly to its starter position
    // on the king's row.
    //
    public static PieceType getInitialPieceTypeForColumnOnKingsRow(int column)  {
        if ((column < 0) || (column >= Board.CHESS_COLUMN_COUNT))
        {
            throw new IllegalArgumentException("'column' holds an invalid value: " + column);
        }
         
        // Obtain type depending on the column.
        switch(column)
        {
            case 0: 
            case 7: 
                return PieceType.ROOK;
            case 1: 
            case 6: 
                return PieceType.KNIGHT;
            case 2: 
            case 5: 
                return PieceType.BISHOP;
            case 3: 
                return PieceType.QUEEN;
            case 4: 
                return PieceType.KING;
            default: 
                throw new IllegalStateException("Unable to handle 'column' value: " + column);
        
        }
    }
}


