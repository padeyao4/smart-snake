package main.snake.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import guojian.SmartSnake;

public class DesktopLauncher {
    public static void main(String[] args) {
        var configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setForegroundFPS(120);
        configuration.setWindowedMode(SmartSnake.GRID_WIDTH * SmartSnake.TITLED_SIZE,SmartSnake.GRID_HEIGHT * SmartSnake.TITLED_SIZE);
        configuration.setTitle("smart-snake");
        configuration.setWindowIcon("snake_128.png");
        configuration.setResizable(false);
        new Lwjgl3Application(new SmartSnake(), configuration);
    }
}