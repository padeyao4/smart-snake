package guojian.smart_snake;

import javafx.scene.paint.Color;

public class Wall extends Point {
	private static final long serialVersionUID = -6179787214172763318L;

	Wall(int row, int col) {
		super(row, col);
	}
	
	static Color color=  Color.DARKCYAN;
	static int value=7;

	@Override
	Color getColor() {
		return color;
	}

	@Override
	int getValue() {
		return value;
	}

}
