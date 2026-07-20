package tactics;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class LevelLoader {
    private static final String UNITS_DELIMITER = "---UNITS---";

    public static LevelData load(String resourcePath) throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(
                Objects.requireNonNull(LevelLoader.class.getClassLoader().getResource(resourcePath)).toURI()));

        List<String> mapLines;
        List<String> unitLines;
        int delimiterIndex = lines.indexOf(UNITS_DELIMITER);
        mapLines = lines.subList(0, delimiterIndex);
        unitLines = lines.subList(delimiterIndex + 1, lines.size());

        return new LevelData(GameMap.fromLines(mapLines), UnitRoster.fromLines(unitLines));
    }
}
