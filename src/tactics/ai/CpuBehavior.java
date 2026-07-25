package tactics.ai;

import tactics.model.GameMap;
import tactics.model.GameState;
import tactics.model.Unit;
import tactics.model.UnitRoster;
import tactics.pathfinding.Pathfinder;

public interface CpuBehavior {
    void act(Unit unit, GameState gameState, UnitRoster roster, GameMap map, Pathfinder pathfinder);
}
