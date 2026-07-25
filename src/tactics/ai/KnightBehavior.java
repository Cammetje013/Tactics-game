package tactics.ai;

import tactics.model.GameMap;
import tactics.model.GameState;
import tactics.model.Unit;
import tactics.model.UnitRoster;
import tactics.pathfinding.Pathfinder;

public class KnightBehavior implements CpuBehavior {
    @Override
    public void act(Unit unit, GameState gameState, UnitRoster roster, GameMap map, Pathfinder pathfinder) {
        // TODO: melee rush - close distance to nearest enemy, attack if in range
    }
}
