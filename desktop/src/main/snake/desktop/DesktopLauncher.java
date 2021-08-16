package main.snake.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import guojian.SmartSnake;

public class DesktopLauncher {
    public static void main(String[] args) {
        var configuration = new LwjglApplicationConfiguration();
        configuration.height = SmartSnake.GRID_HEIGHT * SmartSnake.TITLED_SIZE;
        configuration.width = SmartSnake.GRID_WIDTH * SmartSnake.TITLED_SIZE;
        configuration.title = "smart-snake";
        configuration.addIcon("snake_128.png", Files.FileType.Classpath);
        configuration.resizable = false;
        configuration.forceExit = false;
        new LwjglApplication(new SmartSnake(), configuration);
    }
}