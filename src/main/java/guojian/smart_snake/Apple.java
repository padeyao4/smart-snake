package guojian.smart_snake;

import javafx.scene.paint.Color;

public class Apple extends Point{
	private static final long serialVersionUID = -6829619381101469634L;
	static int value=4;
	static Color color=Color.GREEN;

	Apple(int row, int col) {
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
