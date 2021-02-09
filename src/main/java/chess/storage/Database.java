/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.storage;

import chess.gameplay.MatchResult;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import org.sqlite.SQLiteConnection;

/**
 * Database wrapper of a SQLite connection.
 * @author Gabriel
 */
public class Database {
    private static final String DATABASE_NAME = "chess_db.db3";
        
    private Connection connection;
    
    /**
     * Creates the connection, creating the matchResults table if necessary.
     */
    public Database() {
        String url = "jdbc:sqlite:./" + DATABASE_NAME;
        
        try {
            this.connection = DriverManager.getConnection(url);
            
            initializeTable();
        } catch (SQLException e) {
           System.err.println(e.getMessage());
        }
    }
    
    /**
     * Creation of the SQL table.
     */
    public void initializeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS matchResults (\n"
                + "     id integer PRIMARY KEY,\n"
                + "     player1Name text NOT NULL,\n"
                + "     player2Name text NOT NULL,\n"
                + "     resultType integer NOT NULL,\n"
                + "     startedAt integer NOT NULL,\n"
                + "     finishedAt integer NOT NULL\n"
                + ");";
        
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            System.err.println("Failed to store data.");
        }
    }
    
    /**
     * Inserts the passed match result into the database.
     * @param result result being inserted.
     */
    public void insertMatchResult(MatchResult result) {
        String sql = String.format("INSERT INTO matchResults VALUES (%d, '%s', '%s', %d, %d, %d);",
                result.getID(),
                result.getPlayer1Name(),
                result.getPlayer2Name(),
                result.getResultType(),
                result.getStartedAt().getTime(),
                result.getEndedAt().getTime());
        
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            System.err.println("Failed to insert match result.");
        }
    }
    
    /**
     * Retrieves all match results from the database.
     * @return ArrayList containing the match results, empty if none are found.
     */
    public ArrayList<MatchResult> getMatchResults() {
        String sql = String.format("SELECT * FROM matchResults;");
        
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            
            ArrayList<MatchResult> results = new ArrayList();
            
            while (result.next()) {
                MatchResult matchResult = new MatchResult(
                result.getInt("id"),
                result.getString("player1Name"),
                result.getString("player2Name"),
                result.getInt("resultType"),
                new Date(result.getLong("startedAt")),
                new Date(result.getLong("finishedAt")));
                
                results.add(matchResult);
            }
            
            return results;
        } catch (SQLException e) {
            System.err.println("Failed to insert match result.");
            
            return null;
        }
    }
    
    /**
     * Closes the connection to the database.
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Failed to close connection.");
        }
    }
}
