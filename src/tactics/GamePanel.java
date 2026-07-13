package tactics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GamePanel extends JPanel {
    private final int rows = 15;
    private final int cols = 8;
    private final int tileSize = 80;
    Tile[][] map = new Tile[cols][rows];
    List<Unit> friendlyUnits = new ArrayList<>();

    public GamePanel() {
        //Map creation
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                if (row == 0) map[col][row] = new Tile(TerrainTypes.MOUNTAIN);
                else if (col == 0) map[col][row] = new Tile(TerrainTypes.MOUNTAIN);
                else if (col > 5 && row > 5) map[col][row] = new Tile(TerrainTypes.FOREST);
                else if (col < 4 && row < 2) map[col][row] = new Tile(TerrainTypes.WATER);
                else if (col == 5 && row < 5) map[col][row] = new Tile(TerrainTypes.MOUNTAIN);
                else map[col][row] = new Tile(TerrainTypes.PLAINS);
            }
        }

        //Unit creation
        friendlyUnits.add(new Unit(UnitTypes.KNIGHT, new Position(5, 4), Teams.PLAYER));
        friendlyUnits.add(new Unit(UnitTypes.MAGE, new Position(6, 7), Teams.PLAYER));
        friendlyUnits.add(new Unit(UnitTypes.RANGER, new Position(2, 7), Teams.PLAYER));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                int x = col * tileSize;
                int y = row * tileSize;
                g.setColor(map[col][row].terrain.colour);
                g.fillRect(x, y, tileSize, tileSize);
            }
        }
        for (Unit unit : friendlyUnits){
            int x = unit.position.col * tileSize;
            int y = unit.position.row * tileSize;
            if (unit.unitType == UnitTypes.KNIGHT) g.setColor(Color.DARK_GRAY);
            else if (unit.unitType == UnitTypes.MAGE) g.setColor(Color.PINK);
            else g.setColor(Color.BLACK);
            g.fillOval(x + 20, y + 20, tileSize / 2, tileSize / 2);
        }
    }
}
