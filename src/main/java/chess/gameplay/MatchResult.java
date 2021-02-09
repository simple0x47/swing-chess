package chess.gameplay;

import java.util.Date;

// Contains the relevant information for the result of a match.
public class MatchResult   
{
    // Code used when player 1 wins.
    public static final int PLAYER_1_VICTORY = 0;
    // Code used when player 2 wins.
    public static final int PLAYER_2_VICTORY = 1;
    // Code used when the match ends with a draw.
    public static final int DRAW = 2;
    // ID used within the database.
    private int id;

    public MatchResult() {
        
    }
    
    public MatchResult(int id, String player1Name, String player2Name,
            int resultType, Date startedAt, Date endedAt) {
        this.id = id;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.resultType = resultType;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
    
    public int getID() {
        return id;
    }

    public void setID(int value) {
        id = value;
    }

    // Name of the first player.
    private String player1Name;

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String value) {
        player1Name = value;
    }

    // Name of the second player.
    private String player2Name;

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String value) {
        player2Name = value;
    }

    // Type representing how the match ended.
    private int resultType;
    public int getResultType() {
        return resultType;
    }

    public void setResultType(int value) {
        resultType = value;
    }

    // When the match has started.
    private Date startedAt;

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date value) {
        startedAt = value;
    }

    // When the match has ended
    private Date endedAt;

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date value) {
        endedAt = value;
    }

    // Thumbnail file shown on screen.
    private String resultThumbnail;

    public String getResultThumbnail() {
        return resultThumbnail;
    }

    public void setResultThumbnail(String value) {
        resultThumbnail = value;
    }
}


