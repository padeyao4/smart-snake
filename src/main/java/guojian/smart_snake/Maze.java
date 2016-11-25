package guojian.smart_snake;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * <p>
 * Title: 迷宫
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author guojian
 * @date 2016年11月18日 下午6:06:24
 * @email 1181819395@qq.com
 */
public class Maze implements Serializable, Cloneable {
	private static ResourceBundle bundle=ResourceBundle.getBundle("config");
	public static final int COLSIZE = Integer.parseInt(bundle.getString("cols"));
	public static final int ROWSIZE = Integer.parseInt(bundle.getString("rows"));
	private static final long serialVersionUID = -4768606043555585626L;

	private Point apple;// 苹果
	private Point[][] array;//
	private Snake snake;

	public Maze() {
		array = new Point[ROWSIZE][COLSIZE];
	}

	public Maze clone()   {
		Maze clone = new Maze();
		clone.apple = this.apple.clone();
		clone.snake = this.snake.clone();
		for (int row = 0; row < ROWSIZE; row++) {
			for (int col = 0; col < COLSIZE; col++) {
				clone.array[row][col] = this.array[row][col].clone();
			}
		}
		return clone;
	}

	public Point getApple() {
		return apple;
	}

	public Point[][] getArray() {
		return array;
	}

	public Point getArrayPoint(int row, int col) {
		return array[row][col];
	}

	public Snake getSnake() {
		return snake;
	}

	public Point getSnakeHead() {
		return snake.getHead();
	}

	public Point getSnakeTail() {
		return snake.getTail();
	}

	/**
	 * 初始化迷宫中的蛇
	 */
	public void initMazeSnake() {
		Point head = new Point((int) (ROWSIZE / 2), (int) (COLSIZE / 2), Type.Head);
		Point body = new Point(head.getRow() + 1, head.getCol(), Type.Body);
		Point tail = new Point(body.getRow() + 1, body.getCol(), Type.Tail);
		snake = new Snake();
		snake.add(tail);
		snake.add(body);
		snake.add(head);
		snakeIntoMaze();
	}

	/**
	 * 初始化墙
	 */
	public void initWalls() {
		// 初始化二维数组。
		for (int row = 0; row < ROWSIZE; row++) {
			for (int col = 0; col < COLSIZE; col++) {
				array[row][col] = new Point(row, col, Type.Cell);
			}
		}

		for (int col = 0; col < Maze.COLSIZE; col++) {
			setPoint(new Point(0, col, Type.Wall));
			setPoint(new Point(Maze.ROWSIZE - 1, col, Type.Wall));
		}

		for (int row = 1; row < Maze.ROWSIZE - 1; row++) {
			setPoint(new Point(row, 0, Type.Wall));
			setPoint(new Point(row, Maze.COLSIZE - 1, Type.Wall));
		}
	}

	/**
	 * 清理迷宫中的蛇
	 */
	private void mazeClearSnake() {
		setPoint(new Point(snake.getHead().getRow(), snake.getHead().getCol(), Type.Cell));
		for (Point p : snake.getList()) {
			setPoint(new Point(p.getRow(), p.getCol(), Type.Cell));
		}
	}

	/**
	 * 在二维数组中随机生成一个苹果
	 * 
	 * @return apple
	 */
	public void randomApple() {
		List<Point> list = new ArrayList<>();
		for (int row = 0; row < Maze.ROWSIZE; row++) {
			for (int col = 0; col < Maze.COLSIZE; col++) {
				Point point = array[row][col];
				if (point.getType() == Type.Cell) {
					list.add(point);
				}
			}
		}
		Point point = list.get(new Random().nextInt(list.size()));
		apple = new Point(point.getRow(), point.getCol(), Type.Apple);
		setPoint(apple);
	}

	private void setPoint(Point p) {
		array[p.getRow()][p.getCol()] = p;
	}

	/**
	 * 将蛇画到迷宫中
	 */
	private void snakeIntoMaze() {
		for (Point p : snake.getList()) {
			setPoint(p);
		}
	}

	
	public SnakeStatus snakeMove(Point point) {
		if (point == null) {
			return SnakeStatus.die;
		}
		Type type = point.getType();
		if (type == Type.Head || type == Type.Body || type == Type.Tail||type==Type.Wall) {
			return SnakeStatus.die;
		} else if (type == Type.Apple) {
			mazeClearSnake();
			snake.eatApple(point);
			snakeIntoMaze();
			return SnakeStatus.eat;
		} else if (type == Type.Cell) {
			mazeClearSnake();
			snake.move(point);
			snakeIntoMaze();
			return SnakeStatus.move;
		}
		return null;
	}
}