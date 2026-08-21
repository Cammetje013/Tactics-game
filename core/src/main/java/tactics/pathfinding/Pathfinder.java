package tactics.pathfinding;

import tactics.model.GameMap;
import tactics.model.Position;
import tactics.model.Unit;
import tactics.model.UnitRoster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Pathfinder {
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private boolean isBlocked(int row, int col, GameMap map, UnitRoster roster, Unit mover) {
        if (!map.isInBounds(row, col)) return true;
        if (map.blocksLineOfFire(row, col)) return true;
        Unit occupant = roster.getUnitAt(col, row);
        return occupant != null && occupant != mover && !Unit.isDead(occupant);
    }

    // movement is a single straight cardinal-direction ray per turn, same rule as before -
    // this only adds unit-occupancy blocking on top of the original terrain-only check.
    public Map<Position, Integer> getReachableTiles(Unit unit, GameMap map, UnitRoster roster) {
        Map<Position, Integer> tilesInRange = new HashMap<>();
        for (int[] dir : directions) {
            for (int step = 1; step <= unit.unitType.movement; step++) {
                int newRow = unit.position.row + dir[1] * step;
                int newCol = unit.position.col + dir[0] * step;
                if (isBlocked(newRow, newCol, map, roster, unit)) break;
                tilesInRange.put(new Position(newCol, newRow), step);
            }
        }
        return tilesInRange;
    }

    // destination always comes from getReachableTiles, so it's guaranteed to sit on a
    // single straight line from the unit's position - just walk that line tile by tile.
    public List<Position> getPath(Unit unit, Position destination, GameMap map, UnitRoster roster) {
        List<Position> path = new ArrayList<>();
        int stepCol = Integer.compare(destination.col, unit.position.col);
        int stepRow = Integer.compare(destination.row, unit.position.row);
        int col = unit.position.col;
        int row = unit.position.row;
        while (col != destination.col || row != destination.row) {
            col += stepCol;
            row += stepRow;
            path.add(new Position(col, row));
        }
        return path;
    }

    /**
     * BFS distance from every tile reachable from `target` back to `target` itself,
     * used so CPU behaviors can pick a move tile by real walking distance (routes
     * around walls/units) instead of straight-line distance.
     */
    public Map<Position, Integer> getDistanceMap(Position target, GameMap map, UnitRoster roster, Unit mover) {
        Map<Position, Integer> costSoFar = new HashMap<>();
        Queue<Position> frontier = new LinkedList<>();
        costSoFar.put(target, 0);
        frontier.add(target);

        while (!frontier.isEmpty()) {
            Position current = frontier.poll();
            int currentCost = costSoFar.get(current);
            for (int[] dir : directions) {
                int newCol = current.col + dir[0];
                int newRow = current.row + dir[1];
                if (isBlocked(newRow, newCol, map, roster, mover)) continue;
                Position next = new Position(newCol, newRow);
                int newCost = currentCost + 1;
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    costSoFar.put(next, newCost);
                    frontier.add(next);
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
