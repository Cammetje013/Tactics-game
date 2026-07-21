package tactics;

import java.awt.*;

public enum TerrainTypes {

    //PARAMS: colour, movement cost, defence bonus, evasion bonus
    PLAINS(Color.GREEN, 1, 0, 0, false),
    FOREST(new Color(11, 125, 9), 2, 2, 3, false),
    WATER(Color.BLUE, 3, 2, -3, false),
    MOUNTAIN(Color.GRAY, 99, 3, 1, true);

    public final Color colour;
    public final int movementCost;
    public final int defenceBonus;
    public final int evasionBonus;
    public final Boolean blocksLineOfFire;

    TerrainTypes(Color colour, int movementCost, int defenceBonus, int evasionBonus, Boolean blocksLineOfFire) {
        this.colour = colour;
        this.movementCost = movementCost;
        this.defenceBonus = defenceBonus;
        this.evasionBonus = evasionBonus;
        this.blocksLineOfFire = blocksLineOfFire;
    }
}
