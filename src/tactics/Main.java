import tactics.GamePanel;

import javax.swing.*;

void main() throws IOException, URISyntaxException {
    JFrame gameFrame = new JFrame("Tactics Game");
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GamePanel gamePanel = new GamePanel();
    gameFrame.add(gamePanel);
    gameFrame.pack();

    gameFrame.setVisible(true);
}

