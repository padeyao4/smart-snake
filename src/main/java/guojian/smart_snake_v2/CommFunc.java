package guojian.smart_snake_v2;

public class CommFunc {

	public CommFunc() {
		super();
	}

	/**
	 * 将坐标转换为索引
	 * 
	 * @param col
	 * @param row
	 * @return
	 */
	protected static int coordToIndex(int col, int row) {
		int index = col + row * Config.MAZE_COLS;
		return index;
	}

	/**
	 * @param index
	 * @return int[2] row col
	 */
	protected static int[] indexToCoord(int index) {
		int row = index / Config.MAZE_COLS;
		int col = index % Config.MAZE_COLS;
		return new int[] { row, col };
	}

}