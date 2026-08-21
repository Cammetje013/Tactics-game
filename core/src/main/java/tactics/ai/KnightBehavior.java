package tactics.ai;

import tactics.model.*;
import tactics.pathfinding.Pathfinder;

import java.util.List;
import java.util.Map;

public class KnightBehavior implements CpuBehavior {
    @Override
    public void act(Unit unit, GameState gameState, UnitRoster roster, GameMap map, Pathfinder pathfinder) {
        Unit best = null;
        int bestDistance = Integer.MAX_VALUE;
        for (Unit target : roster.units()) {
            if (target.team == unit.team || Unit.isDead(target)) continue;
            int distance = Math.abs(target.position.col - unit.position.col) + Math.abs(target.position.row - unit.position.row);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = target;
            }
        }
        if (best == null) return;

        Map<Position, Integer> reachableTiles = pathfinder.getReachableTiles(unit, map, roster);
        Map<Position, Integer> distanceToTarget = pathfinder.getDistanceMap(best.position, map, roster, unit);

        Position bestPosition = unit.position;
        int bestTileDistance = distanceToTarget.getOrDefault(unit.position, Integer.MAX_VALUE);

        for (Position candidate : reachableTiles.keySet()) {
            Integer distance = distanceToTarget.get(candidate);
            if (distance != null && distance < bestTileDistance) {
                bestTileDistance = distance;
                bestPosition = candidate;
            }
        }

        if (!bestPosition.equals(unit.position)) {
            List<Position> path = pathfinder.getPath(unit, bestPosition, map, roster);
            unit.moveTo(path);
        }

        Map<Position, Integer> attackRange = pathfinder.getAttackRange(unit, map);
        if (!unit.hasAttacked && attackRange.containsKey(best.position)) {
            unit.attackUnit(best);
        }
    }
}
