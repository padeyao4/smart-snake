package guojian.smart_snake;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	public static final int COLSIZE = 10;
	public static final int ROWSIZE = 10;
	private static final long serialVersionUID = -4768606043555585626L;

	private Point apple;// 苹果
	private Point[][] array;//
	private Snake snake;

	public Maze() {
		array = new Point[ROWSIZE][COLSIZE];
	}

	public Object clone() throws CloneNotSupportedException {
		/*
		 * try { ByteArrayOutputStream bos = new ByteArrayOutputStream();
		 * ObjectOutputStream oos = new ObjectOutputStream(bos);
		 * oos.writeObject(this); // 反序列化 ByteArrayInputStream bis = new
		 * ByteArrayInputStream(bos.toByteArray()); ObjectInputStream ois = new
		 * ObjectInputStream(bis); return ois.readObject(); } catch
		 * (ClassNotFoundException e) { e.printStackTrace(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		Maze clone = new Maze();
		clone.apple = (Point) this.apple.clone();
		clone.snake = (Snake) this.snake.clone();
		for (int row = 0; row < ROWSIZE; row++) {
			for (int col = 0; col < COLSIZE; col++) {
				clone.array[row][col] = (Point) this.array[row][col].clone();
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
		Point body = new Point(head.row + 1, head.col, Type.Body);
		Point tail = new Point(body.row + 1, body.col, Type.Tail);
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
		setPoint(new Point(snake.getHead().row, snake.getHead().col, Type.Cell));
		for (Point p : snake.getList()) {
			setPoint(new Point(p.row, p.col, Type.Cell));
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
		apple = new Point(point.row, point.col, Type.Apple);
		setPoint(apple);
	}

	private void setPoint(Point p) {
		array[p.row][p.col] = p;
	}

	/**
	 * 将蛇画到迷宫中
	 */
	private void snakeIntoMaze() {
		for (Point p : snake.getList()) {
			setPoint(p);
		}
	}

	public Game snakeMove(Point point) {
		if (point == null) {
			return Game.over;
		}
		Type type = point.getType();
		if (type == Type.Head || type == Type.Body || type == Type.Tail) {
			return Game.over;
		} else if (type == Type.Apple) {
			mazeClearSnake();
			snake.eatApple(point);
			snakeIntoMaze();
			return Game.eatApple;
		} else if (type == Type.Cell) {
			mazeClearSnake();
			snake.move(point);
			snakeIntoMaze();
			return Game.move;
		}
		return Game.exception;
	}
}