package tactics.ai;

import tactics.model.GameMap;
import tactics.model.GameState;
import tactics.model.Unit;
import tactics.model.UnitRoster;
import tactics.pathfinding.Pathfinder;

public class RangerBehavior implements CpuBehavior {
    @Override
    public void act(Unit unit, GameState gameState, UnitRoster roster, GameMap map, Pathfinder pathfinder) {
        // TODO: kiting attacker - attack then retreat out of enemy movement+range
    }
}
