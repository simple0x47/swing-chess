package chess.gameplay;

import chess.storage.Database;
import java.util.ArrayList;

/**
 * Manages a match, and provides an interface to the presentation layer.
 * 
 * @author Gabriel
 */
public class MatchWrapper {
    /**
     * On going match.
     */
    public Match onGoingMatch;
    
    /**
     * Instance of the database connection wrapper.
     */
    private final Database database;
    
    /**
     * All match results being held within the database.
     */
    private final ArrayList<MatchResult> results;

    /**
     * Calls for the creation of the database's connection and exits
     * if it fails.
     */
    public MatchWrapper() {
        database = new Database();
        
        results = database.getMatchResults();
        
        if (results == null) {
            System.exit(1);
        }
    }
    
    /**
     * Starts a new match.
     * 
     * @param controller implementation of GameController.
     * @param player1Name Name of the player with white pieces.
     * @param player2Name Name of the player with black pieces.
     */
    public void startNewMatch(GameController controller, String player1Name, String player2Name) {
        onGoingMatch = new Match(this, controller, player1Name, player2Name);

        onGoingMatch.start();
    }

    /**
     * Called by Match, when the match ends, adding it to the database.
     */
    public void onMatchEnds() {
        MatchResult result = onGoingMatch.getResult();
        
        result.setID(results.size());
        
        results.add(result);
        database.insertMatchResult(result);
    }
    
    public ArrayList<MatchResult> getResults() {
        return results;
    }
}
