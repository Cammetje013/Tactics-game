package tactics.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteSheet {
    private final Texture texture;
    private final TextureRegion[] frames;
    private final int groundLine;

    private SpriteSheet(Texture texture, TextureRegion[] frames, int groundLine) {
        this.texture = texture;
        this.frames = frames;
        this.groundLine = groundLine;
    }

    public static SpriteSheet loadFrom(String resourcePath, int frameSize) {
        Pixmap sheet = new Pixmap(Gdx.files.internal(resourcePath));
        int frameCount = sheet.getWidth() / frameSize;
        int frameHeight = sheet.getHeight();

        Texture texture = new Texture(sheet);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(texture, i * frameSize, 0, frameSize, frameHeight);
            // the game uses a y-down camera to keep the original AWT pixel math unchanged,
            // which otherwise renders every texture upside-down - flip once here instead.
            frames[i].flip(false, true);
        }

        int groundLine = shadowCenterRow(sheet, frameSize, frameHeight);
        sheet.dispose();

        return new SpriteSheet(texture, frames, groundLine);
    }

    private static final int SHADOW_R = 83, SHADOW_G = 69, SHADOW_B = 69, SHADOW_TOLERANCE = 10;
    private static final int MIN_SHADOW_ROW_WIDTH = 4;

    // scans only the first frame, same as the original which used frames[0]
    private static int shadowCenterRow(Pixmap sheet, int frameWidth, int frameHeight) {
        int minY = Integer.MAX_VALUE;
        int maxY = -1;
        for (int y = 0; y < frameHeight; y++) {
            int matchesInRow = 0;
            for (int x = 0; x < frameWidth; x++) {
                // getPixel always returns RGBA8888 regardless of the pixmap's own format
                int rgba = sheet.getPixel(x, y);
                int a = rgba & 0xFF;
                if (a <= 10) continue;
                int r = (rgba >>> 24) & 0xFF;
                int g = (rgba >>> 16) & 0xFF;
                int b = (rgba >>> 8) & 0xFF;
                if (Math.abs(r - SHADOW_R) <= SHADOW_TOLERANCE
                        && Math.abs(g - SHADOW_G) <= SHADOW_TOLERANCE
                        && Math.abs(b - SHADOW_B) <= SHADOW_TOLERANCE) {
                    matchesInRow++;
                }
            }
            if (matchesInRow >= MIN_SHADOW_ROW_WIDTH) {
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }
        }
        return maxY < 0 ? frameHeight - 1 : (minY + maxY) / 2;
    }

    public TextureRegion getFrame(int tick) {
        return frames[tick % frames.length];
    }

    public int getGroundLine() {
        return groundLine;
    }

    public void dispose() {
        texture.dispose();
    }
}
