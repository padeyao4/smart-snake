package io.github.guojiank.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.guojiank.game.SmartSnake;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "smart-snake";
        config.height = 500;
        config.width = 500;
        config.addIcon(getPNGPath(), Files.FileType.Internal);
        new LwjglApplication(new SmartSnake(), config);
    }

    private static String getPNGPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String path = "snake.png";
        if (os.contains("window") || os.contains("linux")) {
            path = "snake_32.png";
        } else if (os.contains("mac"))
            path = "snake_128.png";
        return path;
    }


}
