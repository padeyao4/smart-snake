package guojian.smart_snake;

import javafx.scene.paint.Color;

public class Tail extends Point {

	private static final long serialVersionUID = -7011177630450068920L;

	static Color color = Color.BLUE;

	static int value = 1;

	Tail(int row, int col) {
		super(row, col);
	}

	@Override
	Color getColor() {
		return color;
	}

	@Override
	int getValue() {
		return value;
	}

}
