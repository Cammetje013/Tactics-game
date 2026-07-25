package tactics;

import java.awt.*;

public enum TerrainTypes {

    //PARAMS: colour, blocks line of fire, tileset sheet column, tileset sheet row, sprite top offset
    PLAINS(Color.GREEN, false, 3, 3, 26),
    FOREST(new Color(11, 125, 9), false, 1, 2, 0),
    WATER(Color.BLUE, false, 4, 0, 32),
    MOUNTAIN(Color.GRAY, true, 0, 6, 0);

    public final Color colour;
    public final Boolean blocksLineOfFire;
    public final int sheetCol;
    public final int sheetRow;
    public final int spriteTopOffset;

    TerrainTypes(Color colour, Boolean blocksLineOfFire, int sheetCol, int sheetRow, int spriteTopOffset) {
        this.colour = colour;
        this.blocksLineOfFire = blocksLineOfFire;
        this.sheetCol = sheetCol;
        this.sheetRow = sheetRow;
        this.spriteTopOffset = spriteTopOffset;
    }
}
