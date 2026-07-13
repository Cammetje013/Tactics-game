package tactics;

import javax.swing.*;
import java.awt.*;



public class GamePanel extends JPanel {
    private final int rows = 15;
    private final int cols = 8;
    private final int tileSize = 80;
    Tile[][] map = new Tile[cols][rows];

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int col = 0 ; col < cols ; col++){
            for(int row = 0 ; row < rows ; row++){
                if (row == 0) map[col][row] = new Tile(TerrainTypes.MOUNTAIN);
                else if (col == 0) map[col][row] = new Tile(TerrainTypes.MOUNTAIN);
                else map[col][row] = new Tile(TerrainTypes.PLAINS);


                int x = col * tileSize;
                int y = row * tileSize;
                g.setColor(map[col][row].terrain.colour);
                g.fillRect(x, y, tileSize, tileSize);
            }
        }
    }
}
