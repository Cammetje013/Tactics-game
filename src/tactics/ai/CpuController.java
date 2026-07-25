package tactics.ai;

import tactics.model.GameMap;
import tactics.model.GameState;
import tactics.model.Unit;
import tactics.model.UnitRoster;
import tactics.model.UnitTypes;
import tactics.pathfinding.Pathfinder;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CpuController {

    private final Map<UnitTypes, CpuBehavior> behaviors = new EnumMap<>(UnitTypes.class);

    public CpuController() {
        behaviors.put(UnitTypes.KNIGHT, new KnightBehavior());
        behaviors.put(UnitTypes.MAGE, new MageBehavior());
        behaviors.put(UnitTypes.RANGER, new RangerBehavior());
    }

    public void takeTurn(GameState gameState, UnitRoster roster, GameMap map, Pathfinder pathfinder) {
        for (Unit unit : List.copyOf(roster.units())) {
            if (unit.team != gameState.currentTeam() || Unit.isDead(unit)) continue;

            CpuBehavior behavior = behaviors.get(unit.unitType);
            if (behavior != null) {
                behavior.act(unit, gameState, roster, map, pathfinder);
            }

            roster.removeDeadUnits();
        }
    }
}
