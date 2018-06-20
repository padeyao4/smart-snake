/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/19 8:30 AM
 * PS: Not easy to write code, please indicate.
 */
package guojian.smart.snake;

public class Model {

    public static final int APPLE = -1;
    public static final int BLANK = 0;
    public static final int WALL = -2;
    public static final int BODY = 1;
    public static final int TAIL = 1;

    /**
     * 贪食蛇画面中的地图
     * world为view中渲染的数据来源
     */
    public int[][] world;

    private int[][] snake;

    private int[][] walls;
    /**
     * 地图宽
     */
    public static final int COLS = Conf.WIDTH;
    /**
     * 地图高
     */
    public static final int ROWS = Conf.HEIGHT;

    /**
     * 蛇的长度
     */
    private int count;


    public Model() {
        world = initIntArray(ROWS, COLS, BLANK);
        initSnake();
        initWalls();
    }

    private void initWalls() {
        walls = initIntArray(ROWS, COLS, BLANK);
        for (int row = 0; row < ROWS; row++) {
            walls[row][0] = WALL;
            walls[row][COLS - 1] = WALL;
        }
        for (int col = 0; col < COLS; col++) {
            walls[0][col] = WALL;
            walls[ROWS - 1][col] = WALL;
        }
    }

    private void initSnake() {
        count = 2;
        snake = initIntArray(ROWS, COLS, BLANK);
        snake[ROWS / 2][COLS / 2] = TAIL;
        snake[(ROWS / 2) - 1][COLS / 2] = count;
    }


    private int[][] initIntArray(int rows, int cols, int b) {
        int[][] temp = new int[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                temp[row][col] = b;
            }
        }
        return temp;
    }

    /**
     * 更新world数据
     */
    public void update() {

    }
}
