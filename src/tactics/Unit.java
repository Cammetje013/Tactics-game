package tactics;

public class Unit {

    UnitTypes unitType;
    int currentHitpoints;
    Position position;
    Teams team;

    public Unit(UnitTypes unitType, Position startPosition, Teams team) {
        this.unitType = unitType;
        this.currentHitpoints = unitType.hitpoints;
        this.position = startPosition;
        this.team = team;

    }

    public void moveTo(Position position) {
        this.position = position;
    }

    public void attackUnit(Unit enemyUnit) {
        enemyUnit.currentHitpoints -= this.unitType.attack;
    }

    public static boolean isDead(Unit unit) {
        return unit.currentHitpoints <= 0;
    }
}
