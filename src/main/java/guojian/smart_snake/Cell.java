package guojian.smart_snake;

import javafx.scene.paint.Color;

public class Cell extends Point {
	private static final long serialVersionUID = 1308671100150992969L;


	Cell(int row, int col) {
		super(row, col);
	}
	
	static Color color=Color.BLACK;
	static int value=9;


	@Override
	Color getColor() {
		return color;
	}

	@Override
	int getValue() {
		return value;
	}

}
