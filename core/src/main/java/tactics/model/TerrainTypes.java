package tactics.model;

import com.badlogic.gdx.graphics.Color;

public enum TerrainTypes {

    //PARAMS: colour, blocks line of fire, tileset sheet column, tileset sheet row, ground offset
    PLAINS(Color.GREEN, false, 3, 3, 26),
    FOREST(new Color(11 / 255f, 125 / 255f, 9 / 255f, 1f), false, 1, 2, 0),
    WATER(Color.BLUE, false, 4, 0, 32),
    MOUNTAIN(Color.GRAY, true, 0, 6, 0);

    public final Color colour;
    public final Boolean blocksLineOfFire;
    public final int sheetCol;
    public final int sheetRow;
    public final int groundOffset;

    TerrainTypes(Color colour, Boolean blocksLineOfFire, int sheetCol, int sheetRow, int groundOffset) {
        this.colour = colour;
        this.blocksLineOfFire = blocksLineOfFire;
        this.sheetCol = sheetCol;
        this.sheetRow = sheetRow;
        this.groundOffset = groundOffset;
    }
}
