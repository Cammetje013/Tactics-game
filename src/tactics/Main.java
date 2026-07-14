import tactics.GamePanel;

import javax.swing.JFrame;

void main() throws IOException, URISyntaxException {
    JFrame gameFrame = new JFrame ("Tactics Game");
    gameFrame.setSize(640, 1236);
    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GamePanel gamePanel = new GamePanel();
    gameFrame.add(gamePanel);

    gameFrame.setVisible(true);
}

