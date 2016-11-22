package guojian.smart_snake;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
	private static final int WIDTH = 500;// 游戏区域画面宽度
	private static final int HEIGHT = 500;// 游戏区域画面高度
	private static final int OFFSET = WIDTH / Maze.COLSIZE;
	private static final int frame_width = WIDTH + 2 * OFFSET;// 画面宽度
	private static final int frame_height = HEIGHT + 2 * OFFSET;// 画面高度

	private static int speed = 500;

	private GraphicsContext pen;// 画笔
	private Scene scene;// 场景

	private Maze maze;// 蛇，墙，苹果构成的迷宫
	private Point nextPoint;// 蛇一下步移动的点

	private Canvas cvs;

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

	/**
	 * 游戏每隔间断时间后实行的动作
	 * 
	 * @param timeLine
	 */
	private void action(Timeline timeLine) {
		if (isArtificial) {
			try {
				computerSnakeRun();
			} catch (Exception e) {
				e.printStackTrace();
				timeLine.pause();
				timeLine.stop();
				timeLine = null;
				nextPoint = null;
				return;
			}
		} else {
			setPointAndCurrentDirectByKey(currentKey);// 保持上一次方向运动
		}
		Game status = maze.snakeMove(nextPoint);
		if (status == Game.eatApple) {
			maze.randomApple();
		} else if (status == Game.over) {// 当游戏返回over时，结束游戏
			/* paintHelpInfo("GAME OVER", 80, 40); */
			timeLine.pause();
			timeLine.stop();
			timeLine = null;
			nextPoint = null;
			return;
		}
		paintArray(maze);
		saveImageLocal();
	}

	
	private String imageDir;
	/**
	 * 保存到本地
	 */
	private void saveImageLocal() {
		String snakedirStr = System.getProperty("user.home")+File.separatorChar+"snakeImage";
		File snakedir =  new File(snakedirStr);
		if(!snakedir.exists()){
			snakedir.mkdirs();
		}
		if(imageDir==null){
			imageDir=System.currentTimeMillis()+"";
			new File(snakedirStr+File.separatorChar+imageDir).mkdir();
		}
		
		WritableImage image = cvs.snapshot(new SnapshotParameters(), null);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(snakedirStr+File.separatorChar+imageDir+File.separatorChar+System.currentTimeMillis()+".png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * 自动执行
	 * 
	 * @throws Exception
	 */
	private void computerSnakeRun() throws Exception {
		Path path = BFS.findAppleShortPath(maze.getSnakeHead(), maze.getApple(), maze.getArray());

		if (path.hasPath()) {// 找到苹果
			Maze cloneMaze = (Maze) maze.clone();// 复制Maze
			cloneMaze.snakeMove(path.getNextPoint());

			Path cpt = BFS.findTailShortPath(cloneMaze.getSnakeHead(), cloneMaze.getSnakeTail(), cloneMaze.getArray());
			if (cpt.hasPath()) {// 蛇向苹果走一步后，可以找到蛇尾
				System.out.println("//蛇向苹果走一步后，可以找到蛇尾");
				nextPoint = path.getNextPoint();
			} else {// 蛇向苹果走一步后，不能找到蛇尾
				Path tailPath = BFS.findTailShortPath(maze.getSnakeHead(), maze.getSnakeTail(), maze.getArray());
				if (tailPath.hasPath()) {// 蛇找的到苹果，也找的到尾巴。如果蛇向苹果走一步就找不到尾巴
					// 走蛇尾的最远距离
					Path farPath = BFS.findFarTailPath(maze.getSnakeHead(), maze.getSnakeTail(), maze.getArray());
					if (farPath.getSize() == 2) {// 蛇走蛇尾的最长距离，走一步就吃到蛇尾
						System.out.println("蛇走蛇尾的最长距离，走一步就吃到蛇尾,蛇走蛇身的最长距离");
						// 蛇走蛇身的最长距离
						Path farBodyPath = BFS.findFarBodyPath(maze.getSnakeHead(), maze.getSnake().getBodys(),
								maze.getArray());
						nextPoint = farBodyPath.getNextPoint();
					} else {// 蛇走蛇尾的最远距离
						nextPoint = farPath.getNextPoint();
					}
				} else {// 真蛇找不到蛇尾 找的到苹果
						// 走离蛇头最远的蛇身的最远距离
					System.out.println("//真蛇找不到蛇尾 找的到苹果// 走离蛇头最远的蛇身的最远距离");
					Path farBodyPath = BFS.findFarBodyPath(maze.getSnakeHead(), maze.getSnake().getBodys(),
							maze.getArray());
					nextPoint = farBodyPath.getNextPoint();
				}
			}
		} else {// 找不到苹果
			Path tailPath = BFS.findTailShortPath(maze.getSnakeHead(), maze.getSnakeTail(), maze.getArray());
			if (tailPath.hasPath()) {// 找不到苹果，找的到蛇尾
				// 离蛇尾的最远距离
				Path farPath = BFS.findFarTailPath(maze.getSnakeHead(), maze.getSnakeTail(), maze.getArray());
				if (farPath.getSize() == 2) {// 找不到苹果，蛇头走蛇尾最远距离走一步咬到蛇尾
					// 走离蛇身的最远距离
					Path farBodyPath = BFS.findFarBodyPath(maze.getSnakeHead(), maze.getSnake().getBodys(),
							maze.getArray());
					nextPoint = farBodyPath.getNextPoint();
				} else {// 找不到苹果，蛇头走蛇尾最远距离，且咬不到蛇尾
					nextPoint = farPath.getNextPoint();
				}
			} else {// 找不到苹果，找不到蛇尾
				// 走离蛇头最远的蛇身的最远距离
				Path farBodyPath = BFS.findFarBodyPath(maze.getSnakeHead(), maze.getSnake().getBodys(),
						maze.getArray());
				nextPoint = farBodyPath.getNextPoint();
			}
		}
	}

	private KeyCode currentKey;

	/**
	 * 监听按键，并执行对应的方法
	 * 
	 * @param e
	 * @param timeLine
	 */
	private void controller(KeyCode key, Timeline timeLine) {
		if (nextPoint == null && timeLine.getStatus() == Status.STOPPED) {
			/*
			 * if (key == KeyCode.UP || key == KeyCode.DOWN || key ==
			 * KeyCode.LEFT || key == KeyCode.RIGHT) { timeLine.play(); }
			 */
			if (key == KeyCode.ENTER) {
				if (isArtificial == false) {
					timeLine.play();
					isArtificial = true;
				}
			}
		}

		setPointAndCurrentDirectByKey(key);// 方向键控制下一个点

	}

	/**
	 * 根据keyCode设置下一个point和保存当前KeyCode
	 * 
	 * @param key
	 */
	private void setPointAndCurrentDirectByKey(KeyCode key) {
		switch (key) {
		case UP:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().row - 1, maze.getSnakeHead().col);
			currentKey = key;
			break;
		case DOWN:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().row + 1, maze.getSnakeHead().col);
			currentKey = key;
			break;
		case LEFT:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().row, maze.getSnakeHead().col - 1);
			currentKey = key;
			break;
		case RIGHT:
			nextPoint = maze.getArrayPoint(maze.getSnakeHead().row, maze.getSnakeHead().col + 1);
			currentKey = key;
			break;
		default:
			break;
		}
	}

	public static void main(String[] args) {
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

		int w = WIDTH / Maze.COLSIZE;
		int h = HEIGHT / Maze.COLSIZE;

		for (int row = 0; row < Maze.ROWSIZE; row++) {
			for (int col = 0; col < Maze.COLSIZE; col++) {
				Point point = m.getArrayPoint(row, col);
				pen.setFill(point.getColor());
				pen.fillRect(point.getSx() * w + OFFSET, point.getSy() * h + OFFSET, w - 1, h - 1);
			}
		}

		List<Point> bodys = maze.getSnake().getBodys();
		for (int i = 0; i < bodys.size() - 1; i++) {
			pen.setFill(Color.DARKMAGENTA);
			pen.strokeLine(bodys.get(i).sx * OFFSET + OFFSET / 2 + OFFSET,
					bodys.get(i).sy * OFFSET + OFFSET / 2 + OFFSET, bodys.get(i + 1).sx * OFFSET + OFFSET / 2 + OFFSET,
					bodys.get(i + 1).sy * OFFSET + OFFSET / 2 + OFFSET);
		}
		for (int x = 0; x < Maze.COLSIZE; x++) {
			// 设置字体大小
			pen.setFont(new Font(15));
			// 设置文字颜色
			pen.setFill(Color.BLACK);
			// 画文字
			pen.fillText(x + "", (x + 1) * OFFSET, frame_height);
		}
		for (int y = 0; y < Maze.ROWSIZE; y++) {
			// 设置字体大小
			pen.setFont(new Font(15));
			// 设置文字颜色
			pen.setFill(Color.BLACK);
			// 画文字
			pen.fillText(y + "", 0, frame_height - OFFSET * (y + 1));
		}

	}

}
