/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ui;

import chess.gameplay.GameController;
import chess.gameplay.world.Player;
import chess.ui.utils.BoardTileSynchronizer;
import chess.ui.utils.BoardTile;
import javax.swing.JOptionPane;

/**
 * Game controller made for Swing.
 * 
 * @author Gabriel
 */
public class SwingGameController extends GameController {

    private final BoardPanel boardPanel;
    private final BoardTileSynchronizer synchronizer;
    
    public SwingGameController(BoardPanel boardPanel, BoardTileSynchronizer synchronizer) {
        this.boardPanel = boardPanel;
        this.synchronizer = synchronizer;
    }
    
    @Override
    public void handleMatchStart() {
        synchronizer.synchronizeToChessBoard(getOnGoingMatch().getGameBoard());
    }

    @Override
    public void handlePlayerTurn(Player player) {
        synchronizer.synchronizeToChessBoard(getOnGoingMatch().getGameBoard());
        
        JOptionPane.showMessageDialog(null,
                "Now it's your turn: " + player.getName(),
                "My turn!",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void handleVictory(Player winner) {
        // Let the players know if they've won.
        JOptionPane.showMessageDialog(null,
                "You've won the match " + winner.getName(), 
                "Congratulations!",
                JOptionPane.INFORMATION_MESSAGE);
        
        showMainMenu();
    }
    
    private void showMainMenu() {
        boardPanel.gameWindow.remove(boardPanel);
        
        MainMenu mainMenu = new MainMenu(boardPanel.gameWindow);
        boardPanel.gameWindow.add(mainMenu);
        boardPanel.gameWindow.pack();
    }

    @Override
    public void handleInvalidPlayerInteraction() {
        // Let the players know if they've made an invalid move.
        JOptionPane.showMessageDialog(null,
                "That movement is not legal.",
                "Wrong move!",
                JOptionPane.ERROR_MESSAGE);
    }
    
    public void onBoardTileClicked(BoardTile clickedTile) {
        getPlayerInteraction()
                .selectedColumnAndRow(clickedTile.column, clickedTile.row);
        
        if (getPlayerInteraction().isInteractionFull()) {
            applyPlayerInteraction();
        }
    }
}
