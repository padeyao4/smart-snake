package guojian.smart_snake_v2;

import java.util.LinkedList;
import java.util.List;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
	static double cellHeight = 400d / Config.MAZE_ROWS;

	static double cellWidth = 400d / Config.MAZE_COLS;
	static Maze maze;
	static double offset = 0;

	static GraphicsContext pen;
	static double speed = Config.SPEED;// 速度

	static final Timeline timeLine = new Timeline();// 定时，动画

	static {
		timeLine.setCycleCount(Timeline.INDEFINITE);// 设置循环
		timeLine.getKeyFrames().add(new KeyFrame(Duration.millis(speed), e -> action()));
	}

	/**
	 * 固定时间执行的动作
	 */
	private static void action() {
		int nextIndex = simpleAI();
		maze.move(nextIndex);
		if (gameOver()) {// 游戏结束
			System.out.println("game over");
			timeLine.stop();
		} else {
			drawMaze(maze.palace, maze.snake);
		}

	}

	public static void drawMaze(byte[][] maze, List<Integer> list) {
		pen.setFill(Color.WHITE);
		pen.fillRect(0, 0, 400, 400);
		for (int i = 0; i < Config.MAZE_ROWS; i++) {
			for (int j = 0; j < Config.MAZE_COLS; j++) {
				byte type = maze[i][j];
				Color color = selectColorByType(type);
				pen.setFill(color);
				pen.fillRect(j * cellWidth + offset, i * cellHeight + offset, cellWidth, cellHeight);
			}
		}

		for (int i = 0; i < list.size() - 1; i++) {
			pen.setFill(Color.DARKMAGENTA);
			int[] coord = CommFunc.indexToCoord(list.get(i));
			int[] coord2 = CommFunc.indexToCoord(list.get(i + 1));
			// 画直线
			pen.strokeLine(coord[1] * cellWidth + cellWidth / 2, coord[0] * cellHeight + cellHeight / 2,
					coord2[1] * cellWidth + cellWidth / 2, coord2[0] * cellHeight + cellHeight / 2);
		}
	}

	/**
	 * 判断游戏是否结束
	 * 
	 * @return
	 */
	private static boolean gameOver() {
		return maze.apple == -1;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static boolean isSame(List<Integer> a, List<Integer> b) {
		if (a.size() != b.size()) {
			return false;
		} else {
			for (int i = 0; i < a.size(); i++) {
				if (a.get(i) != b.get(i)) {
					return false;
				}
			}
			return true;
		}
	}

//	private static List<Integer> memoryList = new LinkedList<>();
	private static List<Integer> memoryLongPath = new LinkedList<>();

	/**
	 * 简单智能
	 * 
	 * @return 返回下一步的坐标
	 */
	private static int simpleAI() {
//		if (!memoryList.isEmpty()) {
//			return memoryList.remove(0);
//		}

		List<Integer> findTail = Searcher.findShortPath(maze.palace, maze.getHead(), maze.getTail());

		if (findTail.size() > 1) {// 能找到尾巴
			Maze cm = maze.clone();
			List<Integer> findApple = Searcher.findShortPath(cm.palace, cm.getHead(), cm.getApple());

			if (findApple.size() > 1) {// 能找到苹果

				for (int i = 1; i < findApple.size(); i++) {// 一直吃到苹果
					cm.move(findApple.get(i));
				}

				cm.log();
				List<Integer> cm_findTail = Searcher.findShortPath(cm.palace, cm.getHead(), cm.getTail());
				if (cm_findTail.size() > 2) {// 还能找到尾巴
//					memoryList.clear();
//					memoryList.addAll(findApple);
//					memoryList.remove(0);
//					return memoryList.remove(0);
					return findApple.get(1);
				} else {// 不能找到尾巴
					return getMaxPath().get(1);
				}

			} else {
				return getMaxPath().get(1);
			}
		} else {
			return 0;
		}
	}

	/**
	 * 头到尾最远路径
	 * @return
	 */
	private static List<Integer> getMaxPath() {
		List<Integer> realMaxPath = Searcher.findLongPath(maze.palace, maze.getHead(), maze.getTail());
		if (isSame(realMaxPath, memoryLongPath)) {
			realMaxPath = Searcher.findLongPathAndshuffle(maze.palace, maze.getHead(), maze.getTail());
		}
		memoryLongPath.clear();
		memoryLongPath.addAll(realMaxPath);
		return realMaxPath;
	}

	private static Color selectColorByType(byte type) {
		Color color;
		switch (type) {
		case Define.SNAKE:
			color = Color.BURLYWOOD;
			break;
		case Define.APPLE:
			color = Color.GREEN;
			break;
		case Define.WALL:
			color = Color.DARKCYAN;
			break;
		case Define.BLANK:
			color = Color.BLACK;
			break;
		case Define.PATH:
			color = Color.RED;
			break;
		default:
			color = Color.BLACK;
			break;
		}
		return color;
	}

	@FXML
	private Canvas canvas;

	@FXML
	void initialize() {
		assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'smart-snake.fxml'.";
		pen = canvas.getGraphicsContext2D();
		initMaze();
		drawMaze(maze.palace, maze.snake);
	}

	/**
	 * 初始化迷宫
	 */
	private void initMaze() {
		maze = new Maze();
		maze.initPalace();
		maze.initSnake();
		maze.pressSnakeAndPalace();
		maze.RandonApple();
		maze.pressApple();
	}

	private void keyListener(KeyEvent e) {
		System.out.println("press");
		switch (e.getCode()) {
		case ENTER:
			playAndPause();
			break;
		default:
			break;
		}
	}

	private void playAndPause() {
		if (timeLine.getStatus() == Status.PAUSED) {
			System.out.println("play");
			timeLine.play();
		} else if (timeLine.getStatus() == Status.RUNNING) {
			System.out.println("pause");
			timeLine.pause();
		} else {
			timeLine.play();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(App.class.getResource("smart-snake.fxml"));
		Scene scene = new Scene(loader.load());
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setTitle("smart-snake");
		primaryStage.setResizable(false);
		primaryStage.show();
		// 设置按键监听
		scene.setOnKeyPressed(e -> keyListener(e));
	}

	/**
	 * 测试画图
	 */
	public void testDraw() {
		Maze m = new Maze();
		m.initPalace();
		m.initSnake();
		m.pressSnakeAndPalace();
		m.RandonApple();
		m.pressApple();
		List<Integer> paths = Searcher.findShortPath(m.palace, m.getHead(), m.apple);
		Maze newm = m.clone();
		newm.pressPaths(paths);
		drawMaze(newm.palace, newm.snake);
	}

}
