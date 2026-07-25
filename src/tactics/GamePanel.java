package tactics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
    private final int tileSize = 64;
    private Mode mode = Mode.NONE;

    Unit selectedUnit = null;
    LevelData levelData = LevelLoader.load("resources/maps/map2");
    Map<Position, Integer> reachableTiles = new HashMap<>();
    Pathfinder rangeFinder = new Pathfinder();
    TileSet tileSet = TileSet.loadFrom("resources/tilemaps/Tileset.png");
    Position hoveredTile = null;

    public GamePanel() throws IOException, URISyntaxException {
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private int originX() {
        return levelData.map().rows() * (tileSize / 2);
    }

    private int isoX(int col, int row) {
        return (col - row) * (tileSize / 2) + originX();
    }

    private int isoY(int col, int row) {
        return (col + row) * (tileSize / 4);
    }

    private int baseWidth() {
        int cols = levelData.map().cols();
        int rows = levelData.map().rows();
        return (cols + rows - 2) * (tileSize / 2) + tileSize;
    }

    private int baseHeight() {
        int cols = levelData.map().cols();
        int rows = levelData.map().rows();
        return (cols + rows - 2) * (tileSize / 4) + tileSize;
    }

    private double scale() {
        if (getWidth() == 0 || getHeight() == 0) return 1.0;
        double sx = getWidth() / (double) baseWidth();
        double sy = getHeight() / (double) baseHeight();
        return Math.min(sx, sy);
    }

    private Position screenToGrid(int px, int py) {
        double scale = scale();
        double x = px / scale;
        double y = py / scale;
        double halfW = tileSize / 2.0;
        double halfH = tileSize / 4.0;
        double colMinusRow = (x - originX()) / halfW;
        double colPlusRow = (y - halfH) / halfH;
        int col = (int) Math.round((colMinusRow + colPlusRow) / 2);
        int row = (int) Math.round((colPlusRow - colMinusRow) / 2);
        return new Position(col, row);
    }

    private Polygon diamondAt(int col, int row) {
        int x = isoX(col, row);
        int y = isoY(col, row);
        Polygon diamond = new Polygon();
        diamond.addPoint(x, y);
        diamond.addPoint(x + tileSize / 2, y + tileSize / 4);
        diamond.addPoint(x, y + tileSize / 2);
        diamond.addPoint(x - tileSize / 2, y + tileSize / 4);
        return diamond;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        double scale = scale();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.scale(scale, scale);
        for (int row = 0; row < levelData.map().rows(); row++) {
            for (int col = 0; col < levelData.map().cols(); col++) {
                TerrainTypes terrain = levelData.map().tiles()[row][col].terrain;
                int x = isoX(col, row) - tileSize / 2;
                int y = isoY(col, row) - terrain.spriteTopOffset;
                g.drawImage(tileSet.getImage(terrain), x, y, tileSize, tileSize, null);
            }
        }

        g.setColor(new Color(255, 140, 0, 120));
        for (Position pos : reachableTiles.keySet()) {
            g.fillPolygon(diamondAt(pos.col, pos.row));
        }

        if (selectedUnit != null) {
            g.setColor(Color.BLACK);
            g.drawPolygon(diamondAt(selectedUnit.position.col, selectedUnit.position.row));
        }

        if (hoveredTile != null && levelData.map().isInBounds(hoveredTile.row, hoveredTile.col)) {
            g.setColor(Color.WHITE);
            g.drawPolygon(diamondAt(hoveredTile.col, hoveredTile.row));
        }

        for (Unit unit : levelData.roster().units()) {
            int x = isoX(unit.position.col, unit.position.row) - tileSize / 4;
            int y = isoY(unit.position.col, unit.position.row);
            g.setColor(unit.unitType.colour);
            g.fillOval(x, y, tileSize / 2, tileSize / 2);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(baseWidth(), baseHeight());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Position clicked = screenToGrid(e.getX(), e.getY());
        int col = clicked.col;
        int row = clicked.row;
        if (mode == Mode.NONE) {
            selectedUnit = getUnitAt(col, row);
            if (selectedUnit != null) {
                boolean canMove = !selectedUnit.hasMoved;
                boolean canAttack = !selectedUnit.hasAttacked;
                new UnitActionMenu(() -> {
                    mode = Mode.MOVE;
                    reachableTiles = rangeFinder.getReachableTiles(selectedUnit, levelData.map());
                    repaint();
                },
                        () -> {
                            mode = Mode.ATTACK;
                            reachableTiles = rangeFinder.getAttackRange(selectedUnit, levelData.map());
                            repaint();
                        },
                        canMove, canAttack
                ).show(this, e.getX(), e.getY());
            } else reachableTiles = new HashMap<>();
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
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        hoveredTile = screenToGrid(e.getX(), e.getY());
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    public Unit getUnitAt(int col, int row) {
        for (Unit unit : levelData.roster().units()) {
            if (unit.position.col == col && unit.position.row == row) return unit;
        }
        return null;
    }
}
