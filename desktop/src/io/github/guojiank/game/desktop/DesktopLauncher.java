package io.github.guojiank.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.guojiank.game.SmartSnake;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "smart-snake";
		config.addIcon("me.png",Files.FileType.Internal);
		new LwjglApplication(new SmartSnake(), config);
	}
}
