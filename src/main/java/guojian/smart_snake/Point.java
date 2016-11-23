package guojian.smart_snake;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class Point implements Serializable,Cloneable{
	private static final long serialVersionUID = -1089066798225347306L;
	int col;// 数组 列
	int row;// 数组 行

	int x;// 数学坐标
	int y;// 数学坐标

	int sx;// 屏幕坐标x
	int sy;// 屏幕坐标y
	
	Color color;//颜色
	Point parent;
	
	Type type;//种类
	

	public Point(int row,int col,Type type) {
		this(row, col);
		changeType(type);
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

		this.sx = col;
		this.sy = row;
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
		return sx;
	}
	public void setSx(int sx) {
		this.sx = sx;
	}
	public int getSy() {
		return sy;
	}
	public void setSy(int sy) {
		this.sy = sy;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Point getParent() {
		return parent;
	}
	public void setParent(Point parent) {
		this.parent = parent;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return type+"(x:" + x + ",y:" + y+")";
	}
	public void changeType(Type type) {
		this.type=type;
		switch (type) {
		case Head:
			this.color=Color.RED;
			break;
		case Body:
			this.color=Color.BURLYWOOD;
			break;
		case Tail:
			this.color=Color.BURLYWOOD;
			break;
		case Apple:
			this.color=Color.GREEN;
			break;
		case Wall:
			this.color=Color.DARKCYAN;
			break;
		case Cell:
			this.color=Color.BLACK;
			break;
		default:
			break;
		}
	}
	@Override
	protected Object clone() {
		Point point = new Point(this.row, this.col, this.type);
		return point;
	}
}
