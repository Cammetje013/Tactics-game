package tactics.pathfinding;

import tactics.model.GameMap;
import tactics.model.Position;
import tactics.model.Unit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Pathfinder {
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public Map<Position, Integer> getReachableTiles(Unit unit, GameMap map) {
        Map<Position, Integer> tilesInRange = new HashMap<>();
        for (int[] dir : directions) {
            for (int step = 1; step <= unit.unitType.movement; step++) {
                int newRow = unit.position.row + dir[1] * step;
                int newCol = unit.position.col + dir[0] * step;
                if (!map.isInBounds(newRow, newCol)) break;
                if (map.blocksLineOfFire(newRow, newCol)) break;
                tilesInRange.put(new Position(newCol, newRow), step);
            }
        }
        return tilesInRange;
    }

    public Map<Position, Integer> getFloodFillRange(Unit unit, GameMap map) {
        Queue<Position> distances = new LinkedList<>();
        Map<Position, Integer> costSoFar = new HashMap<>();

        costSoFar.put(unit.position, 0);
        distances.add(unit.position);

        while (!distances.isEmpty()) {
            Position current = distances.poll();
            if (map.blocksLineOfFire(current.row, current.col)) continue;
            for (int[] dir : directions) {
                int newCol = current.col + dir[0];
                int newRow = current.row + dir[1];
                if (map.isInBounds(newRow, newCol)) {
                    int newCost = costSoFar.get(current) + 1;
                    if (newCost <= unit.unitType.range) {
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

    public Map<Position, Integer> getAttackRange(Unit unit, GameMap map) {
        Map<Position, Integer> tilesInRange = new HashMap<>();
        for (int[] dir : directions) {
            for (int step = 1; step <= unit.unitType.range; step++) {
                int newRow = unit.position.row + dir[1] * step;
                int newCol = unit.position.col + dir[0] * step;
                if (!map.isInBounds(newRow, newCol)) break;
                if (map.blocksLineOfFire(newRow, newCol)) break;
                tilesInRange.put(new Position(newCol, newRow), step);
            }
        }
        return tilesInRange;
    }
}


