package chess.gameplay;

import chess.gameplay.world.Board;
import chess.gameplay.world.ChessPiece;
import chess.gameplay.world.Player;

// Communicating interface between the presentation layer and the
// business layer.
public abstract class GameController {
    // Interaction holder used for storing the selections made of a player.
    private PlayerBoardInteraction playerInteraction;
    // Instance of match being played right now.
    private Match onGoingMatch;

    // Default empty constructor.
    public GameController() {
    }

    public PlayerBoardInteraction getPlayerInteraction() {
        return playerInteraction;
    }

    public void setPlayerInteraction(PlayerBoardInteraction value) {
        playerInteraction = value;
    }

    protected Match getOnGoingMatch() {
        return onGoingMatch;
    }

    protected void setOnGoingMatch(Match value) {
        onGoingMatch = value;
    }

    //
    // Called when the match has been started.
    //
    public void onMatchStart(Match match) {
        // Initialization of the properties.
        setPlayerInteraction(new PlayerBoardInteraction(match.getGameBoard()));
        setOnGoingMatch(match);
        // Call for implementation dependent match start handling.
        handleMatchStart();
    }

    //
    // Presentation dependent handling of the start of the match.
    //
    public abstract void handleMatchStart();

    //
    // Updates the player making the interactions.
    //
    public void onPlayerTurn(Player player) {
        getPlayerInteraction().setSourcePlayer(player);
        // Calls for the presentation dependent handling of the turn.
        handlePlayerTurn(player);
    }

    //
    // Handles the turn of the passed player.
    //
    public abstract void handlePlayerTurn(Player player);

    //
    // Calls for the presentation dependent handling of a player's victory.
    //
    public void onPlayerWins(Player winner) {
        handleVictory(winner);
    }

    //
    // Handles the victory of the passed player.
    //
    public abstract void handleVictory(Player winner);

    //
    // Applies the current player interaction to the game.
    //
    public void applyPlayerInteraction() {
        // Check if the interaction it's full, otherwise we cannot apply it.
        if (getPlayerInteraction().isInteractionFull()) {
            // Retrieve the neccessary data.
            Board gameBoard = getOnGoingMatch().getGameBoard();
            PlayerBoardCellSelection pieceCell = getPlayerInteraction().getFirstSelection();
            ChessPiece selectedPiece = gameBoard.getCell(pieceCell.getColumn(), pieceCell.getRow()).getPiece();
            PlayerBoardCellSelection destinationCell = getPlayerInteraction().getSecondSelection();
            // Check if the interaction creates a legal movement, and apply
            // it if it does.
            if (PieceDisplacement.isMovementLegal(gameBoard, selectedPiece, getPlayerInteraction().getSourcePlayer(), destinationCell.getColumn(), destinationCell.getRow())) {
                // Create the movement.
                PieceDisplacement movement = new PieceDisplacement(selectedPiece.getId(), getPlayerInteraction().getSourcePlayer(), destinationCell.getColumn(), destinationCell.getRow());
                // Apply the movement to the match.
                getOnGoingMatch().handleMovement(movement);
            } else {
                // Inform the player that the movement is invalid.
                handleInvalidPlayerInteraction();
            }
        } else {
            throw new IllegalStateException("Tried to apply a non-completed interaction.");
        }
    }

    //
    // Informs the player for an invalid interaction.
    //
    public abstract void handleInvalidPlayerInteraction();
}