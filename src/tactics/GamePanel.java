package tactics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


public class GamePanel extends JPanel implements MouseListener {
    private final int tileSize = 80;
    private Mode mode = Mode.NONE;

    Unit selectedUnit = null;
    LevelData levelData = LevelLoader.load("resources/maps/map1");
    Map<Position, Integer> reachableTiles = new HashMap<>();
    Pathfinder rangeFinder = new Pathfinder();

    public GamePanel() throws IOException, URISyntaxException {
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < levelData.map().rows(); row++) {
            for (int col = 0; col < levelData.map().cols(); col++) {
                int x = col * tileSize;
                int y = row * tileSize;
                g.setColor(levelData.map().tiles()[row][col].terrain.colour);
                g.fillRect(x, y, tileSize, tileSize);
            }
        }
        for (Unit unit : levelData.roster().units()) {
            int x = unit.position.col * tileSize;
            int y = unit.position.row * tileSize;
            g.setColor(unit.unitType.colour);
            g.fillOval(x + 20, y + 20, tileSize / 2, tileSize / 2);
        }

        g.setColor(new Color(255, 140, 0, 120));
        for (Position pos : reachableTiles.keySet()) {
            int x = pos.col * tileSize;
            int y = pos.row * tileSize;
            g.fillRect(x, y, tileSize, tileSize);
        }

        if (selectedUnit != null) {
            g.setColor(Color.BLACK);
            g.drawRect(selectedUnit.position.col * tileSize, selectedUnit.position.row * tileSize, tileSize, tileSize);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(levelData.map().cols() * tileSize, levelData.map().rows() * tileSize);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = e.getX() / tileSize;
        int row = e.getY() / tileSize;
        if (mode == Mode.NONE) {
            selectedUnit = getUnitAt(col, row);
            if (selectedUnit != null)
                new UnitActionMenu(() -> {
                    mode = Mode.MOVE;
                    reachableTiles = rangeFinder.getReachableTiles(selectedUnit, levelData.map());
                    repaint();
                },
                        () -> {
                            mode = Mode.ATTACK;
                            reachableTiles = rangeFinder.getAttackRange(selectedUnit, levelData.map());
                            repaint();
                        }
                ).show(this, e.getX(), e.getY());
            else reachableTiles = new HashMap<>();
            repaint();
        } else if (mode == Mode.MOVE) {
            if (selectedUnit != null && getUnitAt(col, row) == null && reachableTiles.containsKey(new Position(col, row))) {
                selectedUnit.moveTo(new Position(col, row));
                reachableTiles = new HashMap<>();
                selectedUnit = null;
                mode = Mode.NONE;
            }
        } else if (mode == Mode.ATTACK) {
            Unit enemyUnit = getUnitAt(col, row);
            if (enemyUnit != null) {
                if (!enemyUnit.team.equals(selectedUnit.team) && reachableTiles.containsKey(new Position(col, row))) {
                    selectedUnit.attackUnit(enemyUnit);
                    levelData.roster().removeDeadUnits();
                    reachableTiles = new HashMap<>();
                    selectedUnit = null;
                    mode = Mode.NONE;
                    repaint();
                }
            } else {
                reachableTiles = new HashMap<>();
                mode = Mode.NONE;
                repaint();
            }
        }
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
        for (Unit unit : levelData.roster().units()) {
            if (unit.position.col == col && unit.position.row == row) return unit;
        }
        return null;
    }
}
