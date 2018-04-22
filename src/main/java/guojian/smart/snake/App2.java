package guojian.smart.snake;

import javafx.animation.Animation.Status;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * 这是入口
 */
public class App2 extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private Controller2 app;

	private void keyListener(KeyEvent e) {
		switch (e.getCode()) {
		case ENTER:
			playAndPause();
			break;
		case F1:
			openOrCloseAI();
			break;
		case F2:
			restartGame();
			break;
		case UP:
			app.reverseDireAndKeepDire(e.getCode());
			break;
		case DOWN:
			app.reverseDireAndKeepDire(e.getCode());
			break;
		case LEFT:
			app.reverseDireAndKeepDire(e.getCode());
			break;
		case RIGHT:
			app.reverseDireAndKeepDire(e.getCode());
			break;
		case SPACE:
			app.music();
			break;
		case ESCAPE:
			System.exit(0);
			break;
		default:
			break;
		}
	}

	private void restartGame() {
		app.restartGame();
	}

	private void openOrCloseAI() {
		app.aiClear();
		app.resetIdx();
		if (app.ai) {
			app.ai = false;
			app.ai_label.setText("关闭");
		} else {
			app.ai = true;
			app.ai_label.setText("开启");
		}
	}

	private void playAndPause() {
		app.resetIdx();
		if (app.timeLine.getStatus() == Status.PAUSED) {
			app.timeLine.play();
			app.status_label.setText("RUNNING");
		} else if (app.timeLine.getStatus() == Status.RUNNING) {
			app.timeLine.pause();
			app.status_label.setText("PAUSED");
		} else {
			app.timeLine.play();
			app.status_label.setText("RUNNING");
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Controller2.class.getResource("/smart-snake.fxml"));
		Scene scene = new Scene(loader.load());
		app = loader.getController();
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setTitle("smart-snake");
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/me.jpg")));
		primaryStage.show();
		scene.setOnKeyPressed(e -> keyListener(e));
	}
}
