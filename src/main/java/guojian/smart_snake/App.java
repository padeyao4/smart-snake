package guojian.smart_snake;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import guojian.smart_snake.BFS.*;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * <p>
 * <p>
 * javafx画图简单的用法：<br>
 * 舞台对象(primaryStage)需要添加一个场景对象(Scene)<br>
 * 场景对象需要添加画布对象(Canvas)<br>
 * 然后舞台对象通过show()方法展示舞台显示画面<br>
 * 最后通过main方法调用launch()方法 来运行javafx<br>
 * <br>
 * 画布对象(canvas)拥有一个画笔(GraphicsContext),通过调用画笔的各种方法在画布上画内容<br>
 * eg.<br>
 * graphicsContext.strokeLine(x, y, x2, y2)<br>
 * 画一条直线在画布上。
 * 
 * </p>
 * 
 * @author guojian
 * @date 2016年11月18日 下午5:14:49
 * @email 1181819395@qq.com
 */
public class App extends Application {
	private static int WIDTH;// 游戏区域画面宽度
	private static int HEIGHT;// 游戏区域画面高度

	private static int FITWIDTH;// 游戏区域，每个小方格的宽度
	private static int FITHEIGHT;// 每个小方块的长度

	private static int OFFSET;
	private static int frame_width;// 画面宽度
	private static int frame_height;// 画面高度

	private static String imageDir;
	private static String rootPath;
	private static String saveImage;

	private static int speed;

	private GraphicsContext pen;// 画笔
	private Scene scene;// 场景

	private Maze maze;// 蛇，墙，苹果构成的迷宫
	private Point nextPoint;// 蛇一下步移动的点

	private Canvas cvs;

	private static void initApp() {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("config");
			WIDTH = Integer.parseInt(bundle.getString("screenWidth"));
			HEIGHT = Integer.parseInt(bundle.getString("screenHeight"));
			FITWIDTH = WIDTH / Maze.COLSIZE;
			FITHEIGHT = HEIGHT / Maze.ROWSIZE;
			OFFSET = Integer.parseInt(bundle.getString("offset"));
			frame_width = WIDTH + 2 * OFFSET;
			frame_height = HEIGHT + 2 * OFFSET;
			imageDir = bundle.getString("imageDir");
			speed = Integer.parseInt(bundle.getString("speed"));
			rootPath = bundle.getString("rootPath");
			saveImage = bundle.getString("saveImage");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("config.properties填写错误");
			System.exit(1);
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group group = new Group();
		cvs = new Canvas(frame_width, frame_height);
		pen = cvs.getGraphicsContext2D();
		group.getChildren().add(cvs);
		scene = new Scene(group, frame_width, frame_height);
		primaryStage.setScene(scene);// primaryStage添加
										// scene,scene添加group，group添加canvas。canvas获取graphicsContext
		primaryStage.setTitle("SmartSnake");
		primaryStage.show();

		// 初始化游戏
		initGame();

		// 设置定时，每隔800ms执行一次
		Timeline timeLine = new Timeline();
		timeLine.setCycleCount(Timeline.INDEFINITE);// 设置循环
		timeLine.getKeyFrames().add(new KeyFrame(Duration.millis(speed), e -> {
			action(timeLine);
		}));

		// 开始帮助信息
		paintHelpInfo("按方向键开始游戏,按enter开启智能", 0, 25);
		// 设置按键监听
		scene.setOnKeyPressed(e -> {
			controller(e.getCode(), timeLine);
		});
	}

	private void paintHelpInfo(String msg, int offset, int fontSize) {
		// 画背景
		pen.setFill(Color.WHITE);
		pen.fillRect(0, 0, frame_width, frame_width);
		// 设置字体大小
		pen.setFont(new Font(fontSize));
		// 设置文字颜色
		pen.setFill(Color.BLACK);
		// 画文字
		pen.fillText(msg, offset + OFFSET, HEIGHT / 2 + OFFSET);
	}

	/**
	 * 初始化游戏
	 */
	private void initGame() {
		maze = new Maze();
		maze.initWalls();
		maze.initMazeSnake();
		maze.randomApple();
	}

	private boolean isArtificial = false;
	private Game status=Game.stoped;

