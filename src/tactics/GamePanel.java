package tactics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;


public class GamePanel extends JPanel implements MouseListener {
    private final int rows = 15;
    private final int cols = 8;
    private final int tileSize = 80;
    Unit selectedUnit = null;
    GameMap map = GameMap.loadFrom("resources/maps/map1");
    List<Unit> units = new ArrayList<>();
    Map<Position, Integer> reachableTiles = new HashMap<>();
    Pathfinder pathfinder = new Pathfinder();

    public GamePanel() throws IOException, URISyntaxException {
        //Unit creation
        units.add(new Unit(UnitTypes.KNIGHT, new Position(5, 4), Teams.PLAYER));
        units.add(new Unit(UnitTypes.MAGE, new Position(6, 7), Teams.PLAYER));
        units.add(new Unit(UnitTypes.RANGER, new Position(2, 7), Teams.PLAYER));

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * tileSize;
                int y = row * tileSize;
                g.setColor(map.tiles()[row][col].terrain.colour);
                g.fillRect(x, y, tileSize, tileSize);
            }
        }
        for (Unit unit : units) {
            int x = unit.position.col * tileSize;
            int y = unit.position.row * tileSize;
            if (unit.unitType == UnitTypes.KNIGHT) g.setColor(Color.DARK_GRAY);
            else if (unit.unitType == UnitTypes.MAGE) g.setColor(Color.PINK);
            else g.setColor(Color.BLACK);
            g.fillOval(x + 20, y + 20, tileSize / 2, tileSize / 2);
        }

        if (selectedUnit != null) {
         g.setColor(Color.BLACK);
         g.drawRect(selectedUnit.position.col * tileSize, selectedUnit.position.row * tileSize, tileSize, tileSize);
        }

        g.setColor(new Color(255, 140, 0, 120));
        for (Position pos : reachableTiles.keySet()){
            int x = pos.col * tileSize;
            int y = pos.row * tileSize;
            g.fillRect(x, y, tileSize, tileSize);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = e.getX() / tileSize;
        int row = e.getY() / tileSize;
        selectedUnit = getUnitAt(col, row);
        System.out.println("Clicked col: " + col + "\nrow: " + row);
        if (selectedUnit != null)
            reachableTiles = pathfinder.getReachableTiles(selectedUnit, map);
        else reachableTiles = new HashMap<>();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public Unit getUnitAt(int col, int row) {
        for (Unit unit : units) {
            if (unit.position.col == col && unit.position.row == row) return unit;
        }
        return null;
    }
}
