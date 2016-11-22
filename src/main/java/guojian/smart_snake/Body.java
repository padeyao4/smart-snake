package guojian.smart_snake;

import javafx.scene.paint.Color;

public class Body extends Point {
	private static final long serialVersionUID = -8691316535370084636L;

	Body(int row, int col) {
		super(row, col);
	}
	static Color color = Color.BURLYWOOD;

	static int value = 3;
	
	@Override
	Color getColor() {
		return color;
	}
	@Override
	int getValue() {
		return value;
	}

}
