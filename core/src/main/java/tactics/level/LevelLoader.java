package tactics.level;

import com.badlogic.gdx.Gdx;
import tactics.model.GameMap;
import tactics.model.LevelData;
import tactics.model.UnitRoster;

import java.util.List;

public class LevelLoader {
    private static final String UNITS_DELIMITER = "---UNITS---";

    public static LevelData load(String resourcePath) {
        List<String> lines = Gdx.files.internal(resourcePath).readString().lines().toList();

        List<String> mapLines;
        List<String> unitLines;
        int delimiterIndex = lines.indexOf(UNITS_DELIMITER);
        mapLines = lines.subList(0, delimiterIndex);
        unitLines = lines.subList(delimiterIndex + 1, lines.size());

        return new LevelData(GameMap.fromLines(mapLines), UnitRoster.fromLines(unitLines));
    }
}
