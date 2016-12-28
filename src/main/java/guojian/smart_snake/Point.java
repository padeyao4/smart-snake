package guojian.smart_snake;

import java.io.Serializable;

import javafx.scene.paint.Color;

/**
 * @author guojian
 * @date 2016年11月23日 下午11:42:05
 * @email 1181819395@qq.com
 */
public class Point implements Serializable, Cloneable {
	private static final long serialVersionUID = -1089066798225347306L;
	int col;// 数组 列
	int row;// 数组 行

	int x;// 数学坐标
	int y;// 数学坐标

	Point parent;
	
	Type type;// 种类

	public Point(int row, int col, Type type) {
		this(row, col);
		this.type = type;
	}
	
	public void setParent(Point parent){
		this.parent=parent;
	}

	/**
	 * @param col
	 * @param row
	 */
	Point(int row, int col) {
		this.col = col;
		this.row = row;

		this.x = col;
		this.y = Maze.ROWSIZE - 1 - row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSx() {
		return col;
	}


	public int getSy() {
		return row;
	}


	public Color getColor() {
		Color color;

		switch (this.type) {
		case Head:
			color = Color.RED;
			break;
		case Body:
			color = Color.BURLYWOOD;
			break;
		case Tail:
			color = Color.BURLYWOOD;
			break;
		case Apple:
			color = Color.GREEN;
			break;
		case Wall:
			color = Color.DARKCYAN;
			break;
		case Cell:
			color = Color.BLACK;
			break;
		default:
			color = Color.BLACK;
			break;
		}
		return color;
	}


	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type + "(x:" + x + ",y:" + y + ")";
	}


	@Override
	protected Point clone() {
		Point point = new Point(this.row, this.col, this.type);
		return point;
	}
}
