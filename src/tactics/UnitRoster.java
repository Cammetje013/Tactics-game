package tactics;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record UnitRoster(List<Unit> units) {

    public static UnitRoster loadFrom(String resourcePath) throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(
                Objects.requireNonNull(UnitRoster.class.getClassLoader().getResource(resourcePath)).toURI()));
        return fromLines(lines);
    }

    public static UnitRoster fromLines(List<String> lines) {
        List<Unit> units = new ArrayList<>();

        for (String line : lines) {
            String[] splitString = line.split(",");
            units.add(new Unit(UnitTypes.valueOf(splitString[0]), new Position((Integer.parseInt(splitString[1])), (Integer.parseInt(splitString[2]))), Teams.valueOf(splitString[3])));

        }

        return new UnitRoster(units);
    }

    public Unit getUnitAt(int col, int row) {
        for (Unit unit : units) {
            if (unit.position.row == row && unit.position.col == col) return unit;
        }
        return null;
    }
}
