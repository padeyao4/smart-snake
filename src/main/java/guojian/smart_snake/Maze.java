package guojian.smart_snake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private static final long serialVersionUID = -4768606043555585626L;
	public static final int COLSIZE = 10;
	public static final int ROWSIZE = 10;

	private Apple apple;// 苹果
	private Point[][] array;//
	private Snake snake;

	public Maze() {
		array = new Point[ROWSIZE][COLSIZE];
		// 初始化二维数组。
		for (int row = 0; row < ROWSIZE; row++) {
			for (int col = 0; col < COLSIZE; col++) {
				array[row][col] = new Cell(row, col);
			}
		}
	}

	public Object clone() {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);

				oos.writeObject(this);

				// 反序列化
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bis);

				return ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	}

	public Apple getApple() {
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

	public Tail getSnakeTail() {
		return snake.getTail();
	}

	/**
	 * 初始化迷宫中的蛇
	 */
	public void initMazeSnake() {
		Head head = new Head((int) (ROWSIZE / 2), (int) (COLSIZE / 2));
		Body body = new Body(head.row + 1, head.col);
		Tail tail = new Tail(body.row + 1, body.col);
		snake = new Snake();
		snake.addHead(head);
		snake.addBody(body);
		snake.addTail(tail);
		snakeIntoMaze();
	}

	public void initWalls() {
		for (int col = 0; col < Maze.COLSIZE; col++) {
			setPoint(new Wall(0, col));
			setPoint(new Wall(Maze.ROWSIZE - 1, col));
		}

		for (int row = 1; row < Maze.ROWSIZE - 1; row++) {
			setPoint(new Wall(row, 0));
			setPoint(new Wall(row, Maze.COLSIZE - 1));
		}
	}

	/**
	 * 清理迷宫中的蛇
	 */
	private void mazeClearSnake() {
		setPoint(new Cell(snake.getHead().row, snake.getHead().col));
		for (Point p : snake.getBodys()) {
			setPoint(new Cell(p.row, p.col));
		}
		setPoint(new Cell(snake.getTail().row, snake.getTail().col));
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
				if (point.getValue() == Cell.value) {
					list.add(point);
				}
			}
		}
		Point point = list.get(new Random().nextInt(list.size()));
		apple = new Apple(point.row, point.col);
		setPoint(apple);
	}

	private void setPoint(Point p) {
		array[p.row][p.col] = p;
	}

	/**
	 * 将蛇画到迷宫中
	 */
	private void snakeIntoMaze() {
		setPoint(snake.getHead());
		for (Point p : snake.getBodys()) {
			setPoint(p);
		}
		setPoint(snake.getTail());
	}

	public Game snakeMove(Point point) {
		if (point == null) {
			return Game.over;
		}
		int value = point.getValue();
		if (value == Body.value || value == Tail.value || value == Wall.value) {
			return Game.over;
		} else if (value == Apple.value) {
			mazeClearSnake();
			snake.eatApple(point);
			snakeIntoMaze();
			return Game.eatApple;
		} else if (value == Cell.value) {
			mazeClearSnake();
			snake.move(point);
			snakeIntoMaze();
			return Game.move;
		}
		return Game.exception;
	}
}