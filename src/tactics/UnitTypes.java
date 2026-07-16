package tactics;

public enum UnitTypes {
    //PARAMS: hitpoints, attack, defence, evasion, range, movement
    KNIGHT(30, 5, 3, 10, 1, 4),
    MAGE(20, 10, 1, 20, 4, 3),
    RANGER(25, 7, 2, 15, 5, 4);

    public final int hitpoints;
    public final int attack;
    public final int defence;
    public final int evasion;
    public final int range;
    public final int movement;

    UnitTypes(int hitpoints, int attack, int defence, int evasion, int range, int movement) {
        this.hitpoints = hitpoints;
        this.attack = attack;
        this.defence = defence;
        this.evasion = evasion;
        this.range = range;
        this.movement = movement;
    }
}
