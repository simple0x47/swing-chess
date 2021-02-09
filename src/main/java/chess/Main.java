package chess;

import chess.gameplay.MatchWrapper;
import chess.ui.GameWindow;

/**
 * Entry point of the Chess game.
 * @author Gabriel
 */
public class Main {
    /**
     * Bridge between the business layer and the presentation one.
     */
    private static MatchWrapper matchWrapper;
    
    public static void main(String[] args) { 
        getMatchWrapper();
        new GameWindow().setVisible(true);
    }
    
    public static MatchWrapper getMatchWrapper() {
        if (matchWrapper == null) {
            matchWrapper = new MatchWrapper();
        }
        
        return matchWrapper;
    }
}
