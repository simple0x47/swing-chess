/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.ui;

/**
 * Panel where players must insert their name.
 * @author Gabriel
 */
public class PlayerNamesIntroduction extends javax.swing.JPanel {

    private GameWindow gameWindow;
    
    /**
     * Creates new form PlayerNamesIntroduction
     */
    public PlayerNamesIntroduction(GameWindow gameWindow) {
        initComponents();
        
        this.gameWindow = gameWindow;
        
        playButton.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gameIcon = new javax.swing.JLabel();
        playersForm = new javax.swing.JPanel();
        player1 = new javax.swing.JLabel();
        player1Name = new javax.swing.JTextField();
        player2 = new javax.swing.JLabel();
        player2Name = new javax.swing.JTextField();
        playButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(640, 480));
        setMinimumSize(new java.awt.Dimension(640, 480));
        setLayout(new java.awt.BorderLayout());

        gameIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gameIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chess/draw.png"))); // NOI18N
        gameIcon.setToolTipText("");
        add(gameIcon, java.awt.BorderLayout.PAGE_START);

        playersForm.setLayout(new java.awt.GridLayout(2, 2));

        player1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        player1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chess/king_white.png"))); // NOI18N
        player1.setLabelFor(player1Name);
        player1.setText("NOMBRE JUGADOR 1:");
        player1.setToolTipText("");
        playersForm.add(player1);

        player1Name.setToolTipText("Nombre jugador 1");
        player1Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                player1NameKeyReleased(evt);
            }
        });
        playersForm.add(player1Name);

        player2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        player2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        player2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chess/king.png"))); // NOI18N
        player2.setText("NOMBRE JUGADOR 2:");
        playersForm.add(player2);

        player2Name.setToolTipText("Nombre jugador 2");
        player2Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                player2NameKeyReleased(evt);
            }
        });
        playersForm.add(player2Name);

        add(playersForm, java.awt.BorderLayout.CENTER);

        playButton.setText("JUGAR");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        add(playButton, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void player1NameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_player1NameKeyReleased
        // Allow the players to start the match if they've inserted any
        // character.
        if (arePlayersNameValid()) {
            playButton.setEnabled(true);
        }
    }//GEN-LAST:event_player1NameKeyReleased

    private void player2NameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_player2NameKeyReleased
        // Allow the players to start the match if they've inserted any
        // character.
        if (arePlayersNameValid()) {
            playButton.setEnabled(true);
        }
    }//GEN-LAST:event_player2NameKeyReleased

    private boolean arePlayersNameValid() {
        return  (!(player1Name.getText().isEmpty() ||
                player2Name.getText().isEmpty()));
    }
    
    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        // Hides the window, and proceeds to show the game board.
        gameWindow.remove(this);
        BoardPanel panel = new BoardPanel(gameWindow, player1Name.getText(), player2Name.getText());
        
        gameWindow.add(panel);
        gameWindow.pack();
    }//GEN-LAST:event_playButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel gameIcon;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel player1;
    private javax.swing.JTextField player1Name;
    private javax.swing.JLabel player2;
    private javax.swing.JTextField player2Name;
    private javax.swing.JPanel playersForm;
    // End of variables declaration//GEN-END:variables
}
