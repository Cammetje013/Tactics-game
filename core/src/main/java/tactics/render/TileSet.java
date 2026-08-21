package tactics.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import tactics.model.TerrainTypes;

import java.util.EnumMap;
import java.util.Map;

public class TileSet {
    private static final int CELL_SIZE = 64;

    private final Texture texture;
    private final Map<TerrainTypes, TextureRegion> tileImages = new EnumMap<>(TerrainTypes.class);

    private TileSet(Texture texture) {
        this.texture = texture;
    }

    public static TileSet loadFrom(String resourcePath) {
        Texture texture = new Texture(Gdx.files.internal(resourcePath));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TileSet tileSet = new TileSet(texture);
        for (TerrainTypes terrain : TerrainTypes.values()) {
            TextureRegion region = new TextureRegion(texture,
                    terrain.sheetCol * CELL_SIZE, terrain.sheetRow * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            // the game uses a y-down camera to keep the original AWT pixel math unchanged,
            // which otherwise renders every texture upside-down - flip once here instead.
            region.flip(false, true);
            tileSet.tileImages.put(terrain, region);
        }
        return tileSet;
    }

    public TextureRegion getImage(TerrainTypes terrain) {
        return tileImages.get(terrain);
    }

    public void dispose() {
        texture.dispose();
    }
}
