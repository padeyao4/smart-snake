package guojian.smart_snake;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

public class Saver {
	public static void storeImage(Canvas cvs) {
		String deskdir = getDeskTopDir();
		createDirOrNot(deskdir);
		String overPath = deskdir + File.separatorChar + System.currentTimeMillis() + ".png";
		saveImage(new File(overPath), cvs);
	}

	private static void createDirOrNot(String deskdir) {
		File file = new File(deskdir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private static String getDeskTopDir() {
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
		String desktopPath = desktopDir.getAbsolutePath();
		String deskdir = desktopPath + File.separatorChar + Config.DIR;
		return deskdir;
	}

	private static void saveImage(File savePath, Canvas cvs) {
		WritableImage image = cvs.snapshot(new SnapshotParameters(), null);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", savePath);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
