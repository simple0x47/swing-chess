package chess.gameplay.world;

// Abstraction of a player of the game.
public class Player   
{
    // Identifier for the player instance.
    private final int id;

    public int getId() {
        return id;
    }

    // Name inserted by a player of the game.
    private final String name;

    public String getName() {
        return name;
    }

    public ChessPiece.PieceColor getPiecesColor()  {
        // 0 -> First player, and first player always are the white ones.
        if (getId() == 0)
        {
            return ChessPiece.PieceColor.WHITE;
        }
        else
        {
            return ChessPiece.PieceColor.BLACK;
        } 
    }

    //
    // Constructs an instance of Player with an ID and a name.
    //
    public Player(int id, String name)  {
        this.id = id;
        this.name = name;
    }
}


