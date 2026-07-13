package tactics;

import java.awt.*;

public enum TerrainTypes{

    //PARAMS: colour, movement cost, defence bonus, evasion bonus
    PLAINS(Color.GREEN, 1, 0, 0),
    FOREST(new Color(11, 125, 9), 2, 2, 3),
    WATER(Color.BLUE, 3, 2, -3),
    MOUNTAIN(Color.GRAY, 99, 3, 1);

    public final Color colour;
    public final int mvntCst;
    public final int dfncBns;
    public final int evsnBns;

    TerrainTypes(Color colour, int movementCost, int defenceBonus, int evasionBonus){
        this.colour = colour;
        this.mvntCst = movementCost;
        this.dfncBns = defenceBonus;
        this.evsnBns = evasionBonus;
    }
}