	/**
	 * 游戏每隔间断时间后实行的动作
	 * 
	 * @param timeLine
	 */
	private void action(Timeline timeLine) {
		if (isArtificial) {
			smartSnakeRun();
		} else {
			setPointByKey(currentKey);// 保持上一次方向运动
		}

		status = maze.snakeMove(nextPoint);
		if (status == Game.apple) {
			maze.randomApple();
		} else if (status == Game.over) {// 当游戏返回over时，结束游戏
			paintHelpInfo("GAME OVER", 80, 40);
			timeLine.stop();
			return;
		}

		paintArray(maze);

		if (saveImage.trim().equals("true")) {
			saveImageLocal();
		}
	}

	private void smartSnakeRun() {
		Path shortPath = BFS.searchShortPath(maze.getSnakeHead(), maze.getApple(), maze.getArray());
		if (shortPath.isEmpty()) {// 找不到苹果
			// 走离蛇身的最远距离
			Path longPath = BFS.searchBodysPath(maze.getSnakeHead(), maze.getSnake(), maze.getArray());
			if (longPath.isEmpty()) {// 路径为空
				System.out.println("Game over");
				nextPoint = null;
			} else {
				nextPoint = longPath.getNextPoint();
			}
		} else {// 找到苹果
			// 试探走一步
			Maze cloneMaze = maze.clone();
			cloneMaze.snakeMove(shortPath.getNextPoint());
			// 找尾巴。找的到表示安全
			Path clonePath = BFS.searchShortPath(cloneMaze.getSnake().getHead(), cloneMaze.getSnake().getTail(),
					cloneMaze.getArray());
			if (clonePath.size() > 2) {// 试探走一步后，蛇头离蛇尾距离大于2，安全。
				Path longPath = BFS.searchBodysPath(maze.getSnakeHead(), maze.getSnake(), maze.getArray());
				if (shortPath.size() <= longPath.size()) {
					nextPoint = shortPath.getNextPoint();
				} else {
					nextPoint = longPath.getNextPoint();
				}

			} else {// 蛇头紧挨蛇尾
					// 走离蛇身的最远距离
				Path longPath = BFS.searchBodysPath(maze.getSnakeHead(), maze.getSnake(), maze.getArray());
				if (longPath.isEmpty()) {// 路径为空
					System.out.println("蛇头紧挨蛇尾,最远路径为空");
					nextPoint = shortPath.getNextPoint();
				} else {// 走最远距离
					System.out.println("蛇头紧挨蛇尾,走最远路径");
					nextPoint = longPath.getNextPoint();
				}
			}
		}
	}

	private String dir;

