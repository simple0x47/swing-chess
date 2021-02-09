package chess.gameplay;

import chess.gameplay.world.Board;
import chess.gameplay.world.ChessPiece;
import chess.gameplay.world.Player;

import java.util.Calendar;
import java.util.Date;

// Abstraction of an instance of a chess match.
public class Match   
{
    // Defines the amount of players for a match.
    public static final int PLAYERS_AMOUNT = 2;

    // Instance of MatchWrapper, in order to communicate events.
    private final MatchWrapper wrapper;

    // Defines the communicating interface between the presentation layer
    // and the business layer.
    private final GameController controller;

    // Keeps track whether or not the match has finished.
    private boolean finished = false;

    // Instance of Player representing the first player.
    private final Player player1;
    public Player getPlayer1() {
        return player1;
    }

    // Instance of Player representing the second player.
    private final Player player2;
    public Player getPlayer2() {
        return player2;
    }

    // Instance of Player whose turn is now.
    private Player turnOfPlayer;
    public Player getTurnOfPlayer() {
        return turnOfPlayer;
    }

    public void setTurnOfPlayer(Player value) {
        turnOfPlayer = value;
    }

    // Instance of the Board used for this match.
    private final Board board;
    public Board getGameBoard() {
        return board;
    }

    // Instance of result stored which is going to be stored into a db.
    private MatchResult result;
    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult value) {
        result = value;
    }

    //
    // Creates an instance of Match with the passed arguments.
    //
    public Match(MatchWrapper wrapper, GameController controller, String player1Name, String player2Name)  {
        // Initializes the variables of the instance.
        this.wrapper = wrapper;
        this.controller = controller;

        // Initializes the properties of the instance.
        board = new Board();
        player1 = new Player(0, player1Name);
        player2 = new Player(1, player2Name);
    }

    //
    // Spawns the initial chess pieces onto the chess board.
    //
    private void spawnChessPieces()  {
        for (int playerId = 0;playerId < PLAYERS_AMOUNT;playerId++)
        {
            // Spawn chess pieces for each player.
            // We retrieve the desired Player instance by making usage
            // of the playerId.
            Player player = (playerId == 0) ? getPlayer1() : getPlayer2();
            // Row for the pawns of the player.
            int pawnsRow = (playerId * Board.CHESS_PREVIOUS_TOP_PAWNS_ROW) + 1;
            // Row for the king of the player.
            int kingRow = playerId * Board.CHESS_TOP_KING_ROW;
            for (int column = 0;column < Board.CHESS_COLUMN_COUNT;column++)
            {
                // Spawn piece whose type depends on the column of the piece.
                // Pawn piece.
                ChessPiece pawn = new ChessPiece(column,player, ChessPiece.PieceType.PAWN,column,pawnsRow);
                // Adds the pawn piece to the game board.
                getGameBoard().addPieceToCell(pawn,column,pawnsRow);
                // Number of the piece on the king's row - (0 or 1).
                int pieceNumber = column / 4;
                // Piece on the king's row.
                ChessPiece kingsRowPiece = new ChessPiece(pieceNumber,player,
                        ChessPiece.getInitialPieceTypeForColumnOnKingsRow(column),column,kingRow);
                // Adds the piece to the game board.
                getGameBoard().addPieceToCell(kingsRowPiece,column,kingRow);
            }
        }
    }

    //
    //	Checks whether or not, there has been a checkmate.
    //
    public boolean isCheckMate(Player playerMakingTheMove)  {
        // Select player who is not making the move.
        Player inactivePlayer = (playerMakingTheMove == getPlayer1()) ? getPlayer2() : getPlayer1();
        // Retrieve king piece of the inactive player.
        ChessPiece piece = getGameBoard().getKingOfPlayer(inactivePlayer);
        // There must be a king piece for the inactive player on the board.
        if (piece == null)
        {
            throw new IllegalStateException("Could not find king piece of the inactive player.");
        }

        return (!PieceDisplacement.hasKingAnyMovementsLeft(board, piece));
    }

    // If king has not any movements left, then there is a checkmate.
    //
    // Starts the match by spawning the chess pieces, and assigning
    // the turn to a player.
    //
    public void start()  {
        // Initializes the instance of match result.
        initializeMatchResult();
        // Spawn the chess pieces on the board.
        spawnChessPieces();
        // Set the player whose turn it's occuring now.
        setTurnOfPlayer(getPlayer1());
        // Inform the controller that the match has started.
        controller.onMatchStart(this);
        // Runs the logic of the turn.
        runTurn();
    }

    //
    // Initializes the instance of Match Result.
    //
    private void initializeMatchResult()  {
        // The match has started now.
        // Sets the name of the player 1.
        // Sets the name of the player 2.
        setResult(new MatchResult(-1, player1.getName(), player2.getName(),
                -1, new Date(System.currentTimeMillis()), null));
    }

    //
    //	Checks whether or not there's a next turn.
    //
    public boolean hasNextTurn()  {
        return !(finished);
    }

    // If the match has not finished, then there's a next turn.
    //
    //	Executes the following turn.
    //
    public void runTurn()  {
        // We cannot run the turn, if the match is finished already.
        if (!finished)
        {
            // Inform the controller in order to handle the turn.
            controller.onPlayerTurn(turnOfPlayer);
            // Change the player who has got the turn.
            setTurnOfPlayer((getTurnOfPlayer() == getPlayer1()) ? getPlayer2() : getPlayer1());
        }
         
    }

    //
    //	Handles the movement requested by a player on its turn.
    //
    public void handleMovement(PieceDisplacement movement)  {
        // Apply the movement to the game board.
        getGameBoard().applyMovement(movement);
        // If it's checkmate then handele the victory of the player.
        if (isCheckMate(movement.getSourcePlayer()))
        {
            int resultType = getResultTypeFromWinner(movement.getSourcePlayer());
            finishMatch(resultType);
            controller.onPlayerWins(movement.getSourcePlayer());
        }
        else
        {
            // Run turn in the contrary case.
            runTurn();
        } 
    }

    //
    // Returns the type of match end.
    //
    private int getResultTypeFromWinner(Player winner)  {
        if (winner == getPlayer1())
        {
            return MatchResult.PLAYER_1_VICTORY;
        }
        else if (winner == getPlayer2())
        {
            return MatchResult.PLAYER_2_VICTORY;
        }
        else
        {
            return MatchResult.DRAW;
        }  
    }

    // If there's no winner, then it's a draw.
    //
    // Updates the match status to finished.
    //
    private void finishMatch(int resultType)  {
        finished = true;
        // Fills the match result with the required details.
        getResult().setEndedAt(Calendar.getInstance().getTime());
        getResult().setResultType(resultType);
        // Inform the match wrapper that the match has ended.
        wrapper.onMatchEnds();
    }

}


