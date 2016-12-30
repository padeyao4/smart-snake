package guojian.smart_snake;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Controller {
	static double cellHeight = 400d / Config.MAZE_ROWS;

	static double cellWidth = 400d / Config.MAZE_COLS;
	static Maze maze;
	static double offset = 0;

	static GraphicsContext pen;
	static double speed = Config.SPEED;// 速度

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

	public boolean ai;

	@FXML
	public Label ai_label;
	@FXML
	private Canvas canvas;
	public int currIdx;
	@FXML
	private Canvas cvs_show;
	@FXML
	private Pane pane;
	private AI simpleAI;

	@FXML
	private Label speed_label;

	@FXML
	public Label status_label;

	public Timeline timeLine;// 定时，动画

	/**
	 * 固定时间执行的动作
	 */
	private void action() {
		if (ai) {
			int nextIndex = simpleAI.run(maze);
			maze.move(nextIndex);
		} else {
			int nextIndex = maze.getHead() + currIdx;
			maze.move(nextIndex);
		}

		if (gameOver()) {// 游戏结束
			timeLine.stop();
			status_label.setText("STOPED");
			showGameOver();
		} else {
			drawMaze(maze.palace, maze.snake);
		}

		if (Config.SAVE) {
			Saver.storeImage(canvas);
		}
	}

	public void aiClear() {
		simpleAI.clear();
	}

	private void clearGameOver() {
		cvs_show.getGraphicsContext2D().setFill(Color.WHITE);
		cvs_show.getGraphicsContext2D().fillRect(0, 0, cvs_show.getWidth(), cvs_show.getHeight());
	}

	/**
	 * 将方向转换为移动量
	 * 
	 * @return
	 */
	private int directionConvToOffsetIdx(KeyCode key) {
		switch (key) {
		case LEFT:
			return -1;
		case RIGHT:
			return 1;
		case DOWN:
			return Config.MAZE_COLS;
		case UP:
			return -Config.MAZE_COLS;
		default:
			break;
		}
		return 0;
	}

	/**
	 * 蛇的运动方向的 偏移量
	 * 
	 * @return
	 */
	private int getSnakeDirectionIdx() {
		int head = maze.getHead();
		int body = maze.getSecondBody();
		int key = head - body;
		return key;
	}

	/**
	 * 
	 * @return 蛇运动的反方向
	 */
	private KeyCode getSnakeReverseDirection() {
		int key = getSnakeDirectionIdx();
		switch (key) {
		case 1:
			return KeyCode.LEFT;
		case -1:
			return KeyCode.RIGHT;
		case Config.MAZE_COLS:
			return KeyCode.UP;
		case -Config.MAZE_COLS:
			return KeyCode.DOWN;
		}
		return null;
	}
	
	private MediaPlayer mediaPlayer;

	@FXML
	void initialize() {
		Media media1 = new Media(this.getClass().getResource("WhereIstheLove.mp3").toString());  
		mediaPlayer = new MediaPlayer(media1);
		mediaPlayer.setCycleCount(99);
		mediaPlayer.setAutoPlay(true);
		ai = true;
		ai_label.setText("开启");
		speed_label.setText("每秒" + (1000.0d / Config.SPEED) + "步");
		pen = canvas.getGraphicsContext2D();
		simpleAI = new SimpleAI();
		restartGame();
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

	void initTimeLine() {
		timeLine = new Timeline();// 定时，动画
		timeLine.setCycleCount(Timeline.INDEFINITE);// 设置循环
		timeLine.getKeyFrames().add(new KeyFrame(Duration.millis(speed), e -> action()));
		status_label.setText("stoped");
	}

	public void resetIdx() {
		currIdx = getSnakeDirectionIdx();
	}

	public void restartGame() {
		if (timeLine != null) {
			timeLine.stop();
			status_label.setText("STOPED");
		}
		initMaze();
		currIdx = getSnakeDirectionIdx();
		initTimeLine();
		drawMaze(maze.palace, maze.snake);
		simpleAI.clear();
		clearGameOver();
	}

	/**
	 * 相反的方向忽略
	 * 
	 * @param key,up
	 *            down left right
	 * @return
	 */
	public void reverseDireAndKeepDire(KeyCode key) {
		KeyCode inverseKey = getSnakeReverseDirection();
		if (key.equals(inverseKey)) {
			currIdx = getSnakeDirectionIdx();

		} else {
			currIdx = directionConvToOffsetIdx(key);

		}
	}

	public void setAi_label(Label ai_label) {
		this.ai_label = ai_label;
	}

	private void showGameOver() {
		cvs_show.getGraphicsContext2D().setFont(new Font(15));
		cvs_show.getGraphicsContext2D().setFill(Color.BLACK);
		cvs_show.getGraphicsContext2D().fillText("GAME OVER", 30, 65);
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

	public void music() {
		Status s  = mediaPlayer.getStatus();
		if(s.equals(Status.PAUSED)){
			mediaPlayer.play();
		}else{
			mediaPlayer.pause();
		}
	}

}
