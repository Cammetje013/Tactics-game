package tactics;

public class Unit {

    UnitTypes unitType;
    int currentHitpoints;
    boolean hasMoved;
    boolean hasAttacked;
    Position position;
    Teams team;

    public Unit(UnitTypes unitType, Position startPosition, Teams team) {
        this.unitType = unitType;
        this.currentHitpoints = unitType.hitpoints;
        this.position = startPosition;
        this.team = team;

    }

    public void moveTo(Position position) {
        if (!hasMoved) {
            this.position = position;
            hasMoved = true;
        }
    }

    public void attackUnit(Unit enemyUnit) {
        enemyUnit.currentHitpoints -= this.unitType.attack;
        hasAttacked = true;
    }

    public static boolean isDead(Unit unit) {
        return unit.currentHitpoints <= 0;
    }

    public void resetTurn() {
        hasMoved = false;
        hasAttacked = false;
    }
}
