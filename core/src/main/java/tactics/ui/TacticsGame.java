package tactics.ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import tactics.ai.CpuController;
import tactics.level.LevelLoader;
import tactics.model.GameState;
import tactics.model.LevelData;
import tactics.model.Position;
import tactics.model.Teams;
import tactics.model.TerrainTypes;
import tactics.model.Unit;
import tactics.pathfinding.Pathfinder;
import tactics.render.SpriteSheet;
import tactics.render.TileSet;
import tactics.render.UnitSprites;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TacticsGame extends ApplicationAdapter {
    private static final int TILE_SIZE = 64;
    private static final double MOVE_TILES_PER_SECOND = 3.0;
    private static final float ANIMATION_FRAME_SECONDS = 0.12f;

    private Mode mode = Mode.NONE;

    private Unit selectedUnit = null;
    private LevelData levelData;
    private Map<Position, Integer> reachableTiles = new HashMap<>();
    private Pathfinder rangeFinder;
    private TileSet tileSet;
    private UnitSprites unitSprites;
    private int animationTick = 0;
    private float animationAccumulator = 0f;
    private Position hoveredTile = null;
    private GameState gameState;
    private CpuController cpuController;

    private SpriteBatch batch;
    private Texture diamondFill;
    private Texture diamondOutline;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Stage stage;
    private Skin skin;
    private Label turnLabel;
    private UnitActionMenu currentPopup;

    @Override
    public void create() {
        levelData = LevelLoader.load("maps/map3");
        tileSet = TileSet.loadFrom("tilemaps/Tileset.png");
        unitSprites = new UnitSprites();
        rangeFinder = new Pathfinder();
        gameState = new GameState();
        cpuController = new CpuController();

        batch = new SpriteBatch();
        diamondFill = makeDiamondTexture(true);
        diamondOutline = makeDiamondTexture(false);

        camera = new OrthographicCamera();
        camera.setToOrtho(true, baseWidth(), baseHeight());
        viewport = new FitViewport(baseWidth(), baseHeight(), camera);

        setupUi();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new GameInput());
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void setupUi() {
        skin = new Skin();

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        skin.add("white", new Texture(pm));
        pm.dispose();

        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.newDrawable("white", 0.25f, 0.25f, 0.25f, 1f);
        buttonStyle.down = skin.newDrawable("white", 0.45f, 0.45f, 0.45f, 1f);
        buttonStyle.over = skin.newDrawable("white", 0.35f, 0.35f, 0.35f, 1f);
        buttonStyle.disabled = skin.newDrawable("white", 0.15f, 0.15f, 0.15f, 1f);
        buttonStyle.font = font;
        buttonStyle.disabledFontColor = Color.GRAY;
        skin.add("default", buttonStyle);

        stage = new Stage(new ScreenViewport());

        turnLabel = new Label("", labelStyle);
        Table topTable = new Table();
        topTable.setFillParent(true);
        topTable.top().left().pad(10);
        topTable.add(turnLabel);
        stage.addActor(topTable);

        TextButton endTurnButton = new TextButton("End Turn", skin);
        endTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                endTurn();
            }
        });
        Table bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.bottom().right().pad(10);
        bottomTable.add(endTurnButton);
        stage.addActor(bottomTable);
    }

    // builds a small tileSize x tileSize/2 diamond texture matching the original
    // AWT diamondAt() polygon, used for both the filled reachable-tile highlight
    // and the selected/hovered outline (tinted via batch.setColor at draw time).
    private Texture makeDiamondTexture(boolean filled) {
        int w = TILE_SIZE;
        int h = TILE_SIZE / 2;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, 1f);
        int halfW = w / 2;
        int halfH = h / 2;
        if (filled) {
            for (int y = 0; y < h; y++) {
                int dy = Math.abs(y - halfH);
                int rowHalfWidth = halfW * (halfH - dy) / halfH;
                pm.drawLine(halfW - rowHalfWidth, y, halfW + rowHalfWidth, y);
            }
        } else {
            pm.drawLine(halfW, 0, w - 1, halfH);
            pm.drawLine(w - 1, halfH, halfW, h - 1);
            pm.drawLine(halfW, h - 1, 0, halfH);
            pm.drawLine(0, halfH, halfW, 0);
        }
        Texture texture = new Texture(pm);
        pm.dispose();
        return texture;
    }

    private int originX() {
        return levelData.map().rows() * (TILE_SIZE / 2);
    }

    private int isoX(int col, int row) {
        return (col - row) * (TILE_SIZE / 2) + originX();
    }

    private int isoY(int col, int row) {
        return (col + row) * (TILE_SIZE / 4);
    }

    private double isoX(double col, double row) {
        return (col - row) * (TILE_SIZE / 2.0) + originX();
    }

    private double isoY(double col, double row) {
        return (col + row) * (TILE_SIZE / 4.0);
    }

    private int baseWidth() {
        int cols = levelData.map().cols();
        int rows = levelData.map().rows();
        return (cols + rows - 2) * (TILE_SIZE / 2) + TILE_SIZE;
    }

    private int baseHeight() {
        int cols = levelData.map().cols();
        int rows = levelData.map().rows();
        return (cols + rows - 2) * (TILE_SIZE / 4) + TILE_SIZE;
    }

    private TerrainTypes terrainAt(int col, int row) {
        return levelData.map().tiles()[row][col].terrain;
    }

    private boolean diamondContains(int col, int row, double px, double py) {
        int x = isoX(col, row);
        int y = isoY(col, row) + terrainAt(col, row).groundOffset;
        double halfW = TILE_SIZE / 2.0;
        double halfH = TILE_SIZE / 4.0;
        double dx = Math.abs(px - x);
        double dy = Math.abs(py - (y + halfH));
        return (dx / halfW + dy / halfH) <= 1.0;
    }

    private Position screenToGrid(double x, double y) {
        double halfW = TILE_SIZE / 2.0;
        double halfH = TILE_SIZE / 4.0;
        double colMinusRow = (x - originX()) / halfW;
        double colPlusRow = (y - halfH) / halfH;
        int approxCol = (int) Math.round((colMinusRow + colPlusRow) / 2);
        int approxRow = (int) Math.round((colPlusRow - colMinusRow) / 2);

        Position best = new Position(-1, -1);
        int bestDistance = Integer.MAX_VALUE;
        for (int row = approxRow - 2; row <= approxRow + 2; row++) {
            for (int col = approxCol - 2; col <= approxCol + 2; col++) {
                if (!levelData.map().isInBounds(row, col)) continue;
                if (!diamondContains(col, row, x, y)) continue;
                int distance = Math.abs(col - approxCol) + Math.abs(row - approxRow);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    best = new Position(col, row);
                }
            }
        }
        return best;
    }

    private record DepthDraw(double depth, Runnable draw) {}

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        updateAnimationTick(delta);
        tickMovementAnimation(delta);

        ScreenUtils.clear(0.15f, 0.15f, 0.15f, 1f);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);

        List<DepthDraw> drawables = new ArrayList<>();
        for (int row = 0; row < levelData.map().rows(); row++) {
            for (int col = 0; col < levelData.map().cols(); col++) {
                TerrainTypes terrain = levelData.map().tiles()[row][col].terrain;
                int x = isoX(col, row) - TILE_SIZE / 2;
                int y = isoY(col, row);
                int cCol = col;
                int cRow = row;
                drawables.add(new DepthDraw(col + row, () -> {
                    batch.setColor(Color.WHITE);
                    batch.draw(tileSet.getImage(terrain), x, y, TILE_SIZE, TILE_SIZE);
                    drawTileHighlight(cCol, cRow);
                }));
            }
        }
        for (Unit unit : levelData.roster().units()) {
            double depth = unit.displayCol + unit.displayRow + 0.5;
            drawables.add(new DepthDraw(depth, () -> drawUnit(unit)));
        }
        drawables.sort(Comparator.comparingDouble(DepthDraw::depth));
        for (DepthDraw drawable : drawables) {
            drawable.draw().run();
        }

        batch.end();

        turnLabel.setText("Turn " + gameState.turnNumber() + " - " + gameState.currentTeam());
        stage.act(delta);
        stage.getViewport().apply();
        stage.draw();
    }

    private void drawTileHighlight(int col, int row) {
        int x = isoX(col, row) - TILE_SIZE / 2;
        int y = isoY(col, row) + terrainAt(col, row).groundOffset;

        if (reachableTiles.containsKey(new Position(col, row))) {
            batch.setColor(1f, 140 / 255f, 0f, 120 / 255f);
            batch.draw(diamondFill, x, y, TILE_SIZE, TILE_SIZE / 2f);
        }

        if (selectedUnit != null && selectedUnit.position.col == col && selectedUnit.position.row == row) {
            batch.setColor(Color.BLACK);
            batch.draw(diamondOutline, x, y, TILE_SIZE, TILE_SIZE / 2f);
        }

        if (hoveredTile != null && hoveredTile.col == col && hoveredTile.row == row) {
            batch.setColor(Color.WHITE);
            batch.draw(diamondOutline, x, y, TILE_SIZE, TILE_SIZE / 2f);
        }
        batch.setColor(Color.WHITE);
    }

    private void drawUnit(Unit unit) {
        int terrainCol = (int) Math.round(unit.displayCol);
        int terrainRow = (int) Math.round(unit.displayRow);
        float centerX = (float) isoX(unit.displayCol, unit.displayRow);
        float groundY = (float) isoY(unit.displayCol, unit.displayRow) + terrainAt(terrainCol, terrainRow).groundOffset;

        SpriteSheet idle = unitSprites.getIdle(unit.unitType);
        TextureRegion frame = idle.getFrame(animationTick);
        int drawSize = TILE_SIZE * 2;
        int frameSize = frame.getRegionWidth();
        double spriteScale = (double) drawSize / frameSize;
        int groundLineScaled = (int) Math.round(idle.getGroundLine() * spriteScale);
        float drawX = centerX - drawSize / 2f;
        float drawY = groundY + TILE_SIZE / 4f - groundLineScaled;

        boolean faceLeft = unit.team == Teams.CPU;
        TextureRegion drawRegion = frame;
        if (faceLeft) {
            drawRegion = new TextureRegion(frame);
            drawRegion.flip(true, false);
        }
        batch.setColor(Color.WHITE);
        batch.draw(drawRegion, drawX, drawY, drawSize, drawSize);
    }

    private void updateAnimationTick(float delta) {
        animationAccumulator += delta;
        while (animationAccumulator >= ANIMATION_FRAME_SECONDS) {
            animationAccumulator -= ANIMATION_FRAME_SECONDS;
            animationTick++;
        }
    }

    private void tickMovementAnimation(float delta) {
        double step = MOVE_TILES_PER_SECOND * delta;
        for (Unit unit : levelData.roster().units()) {
            if (unit.pendingPath.isEmpty()) continue;
            Position target = unit.pendingPath.get(0);
            double dCol = target.col - unit.displayCol;
            double dRow = target.row - unit.displayRow;
            double remaining = Math.sqrt(dCol * dCol + dRow * dRow);
            if (remaining <= step) {
                unit.displayCol = target.col;
                unit.displayRow = target.row;
                unit.pendingPath.remove(0);
            } else {
                unit.displayCol += dCol / remaining * step;
                unit.displayRow += dRow / remaining * step;
            }
        }
    }

    private Unit getUnitAt(int col, int row) {
        for (Unit unit : levelData.roster().units()) {
            if (unit.position.col == col && unit.position.row == row) return unit;
        }
        return null;
    }

    private void openUnitMenu(int screenX, int screenY, boolean canMove, boolean canAttack) {
        UnitActionMenu menu = new UnitActionMenu(skin,
                () -> {
                    mode = Mode.MOVE;
                    reachableTiles = rangeFinder.getReachableTiles(selectedUnit, levelData.map(), levelData.roster());
                    closePopup();
                },
                () -> {
                    mode = Mode.ATTACK;
                    reachableTiles = rangeFinder.getAttackRange(selectedUnit, levelData.map());
                    closePopup();
                },
                canMove, canAttack);
        Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
        menu.setPosition(stageCoords.x, stageCoords.y - menu.getHeight());
        stage.addActor(menu);
        currentPopup = menu;
    }

    private void closePopup() {
        if (currentPopup != null) {
            currentPopup.remove();
            currentPopup = null;
        }
    }

    public void endTurn() {
        selectedUnit = null;
        reachableTiles = new HashMap<>();
        mode = Mode.NONE;
        closePopup();
        gameState.endTurn(levelData.roster());
        if (gameState.currentTeam() == Teams.CPU) {
            cpuController.takeTurn(gameState, levelData.roster(), levelData.map(), rangeFinder);
            endTurn();
        }
    }

    private void checkAutoEndTurn() {
        for (Unit unit : levelData.roster().units()) {
            if (unit.team == gameState.currentTeam() && !(unit.hasMoved && unit.hasAttacked)) {
                return;
            }
        }
        endTurn();
    }

    private class GameInput extends InputAdapter {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (currentPopup != null) {
                closePopup();
                return true;
            }

            Vector2 world = viewport.unproject(new Vector2(screenX, screenY));
            Position clicked = screenToGrid(world.x, world.y);
            int col = clicked.col;
            int row = clicked.row;

            if (mode == Mode.NONE) {
                Unit clickedUnit = getUnitAt(col, row);
                selectedUnit = (clickedUnit != null && clickedUnit.team == gameState.currentTeam()) ? clickedUnit : null;
                if (selectedUnit != null) {
                    boolean canMove = !selectedUnit.hasMoved;
                    boolean canAttack = !selectedUnit.hasAttacked;
                    openUnitMenu(screenX, screenY, canMove, canAttack);
                } else {
                    reachableTiles = new HashMap<>();
                }
            } else if (mode == Mode.MOVE) {
                if (selectedUnit != null && getUnitAt(col, row) == null && reachableTiles.containsKey(new Position(col, row))) {
                    List<Position> path = rangeFinder.getPath(selectedUnit, new Position(col, row), levelData.map(), levelData.roster());
                    selectedUnit.moveTo(path);
                    reachableTiles = new HashMap<>();
                    selectedUnit = null;
                    mode = Mode.NONE;
                    checkAutoEndTurn();
                }
            } else if (mode == Mode.ATTACK) {
                Unit enemyUnit = getUnitAt(col, row);
                if (enemyUnit != null) {
                    if (!enemyUnit.team.equals(selectedUnit.team) && reachableTiles.containsKey(new Position(col, row))) {
                        selectedUnit.attackUnit(enemyUnit);
                        levelData.roster().removeDeadUnits();
                        reachableTiles = new HashMap<>();
                        selectedUnit = null;
                        mode = Mode.NONE;
                        checkAutoEndTurn();
                    }
                } else {
                    reachableTiles = new HashMap<>();
                    mode = Mode.NONE;
                }
            }
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            Vector2 world = viewport.unproject(new Vector2(screenX, screenY));
            hoveredTile = screenToGrid(world.x, world.y);
            return false;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        tileSet.dispose();
        unitSprites.dispose();
        diamondFill.dispose();
        diamondOutline.dispose();
        stage.dispose();
        skin.dispose();
    }
}
