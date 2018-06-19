/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/19 8:30 AM
 * PS: Not easy to write code, please indicate.
 */
package guojian.smart.snake;


public class Model {

    /**
     * 贪食蛇画面中的地图
     */
    Cell[][] world;
    /**
     * 地图宽
     */
    int cols = Conf.WIDTH;
    /**
     * 地图高
     */
    int rows = Conf.HEIGHT;

    enum Cell {
        header, tail, apple, body, blank, wall,
    }


    public Model() {
        world = new Cell[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                world[row][col] = Cell.blank;
            }
        }
    }

    public void update() {

    }
}
