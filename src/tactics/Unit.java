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
}
