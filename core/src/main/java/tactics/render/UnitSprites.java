package tactics.render;

import tactics.model.UnitTypes;

import java.util.EnumMap;
import java.util.Map;

public class UnitSprites {
    private static final int FRAME_SIZE = 100;

    private final Map<UnitTypes, SpriteSheet> idleSheets = new EnumMap<>(UnitTypes.class);

    public SpriteSheet getIdle(UnitTypes unitType) {
        return idleSheets.computeIfAbsent(unitType, this::loadIdle);
    }

    private SpriteSheet loadIdle(UnitTypes unitType) {
        String folder = unitType.spriteFolder;
        String path = "spritepacks/Characters/" + folder + "/" + folder + " with shadows/" + folder + "_Idle.png";
        return SpriteSheet.loadFrom(path, FRAME_SIZE);
    }

    public void dispose() {
        idleSheets.values().forEach(SpriteSheet::dispose);
    }
}
