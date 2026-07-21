package tactics;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record GameMap(int rows, int cols, Tile[][] tiles) {

    public static GameMap loadFrom(String resourcePath) throws IOException, URISyntaxException {
        List<String> lines = Files.readAllLines(Paths.get(
                Objects.requireNonNull(GameMap.class.getClassLoader().getResource(resourcePath)).toURI()));
        return fromLines(lines);
    }

    public static GameMap fromLines(List<String> lines) {
        Map<Character, TerrainTypes> terrainLookup = new HashMap<>();
        terrainLookup.put('M', TerrainTypes.MOUNTAIN);
        terrainLookup.put('P', TerrainTypes.PLAINS);
        terrainLookup.put('W', TerrainTypes.WATER);
        terrainLookup.put('F', TerrainTypes.FOREST);

        int rows = lines.size();
        int cols = lines.getFirst().length();
        Tile[][] tiles = new Tile[rows][cols];

        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(row).length(); col++) {
                tiles[row][col] = new Tile(terrainLookup.get(lines.get(row).charAt(col)));
            }
        }

        return new GameMap(tiles.length, tiles[0].length, tiles);
    }

    public boolean isInBounds(int row, int col) {
        return col >= 0 && col < cols && row >= 0 && row < rows;
    }

    public int movementCostAt(int row, int col) {
        return tiles[row][col].terrain.movementCost;
    }

    public boolean blocksLineOfFire(int row, int col) {
        return tiles[row][col].terrain.blocksLineOfFire;
    }
}
