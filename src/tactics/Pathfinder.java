package tactics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Pathfinder {
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public Map<Position, Integer> getReachableTiles(Unit unit, GameMap map) {
        Queue<Position> distances = new LinkedList<>();
        Map<Position, Integer> costSoFar = new HashMap<>();

        costSoFar.put(unit.position, 0);
        distances.add(unit.position);

        while (!distances.isEmpty()) {
            Position current = distances.poll();
            for (int[] dir : directions) {
                int newCol = current.col + dir[0];
                int newRow = current.row + dir[1];
                if (map.isInBounds(newRow, newCol)) {
                    int newCost = costSoFar.get(current) + map.movementCostAt(newRow, newCol);
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


