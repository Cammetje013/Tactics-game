package tactics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class TileSet {
    private static final int CELL_SIZE = 64;

    private final Map<TerrainTypes, BufferedImage> tileImages = new EnumMap<>(TerrainTypes.class);

    public static TileSet loadFrom(String resourcePath) throws IOException {
        BufferedImage sheet = ImageIO.read(
                Objects.requireNonNull(TileSet.class.getClassLoader().getResource(resourcePath)));

        TileSet tileSet = new TileSet();
        for (TerrainTypes terrain : TerrainTypes.values()) {
            BufferedImage tileImage = sheet.getSubimage(
                    terrain.sheetCol * CELL_SIZE, terrain.sheetRow * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            tileSet.tileImages.put(terrain, tileImage);
        }
        return tileSet;
    }

    public BufferedImage getImage(TerrainTypes terrain) {
        return tileImages.get(terrain);
    }
}
