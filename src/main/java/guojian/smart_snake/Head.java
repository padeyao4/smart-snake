package guojian.smart_snake;

import javafx.scene.paint.Color;

public class Head extends Point {
	private static final long serialVersionUID = 4040612967902685523L;

	Head(int row, int col) {
		super(row, col);
	}

	static Color color = Color.RED;
	static int value = 2;

	@Override
	Color getColor() {
		return color;
	}

	@Override
	int getValue() {
		return value;
	}

}