	/**
	 * 将游戏执行图片保存到本地
	 */
	public void saveImageLocal() {
		String snakedirStr = System.getProperty(rootPath) + File.separatorChar + imageDir;
		File snakedir = new File(snakedirStr);

		if (!snakedir.exists()) {
			snakedir.mkdirs();
		}

		if (dir == null) {
			dir = System.currentTimeMillis() + "";
			new File(snakedirStr + File.separatorChar + dir).mkdir();
		}

		WritableImage image = cvs.snapshot(new SnapshotParameters(), null);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(
					snakedirStr + File.separatorChar + dir + File.separatorChar + System.currentTimeMillis() + ".png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private KeyCode currentKey = KeyCode.DOWN;// 保存上一次按键的方向

	/**
	 * 监听按键，并执行对应的方法
	 * 
	 * @param e
	 * @param timeLine
	 */
	private void controller(KeyCode key, Timeline timeLine) {
		if (timeLine.getStatus() == Status.STOPPED) {
			// 按方向键开始游戏
			if (key == KeyCode.UP || key == KeyCode.DOWN || key == KeyCode.LEFT || key == KeyCode.RIGHT) {
				timeLine.play();
				if (key == KeyCode.DOWN) {
					currentKey = KeyCode.UP;
				} else {
					currentKey = key;
				}
				paintArray(maze);// 第一次显示
				return;
			}

			if (key == KeyCode.ENTER) {
				if (isArtificial == false) {
					timeLine.play();
					isArtificial = true;
					paintArray(maze);// 第一次显示
					return;
				}
			}
		} else if (timeLine.getStatus() == Status.RUNNING) {
			if (key == KeyCode.SPACE) {
				timeLine.pause();
				return;
			} else if (key == KeyCode.ENTER) {
				if (isArtificial == false) {
					isArtificial = true;
					return;
				} else {
					isArtificial = false;
					currentKey=getSnakeDirection()[1];
					return;
				}
			}

		} else if (timeLine.getStatus() == Status.PAUSED) {
			if (key == KeyCode.SPACE) {
				timeLine.play();
				return;
			} else if (key == KeyCode.ENTER) {
				if (isArtificial == false) {
					timeLine.play();
					isArtificial = true;
					return;
				}
			}
		}
		setPointByKey(key);// 控制
	}

	private KeyCode negativeIgnore(KeyCode key) {
		KeyCode inverseKey = getSnakeDirection()[0];
		if (key == inverseKey) {
			return currentKey;
		} else {
			return key;
		}
	}

	/**
	 * 
	 * @return 第一个key 为 蛇运动的反方向，第二个key为蛇运动的正方向
	 */
	private KeyCode[] getSnakeDirection() {
		KeyCode inverseKey = null;
		KeyCode forwardKey = null;
		Point p1 = maze.getSnakeHead();
		List<Point> list = maze.getSnake().getList();
		Point p2 = list.get(list.size() - 2);

		if (p1.x > p2.x) {
			inverseKey = KeyCode.LEFT;
			forwardKey = KeyCode.RIGHT;
		} else if (p1.x < p2.x) {
			inverseKey = KeyCode.RIGHT;
			forwardKey = KeyCode.LEFT;
		} else if (p1.x == p2.x) {
			if (p1.y > p2.y) {
				inverseKey = KeyCode.DOWN;
				forwardKey = KeyCode.UP;
			} else if (p1.y < p2.y) {
				inverseKey = KeyCode.UP;
				forwardKey = KeyCode.DOWN;
			}
		}
		return new KeyCode[]{inverseKey,forwardKey};
	}

	/**
	 * 根据keyCode设置下一个point和保存当前KeyCode
	 * 
	 * @param key
	 */
	private void setPointByKey(KeyCode key) {
		switch (key) {
		case UP:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().row - 1, maze.getSnakeHead().col);
			currentKey = negativeIgnore(key);
			break;
		case DOWN:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().row + 1, maze.getSnakeHead().col);
			currentKey = negativeIgnore(key);
			break;
		case LEFT:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().row, maze.getSnakeHead().col - 1);
			currentKey = negativeIgnore(key);
			break;
		case RIGHT:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().row, maze.getSnakeHead().col + 1);
			currentKey = negativeIgnore(key);
			break;
		default:
			break;
		}
	}

	public static void main(String[] args) {
		initApp();
		launch(args);// 启动程序
	}

	/**
	 * 将数组画到画布
	 * 
	 * @param m
	 */
	private void paintArray(Maze m) {
		// 画背景
		pen.setFill(Color.WHITE);
		pen.fillRect(0, 0, frame_width, frame_width);

		for (int row = 0; row < Maze.ROWSIZE; row++) {
			for (int col = 0; col < Maze.COLSIZE; col++) {
				Point point = m.getArrayPoint(row, col);
				pen.setFill(point.getColor());
				pen.fillRect(point.getSx() * FITWIDTH + OFFSET, point.getSy() * FITHEIGHT + OFFSET, FITWIDTH - 1,
						FITHEIGHT - 1);
			}
		}

		List<Point> snakeList = maze.getSnake().getList();
		for (int i = 0; i < snakeList.size() - 1; i++) {
			pen.setFill(Color.DARKMAGENTA);
			//画直线
			pen.strokeLine(snakeList.get(i).sx * FITWIDTH + OFFSET + FITWIDTH / 2,
					snakeList.get(i).sy * FITHEIGHT + OFFSET + FITHEIGHT / 2,
					snakeList.get(i + 1).sx * FITWIDTH + OFFSET + FITWIDTH / 2,
					snakeList.get(i + 1).sy * FITHEIGHT + OFFSET + FITHEIGHT / 2);
		}
		for (int x = 0; x < Maze.COLSIZE; x++) {
			// 设置字体大小
			pen.setFont(new Font(15));
			// 设置文字颜色
			pen.setFill(Color.BLACK);
			// 画文字
			pen.fillText(x + "", OFFSET + x * FITWIDTH + (OFFSET / 2), frame_height);
		}
		for (int y = 0; y < Maze.ROWSIZE; y++) {
			// 设置字体大小
			pen.setFont(new Font(15));
			// 设置文字颜色
			pen.setFill(Color.BLACK);
			// 画文字
			pen.fillText(y + "", 0, OFFSET + HEIGHT - FITHEIGHT * (y) - (OFFSET / 2));
		}

	}

}
