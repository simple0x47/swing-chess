package chess.gameplay;

import chess.gameplay.world.Board;
import chess.gameplay.world.BoardCell;
import chess.gameplay.world.Player;

// Interaction made by the player within a match.
public class PlayerBoardInteraction   
{
    // Instance of Board being used for the match.
    private final Board gameBoard;

    // Player who created the interaction.
    private Player sourcePlayer;

    // Property pointing to the player who has created the interaction.
    // Clears all interactions if a new player is set.
    public Player getSourcePlayer()  {
        return sourcePlayer;
    }

    public void setSourcePlayer(Player value)  {
        // Sets the member to the passed value.
        sourcePlayer = value;
        // Clears the interactions made by the previous player.
        clearInteractions();
    }

    // First position selection made by the player.
    private PlayerBoardCellSelection firstSelection;
    public PlayerBoardCellSelection getFirstSelection() {
        return firstSelection;
    }

    public void setFirstSelection(PlayerBoardCellSelection value) {
        firstSelection = value;
    }

    // Second position selection made by the player.
    private PlayerBoardCellSelection secondSelection;
    public PlayerBoardCellSelection getSecondSelection() {
        return secondSelection;
    }

    public void setSecondSelection(PlayerBoardCellSelection value) {
        secondSelection = value;
    }

    //
    // Initialization of the instance by making usage
    // of the specified Board.
    //
    public PlayerBoardInteraction(Board gameBoard)  {
        this.gameBoard = gameBoard;
    }

    //
    // Handles the selection of a certain position within the game board.
    //
    public void selectedColumnAndRow(int column, int row)  {
        PlayerBoardCellSelection selection = new PlayerBoardCellSelection(column,row);
        if (getFirstSelection() != null)
        {
            if (getSecondSelection() == null)
            {
                // The second selection can be whatever position within
                // the game board.
                setSecondSelection(selection);
            }
            else
            {
                // Checks the created interaction whether it is a valid
                // movement or not, and after that clears the interactions.
                onInteractionIsFull(column,row);
            } 
        }
        else
        {
            // The first selection must be a chess piece.
            if (isSelectionAPieceOfThePlayer(selection))
            {
                setFirstSelection(selection);
            }
             
        } 
    }

    //
    // Clears the interaction and repeats the selection whenever the
    // interaction has been filled.
    //
    private void onInteractionIsFull(int lastSelectionColumn, int lastSelectionRow)  {
        clearInteractions();
        selectedColumnAndRow(lastSelectionColumn,lastSelectionRow);
    }

    //
    // Restores the selections to null.
    //
    private void clearInteractions()  {
        setFirstSelection(null);
        setSecondSelection(null);
    }

    //
    // Checks whether or not the selection is a piece, whose owner,
    // its the player making the movement.
    //
    private boolean isSelectionAPieceOfThePlayer(PlayerBoardCellSelection selection)  {
        BoardCell cell = gameBoard.getCell(selection.getColumn(),selection.getRow());
        // Return whether or not there's a piece whose owner is the player
        // making the selection.
        if (cell.hasPiece())
        {
            return (cell.getPiece().getOwner() == getSourcePlayer());
        }
        else
        {
            return false;
        } 
    }

    //
    // Checks if the interaction has been filled.
    //
    public boolean isInteractionFull()  {
        if (getFirstSelection() != null)
        {
            if (getSecondSelection() != null)
            {
                return true;
            }
             
        }
         
        return false;
    }
}