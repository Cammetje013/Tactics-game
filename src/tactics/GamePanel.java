package tactics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;


public class GamePanel extends JPanel implements MouseListener {
    private final int rows = 15;
    private final int cols = 8;
    private final int tileSize = 80;
    Unit selectedUnit = null;
    Tile[][] map = new Tile[rows][cols];
    List<Unit> units = new ArrayList<>();
    Map<Character, TerrainTypes> terrainLookup = new HashMap<>();
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    Map<Position, Integer> reachableTiles = new HashMap<>();

    public GamePanel() throws IOException, URISyntaxException {
        //Map creation
        terrainLookup.put('M', TerrainTypes.MOUNTAIN);
        terrainLookup.put('P', TerrainTypes.PLAINS);
        terrainLookup.put('W', TerrainTypes.WATER);
        terrainLookup.put('F', TerrainTypes.FOREST);

        List<String> lines =
                Files.readAllLines(Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/maps/map1")).toURI()));

        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(row).length(); col++) {
                map[row][col] = new Tile(terrainLookup.get(lines.get(row).charAt(col)));
            }
        }

        //Unit creation
        units.add(new Unit(UnitTypes.KNIGHT, new Position(5, 4), Teams.PLAYER));
        units.add(new Unit(UnitTypes.MAGE, new Position(6, 7), Teams.PLAYER));
        units.add(new Unit(UnitTypes.RANGER, new Position(2, 7), Teams.PLAYER));

        Map<Position, Integer> reachable = getReachableTiles(units.getFirst());
        System.out.println("Reachable tiles: " + reachable.size());

        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * tileSize;
                int y = row * tileSize;
                g.setColor(map[row][col].terrain.colour);
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
            reachableTiles = getReachableTiles(selectedUnit);
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

    public Map<Position, Integer> getReachableTiles(Unit unit) {
        Queue<Position> distances = new LinkedList<>();
        Map<Position, Integer> costSoFar = new HashMap<>();

        costSoFar.put(unit.position, 0);
        distances.add(unit.position);

        while (!distances.isEmpty()) {
            Position current = distances.poll();
            for (int[] dir : directions) {
                int newCol = current.col + dir[0];
                int newRow = current.row + dir[1];
                if (newCol >= 0 && newCol < cols && newRow >= 0 && newRow < rows) {
                    int newCost = costSoFar.get(current) + map[newRow][newCol].terrain.movementCost;
                    if (newCost <= unit.unitType.movement) {
                        Position neighbour = new Position(newCol, newRow);
                        if (!costSoFar.containsKey(neighbour) || newCost < costSoFar.get(neighbour)) {
                            costSoFar.put(neighbour, newCost);
                            distances.add(neighbour);
                        }
                    }
                }
            }
        }

        return costSoFar;
    }
}
