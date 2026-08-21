package tactics.model;

import java.util.ArrayList;
import java.util.List;

public class Unit {

    public UnitTypes unitType;
    public int currentHitpoints;
    public boolean hasMoved;
    public boolean hasAttacked;
    public Position position;
    public Teams team;

    // visual-only: the tile-by-tile route still being animated on screen, and the
    // currently rendered position along it - position above is already the final
    // logical tile the moment moveTo is called, these just drive the walk animation.
    public double displayCol;
    public double displayRow;
    public List<Position> pendingPath = new ArrayList<>();

    public Unit(UnitTypes unitType, Position startPosition, Teams team) {
        this.unitType = unitType;
        this.currentHitpoints = unitType.hitpoints;
        this.position = startPosition;
        this.team = team;
        this.displayCol = startPosition.col;
        this.displayRow = startPosition.row;
    }

    public void moveTo(List<Position> path) {
        if (hasMoved || path.isEmpty()) return;
        pendingPath = new ArrayList<>(path);
        position = path.get(path.size() - 1);
        hasMoved = true;
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
