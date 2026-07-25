package tactics.ai;

import tactics.model.GameMap;
import tactics.model.GameState;
import tactics.model.Unit;
import tactics.model.UnitRoster;
import tactics.pathfinding.Pathfinder;

public class MageBehavior implements CpuBehavior {
    @Override
    public void act(Unit unit, GameState gameState, UnitRoster roster, GameMap map, Pathfinder pathfinder) {
        // TODO: ranged attacker - keep distance, attack strongest threat in range
    }
}
