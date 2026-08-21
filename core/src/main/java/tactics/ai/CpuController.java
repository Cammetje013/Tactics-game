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
        behaviors.put(UnitTypes.ORC, new KnightBehavior());
        behaviors.put(UnitTypes.NECROMANCER, new MageBehavior());
        behaviors.put(UnitTypes.SKELETON_ARCHER, new RangerBehavior());

        behaviors.put(UnitTypes.ARMORED_AXEMAN, new KnightBehavior());
        behaviors.put(UnitTypes.BAT, new KnightBehavior());
        behaviors.put(UnitTypes.ARMORED_ORC, new KnightBehavior());
        behaviors.put(UnitTypes.ARMORED_SKELETON, new KnightBehavior());
        behaviors.put(UnitTypes.ELITE_ORC, new KnightBehavior());
        behaviors.put(UnitTypes.GREATSWORD_SKELETON, new KnightBehavior());
        behaviors.put(UnitTypes.KNIGHT_TEMPLAR, new KnightBehavior());
        behaviors.put(UnitTypes.LANCER, new KnightBehavior());
        behaviors.put(UnitTypes.ORC_RIDER, new KnightBehavior());
        behaviors.put(UnitTypes.PRIEST, new MageBehavior());
        behaviors.put(UnitTypes.SKELETON, new KnightBehavior());
        behaviors.put(UnitTypes.SLIME, new KnightBehavior());
        behaviors.put(UnitTypes.SOLDIER, new KnightBehavior());
        behaviors.put(UnitTypes.SWORDSMAN, new KnightBehavior());
        behaviors.put(UnitTypes.WEREBEAR, new KnightBehavior());
        behaviors.put(UnitTypes.WEREWOLF, new KnightBehavior());
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
