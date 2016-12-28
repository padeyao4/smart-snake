package guojian.smart_snake;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
	private static int FITHEIGHT;// 每个小方块的长度
	private static int FITWIDTH;// 游戏区域，每个小方格的宽度

	private static int frame_height;// 画面高度
	private static int frame_width;// 画面宽度

	private static int HEIGHT;// 游戏区域画面高度
	private static String imageDir;// 图片文件夹名称
	private static int OFFSET;// 偏移量

	private static String rootPath;// java获取绝对路径的参数. eg
									// System.getProperty(rootPath) ,
									// rootpath="user.home"
	private static String saveImage;// 是否开启保存图片 默认为false
	private static int speed;// 速度

	private static int WIDTH;// 游戏区域画面宽度
	private static String userDir;
	private static String saveGameOverImage;

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
			userDir = bundle.getString("userDir");
			saveGameOverImage = bundle.getString("saveGameOverImage");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("config.properties填写错误");
			System.exit(1);
		}

	}

	public static void main(String[] args) {
		initApp();
		launch(args);// 启动程序
	}

	private KeyCode currentKey;// 保存上一次按键的方向
	private Canvas cvs;// 画布

	private String dir;// 每次运行时，图片保存的文件夹名称。为游戏运行时，系统时间

	private BooleanProperty isAuto;// 默认不开启智能

	private Maze maze;// 蛇，墙，苹果构成的迷宫

	private Point nextPoint;// 蛇一下步移动的点

	private GraphicsContext pen;// 画笔

	private Scene scene;// 场景
	private SnakeStatus status;// 蛇当前的状态
	private GameStatus game;

	void initParam() {
		currentKey = null;
		dir = null;
		maze = null;
		nextPoint = null;
		isAuto = new SimpleBooleanProperty(false);
		isAuto.addListener((v, o, n) -> {
			showHelpInfo();
		});
		status = null;
		game = GameStatus.wait;
	}

	enum GameStatus {
		over, // 结束
		running, // 运行中
		wait// 等待中
	}

	/**
	 * 游戏每隔间断时间后实行的动作
	 * 
	 * @param timeLine
	 */
	private void action(Timeline timeLine) {
		if (isAuto.get()) {// 智能运行
			simpleAI();
		} else {// 手动运行
			setPointByKey(currentKey);// ，保持上一次方向运动
		}

		status = maze.snakeMove(nextPoint);
		if (status == SnakeStatus.eat) {
			maze.randomApple();
			paintMzae();
			game = GameStatus.running;
		} else if (status == SnakeStatus.die) {// 游戏结束
			timeLine.stop();
			game = GameStatus.over;
			saveGameOverImage();
			showOverInfo();
		} else if (status == SnakeStatus.move) {
			paintMzae();
			game = GameStatus.running;
		}

		if (saveImage.trim().equals("true")) {
			saveImageLocal();
		}
	}

	private String overPath;

	private void saveGameOverImage() {
		if (saveGameOverImage.trim().equals("true")) {
			File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
			String desktopPath = desktopDir.getAbsolutePath();
			String deskdir = desktopPath + File.separatorChar + userDir;
			File d = new File(deskdir);
			if (!d.exists()) {
				d.mkdirs();
			}
			;
			overPath = deskdir + File.separatorChar + System.currentTimeMillis() + ".png";
			saveImage(new File(overPath));
		}
	}

	/**
	 * 监听按键，并执行对应的方法
	 * 
	 * @param e
	 * @param timeLine
	 */
	private void controller(KeyCode key, Timeline timeLine) {

		if (key == KeyCode.ESCAPE) {
			System.exit(0);
			return;
		}

		if (timeLine.getStatus() == Status.STOPPED) {
			if (game == GameStatus.wait) {// 游戏等待中
				// 按空格键开始游戏
				if (key == KeyCode.SPACE) {
					timeLine.play();
					game = GameStatus.running;
					currentKey = getSnakeDirection()[1];
					paintMzae();// 第一次显示
					return;
				} else if (key == KeyCode.ENTER) {
					if (isAuto.get() == false) {
						isAuto.set(true);
						return;
					} else {
						isAuto.set(false);
						currentKey = getSnakeDirection()[1];
						return;
					}
				}
			} else if (game == GameStatus.over) {
				if (key == KeyCode.SPACE) {
					initGame();// 重新开始游戏
				}
			}

		} else if (timeLine.getStatus() == Status.RUNNING) {
			if (key == KeyCode.SPACE) {
				timeLine.pause();
				showHelpInfo();
				return;
			} else if (key == KeyCode.P) {
				timeLine.pause();
				return;
			}
		} else if (timeLine.getStatus() == Status.PAUSED) {
			if (key == KeyCode.SPACE) {
				timeLine.play();
				paintMzae();
				return;
			} else if (key == KeyCode.ENTER) {
				if (isAuto.get() == false) {
					isAuto.set(true);
					return;
				} else {
					isAuto.set(false);
					currentKey = getSnakeDirection()[1];
					return;
				}
			} else if (key == KeyCode.P) {
				timeLine.play();
				paintMzae();
				return;
			}
		}
		setPointByKey(key);// 控制蛇移动的方向
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

		if (p1.getX() > p2.getX()) {
			inverseKey = KeyCode.LEFT;
			forwardKey = KeyCode.RIGHT;
		} else if (p1.getX() < p2.getX()) {
			inverseKey = KeyCode.RIGHT;
			forwardKey = KeyCode.LEFT;
		} else if (p1.getX() == p2.getX()) {
			if (p1.getY() > p2.getY()) {
				inverseKey = KeyCode.DOWN;
				forwardKey = KeyCode.UP;
			} else if (p1.getY() < p2.getY()) {
				inverseKey = KeyCode.UP;
				forwardKey = KeyCode.DOWN;
			}
		}
		return new KeyCode[] { inverseKey, forwardKey };
	}

	/**
	 * 初始化游戏
	 */
	private void initMaze() {
		maze = new Maze();// 迷宫
		maze.initWalls();// 生成迷宫的墙
		maze.initMazeSnake();// 在迷宫中间生成一条蛇
		maze.randomApple();// 在迷宫里随机生成一个苹果
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
	 * 将数组画到画布
	 * 
	 * @param m
	 */
	private void paintMzae() {
		// 画背景
		pen.setFill(Color.WHITE);
		pen.fillRect(0, 0, frame_width, frame_width);

		for (int row = 0; row < Maze.ROWSIZE; row++) {
			for (int col = 0; col < Maze.COLSIZE; col++) {
				Point point = maze.getArrayPoint(row, col);
				pen.setFill(point.getColor());
				pen.fillRect(point.getSx() * FITWIDTH + OFFSET, point.getSy() * FITHEIGHT + OFFSET, FITWIDTH - 1,
						FITHEIGHT - 1);
			}
		}

		List<Point> snakeList = maze.getSnake().getList();
		for (int i = 0; i < snakeList.size() - 1; i++) {
			pen.setFill(Color.DARKMAGENTA);
			// 画直线
			pen.strokeLine(snakeList.get(i).getSx() * FITWIDTH + OFFSET + FITWIDTH / 2,
					snakeList.get(i).getSy() * FITHEIGHT + OFFSET + FITHEIGHT / 2,
					snakeList.get(i + 1).getSx() * FITWIDTH + OFFSET + FITWIDTH / 2,
					snakeList.get(i + 1).getSy() * FITHEIGHT + OFFSET + FITHEIGHT / 2);
		}
		
		if(OFFSET==0){
			return;
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

		File savePath = new File(
				snakedirStr + File.separatorChar + dir + File.separatorChar + System.currentTimeMillis() + ".png");

		saveImage(savePath);
	}

	private void saveImage(File savePath) {
		WritableImage image = cvs.snapshot(new SnapshotParameters(), null);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", savePath);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 根据keyCode设置下一个point和保存当前KeyCode
	 * 
	 * @param key
	 */
	private void setPointByKey(KeyCode key) {
		switch (key) {
		case UP:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().getRow() - 1, maze.getSnakeHead().getCol());
			currentKey = negativeIgnore(key);
			break;
		case DOWN:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().getRow() + 1, maze.getSnakeHead().getCol());
			currentKey = negativeIgnore(key);
			break;
		case LEFT:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().getRow(), maze.getSnakeHead().getCol() - 1);
			currentKey = negativeIgnore(key);
			break;
		case RIGHT:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().getRow(), maze.getSnakeHead().getCol() + 1);
			currentKey = negativeIgnore(key);
			break;
		default:
			break;
		}
	}

	class Path {
		public Path(Point nextPoint, int value2) {
			this.point = nextPoint;
			this.value = value2;
		}

		Point point;
		int value;
	}
	

	/**
	 * 贪食蛇AI的最要逻辑<br>
	 * 如果要修改.maze.list是一个二维数组，保存当前画面的全部坐标<br>
	 * maze.snake 是一个一维数组，保存蛇的全部坐标<br>
	 * maze.apple 当前苹果的坐标<br>
	 * simpleAI主要 修改了nextPoint,告诉蛇下一步走哪里。nextPoint是maze.list中的一个点
	 */
	public void simpleAI() {
		List<Point> lt = BFS.searchLongPath(maze.snake.getHead(), maze.snake.getTail(), maze.array);
		if (lt.size() > 0) {// 可以找到尾巴

			List<Point> sa = BFS.searchShortPath(maze.snake.getHead(), maze.apple, maze.array);
			if (sa.size() > 0) {// 找的到苹果
				Maze cm = maze.clone();
				SnakeStatus s = cm.snakeMove(sa.get(1));// 假想 蛇向苹果走了一步
				List<Point> clt = BFS.searchLongPath(cm.snake.getHead(), cm.snake.getTail(), cm.array);
				if(s==SnakeStatus.eat){
					if (clt.size() > 4) {// 还能找到尾巴
						nextPoint = sa.get(1);
					} else {
						if (lt.size() < 2) {
							nextPoint = null;
						} else {
							nextPoint = lt.get(1);// 走离尾巴的最远路径
						}
					}
				}else{
					if (clt.size() > 2) {// 还能找到尾巴
						nextPoint = sa.get(1);
					} else {
						if (lt.size() < 2) {
							nextPoint = null;
						} else {
							nextPoint = lt.get(1);// 走离尾巴的最远路径
						}
					}
				}
				

			} else {// 找不到苹果
				if (lt.size() < 2) {
					nextPoint = null;
				} else {
					nextPoint = lt.get(1);// 走离尾巴的最远路径
				}
			}
		} else {
			nextPoint = null;// 游戏结束
		}
	}


	private Timeline timeLine = null;// 定时，动画

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

		// 设置按键监听
		scene.setOnKeyPressed(e -> {
			controller(e.getCode(), timeLine);
		});
	}

	private void initGame() {
		// 初始化参数
		initParam();
		// 初始化游戏
		initMaze();
		// 设置定时，每过一段时间执行一次
		initTimeLine();
		// 开始帮助信息
		showHelpInfo();
	}

	private void showHelpInfo() {
		// 画背景
		pen.setFill(Color.WHITE);
		pen.fillRect(0, 0, frame_width, frame_width);
		// 设置字体大小
		pen.setFont(new Font(15));
		// 设置文字颜色
		pen.setFill(Color.BLACK);
		// 画文字
		pen.fillText("空格: 开始/暂停", OFFSET, HEIGHT / 2);
		pen.fillText("ENTER: 开启/关闭智能", OFFSET, HEIGHT / 2 + 40);
		pen.fillText("修改速度: 修改配置文件config.properties中的speed数据", OFFSET, HEIGHT / 2 - 40);

		if (isAuto.getValue() == true) {
			pen.fillText("AI:开启", 0 + OFFSET, HEIGHT / 2 + 80);
		} else {
			pen.fillText("AI:关闭", 0 + OFFSET, HEIGHT / 2 + 80);
		}

	}

	private void showOverInfo() {
		// 画背景
		pen.setFill(Color.WHITE);
		pen.fillRect(0, 0, frame_width, frame_width);
		// 设置字体大小
		pen.setFont(new Font(25));
		// 设置文字颜色
		pen.setFill(Color.BLACK);
		// 画文字
		pen.fillText("GAME OVER", 0 + OFFSET, HEIGHT / 3);

		pen.fillText("空格从新开始", 0 + OFFSET, HEIGHT / 3 + 40);

		pen.fillText("游戏结束图片保存地址:", 0 + OFFSET, HEIGHT / 3 + 80);
		pen.setFont(new Font(14));
		pen.fillText(overPath, 0 + OFFSET, HEIGHT / 3 + 120);
	}

	private void initTimeLine() {
		timeLine = new Timeline();
		timeLine.setCycleCount(Timeline.INDEFINITE);// 设置循环
		timeLine.getKeyFrames().add(new KeyFrame(Duration.millis(speed), e -> {
			action(timeLine);
		}));
	}

}
