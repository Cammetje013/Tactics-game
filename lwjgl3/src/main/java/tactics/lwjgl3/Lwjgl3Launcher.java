package tactics.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import tactics.ui.TacticsGame;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Tactics Game");
        config.setWindowedMode(1280, 800);
        config.setResizable(true);
        new Lwjgl3Application(new TacticsGame(), config);
    }
}
