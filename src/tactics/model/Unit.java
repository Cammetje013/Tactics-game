package tactics.model;

public class Unit {

    public UnitTypes unitType;
    public int currentHitpoints;
    public boolean hasMoved;
    public boolean hasAttacked;
    public Position position;
    public Teams team;

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
