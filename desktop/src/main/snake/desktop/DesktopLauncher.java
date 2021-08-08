package main.snake.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import snake.SmartSnake;

/** Launches the desktop (LWJGL3) application. */
public class DesktopLauncher {
	public static void main(String[] args) {
		var configuration = new LwjglApplicationConfiguration();
		configuration.height=500;
		configuration.width=500;
		configuration.title="smart-snake";
		configuration.addIcon("snake_128.png", Files.FileType.Classpath);
		configuration.resizable=false;
		configuration.forceExit=true;
		new LwjglApplication(new SmartSnake(), configuration);
	}
}