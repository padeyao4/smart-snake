package guojian.smart.snake;


import java.util.LinkedList;
import java.util.Queue;

/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/21 10:21 AM
 * PS: Not easy to write code, please indicate.
 * <p>
 * BFS算法实现贪食蛇搜索路径
 */
public class BFSRobot extends Robot {


    public BFSRobot(int[][] snake, int[][] walls, int[] apples, int[][] world, int[] head) {
        super(snake, walls, apples, world, head);
    }

    @Override
    public Model.Direction search() {
        return null;
    }


    int[][] tmpWorld = Model.initIntArray(Model.ROWS, Model.COLS, Model.BLANK);

    void searchShortestPath() {
        Model.updateWorld(tmpWorld, snake, walls, apples);
        Queue<int[]> q = new LinkedList();
        q.offer(head);
        while (q.size() > 0) {
            int[] n = q.poll();
            if (closeTo(n, tail)) {
                break;
            }else{

            }
        }
    }

    /**
     * 判断n点和tail是否相近
     *
     * @param n
     * @param tail
     * @return
     */
    boolean closeTo(int[] n, int[] tail) {
        return (n[0] == tail[0] && Math.abs(n[1] - tail[1]) == 1) || (n[1] == tail[1] && Math.abs(n[0] - tail[0]) == 1);
    }

}
