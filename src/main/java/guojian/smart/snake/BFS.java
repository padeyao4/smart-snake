package guojian.smart.snake;


import java.util.*;

/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/21 10:21 AM
 * PS: Not easy to write code, please indicate.
 * <p>
 * BFS算法实现贪食蛇搜索路径
 */
public class BFS extends Robot {


    public BFS(int[][] snake, int[][] walls, int[] apples, int[][] world, int[] head) {
        super(snake, walls, apples, world, head);
    }

    @Override
    public Model.Direction search() {
        return null;
    }


    int[][] tmpWorld = Model.initIntArray(Model.ROWS, Model.COLS, Model.BLANK);

    /**
     * 寻找最短路径，返回值为 目标坐标到起始坐标路径的路径，不包含目标坐标和起始坐标
     *
     * @param src 起始坐标
     * @param dst 目标坐标
     * @return 返回null时表示，没有路径；返回长度为0的列表时，表示目标就在跟前.
     */
    List<int[]> searchShortestPath(int[] src, int[] dst) {
        Model.updateWorld(tmpWorld, snake, walls, apples);
        List<int[]> path = new ArrayList<>(Model.COLS * Model.ROWS);

        Map<Integer, Integer> m = new HashMap<>();
        Queue<int[]> q = new LinkedList();
        Set<Integer> s = new HashSet();

        q.offer(src);
        m.put(src[0] * Model.COLS + src[1], -1);
        while (q.size() > 0) {
            int[] n = q.poll();
            if ((n[0] == dst[0] && Math.abs(n[1] - dst[1]) == 1)
                    || (n[1] == dst[1] && Math.abs(n[0] - dst[0]) == 1)) {
                int key = n[0] * Model.COLS + n[1];
                while (key != -1) {
                    path.add(new int[]{key / Model.COLS, key % Model.COLS});
                    key = m.get(key);
                }
                return path;
            } else {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (Math.abs(Math.abs(i) - Math.abs(j)) == 1) { //3
                            int index = (n[0] + i) * Model.COLS + n[1] + j;
                            int v = tmpWorld[n[0] + i][n[1] + j];
                            if ((v == Model.BLANK || v == Model.APPLE) && !s.contains(index)) {
                                q.offer(new int[]{n[0] + i, n[1] + j});
                                s.add(index);
                                m.put(index, n[0] * Model.COLS + n[1]);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

}
