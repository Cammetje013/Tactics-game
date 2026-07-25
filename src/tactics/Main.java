import tactics.ui.GamePanel;

import javax.swing.*;
import java.awt.BorderLayout;

void main() throws IOException, URISyntaxException {
    JFrame gameFrame = new JFrame("Tactics Game");
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GamePanel gamePanel = new GamePanel();

    JButton endTurnButton = new JButton("End Turn");
    endTurnButton.addActionListener(e -> gamePanel.endTurn());

    gameFrame.setLayout(new BorderLayout());
    gameFrame.add(gamePanel, BorderLayout.CENTER);
    gameFrame.add(endTurnButton, BorderLayout.SOUTH);
    gameFrame.pack();

    gameFrame.setVisible(true);
}

