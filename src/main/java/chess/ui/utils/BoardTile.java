/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ui.utils;

import java.awt.Color;
import javax.swing.JButton;

/**
 * Extensions of JButton in order to apply the logic of a chess board's tile.
 * @author Gabriel
 */
public class BoardTile extends JButton {
    public static final String EMPTY_TILE = "tile";
    
    private static final Color DEFAULT_BLACK_TILE = new Color(41, 41, 41);
    private static final Color DEFAULT_WHITE_TILE = new Color(235, 235, 235);
    
    public final int column;
    public final int row;
    
    public BoardTile(int column, int row) {
        this.column = column;
        this.row = row;
        
        setTileColorRelativeToPosition();
        setEmpty();
    }
    
    public void setTileColorRelativeToPosition() {
        boolean isSumEven = ((column + row) % 2 == 0);
        
        if (isSumEven) {
            setBackground(DEFAULT_BLACK_TILE);
        } else {
            setBackground(DEFAULT_WHITE_TILE);
        }
    }
    
    public void setEmpty() {
        setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/chess/" + EMPTY_TILE + ".png")));
    }
    
    public void setPieceOn(String pieceName) {
        setIcon(new javax.swing.ImageIcon(getClass()
                .getResource("/chess/" + pieceName + ".png")));
    }
}
