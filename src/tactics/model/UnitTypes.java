package tactics.model;

import java.awt.*;

public enum UnitTypes {
    //PARAMS: hitpoints, attack, range, movement
    KNIGHT(30, 5, 1, 4, Color.DARK_GRAY),
    MAGE(20, 10, 4, 3, Color.PINK),
    RANGER(25, 7, 5, 4, Color.BLACK);

    public final int hitpoints;
    public final int attack;
    public final int range;
    public final int movement;
    public final Color colour;

    UnitTypes(int hitpoints, int attack, int range, int movement, Color colour) {
        this.hitpoints = hitpoints;
        this.attack = attack;
        this.range = range;
        this.movement = movement;
        this.colour = colour;
    }
}
