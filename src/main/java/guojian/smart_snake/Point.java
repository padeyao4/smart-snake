package guojian.smart_snake;

import java.io.Serializable;

import javafx.scene.paint.Color;

public abstract class Point implements Serializable{
	private static final long serialVersionUID = -1089066798225347306L;
	int col;// 数组 列
	int row;// 数组 行

	int x;// 数学坐标
	int y;// 数学坐标

	int sx;// 屏幕坐标x
	int sy;// 屏幕坐标y

	/**
	 * @param col 
	 * @param row
	 */
	Point(int row, int col) {
		this.col = col;
		this.row = row;

		this.x = col;
		this.y = Maze.ROWSIZE - 1 - row;

		this.sx = col;
		this.sy = row;
	}

	public int getSx() {
		return sx;
	}

	public int getSy() {
		return sy;
	}

	public int getCol() {
		return col;
	}

	abstract Color getColor();

	public int getRow() {
		return row;
	}

	abstract int getValue();

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "x:" + x + " ,y:" + y;
	}
	
}
