package guojian.smart.snake;


import java.util.*;

import static guojian.smart.snake.Model.*;
import static guojian.smart.snake.Model.Direction.*;


/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/21 10:21 AM
 * PS: Not easy to write code, please indicate.
 * <p>
 * BFS算法实现贪食蛇搜索路径
 */
public class BFS extends Robot {

    Direction[] directions = Direction.values();

    int[][] tmpWorld = initIntArray(ROWS, COLS, BLANK);


    public BFS(int[][] snake, int[][] walls, int[] apples, int[][] world, int[] head) {
        super(snake, walls, apples, world, head);
        updateWorld(tmpWorld, snake, walls, apples);
    }

    @Override
    public Direction search() {
        int[] next = null;
        Direction direction = directions[new Random().nextInt(directions.length)];
        List<int[]> shortPath = searchShortestPath(head, tail, tmpWorld);
        if (shortPath != null) {
            List<int[]> applePath = searchShortestPath(head, apples, tmpWorld);
            if (applePath != null) {
                next = (applePath.size() == 0 ? apples : applePath.get(0));

            } else {
                List<int[]> fartherPath = findFartherPath(head, tail, tmpWorld);
                next = fartherPath.size() == 0 ? tail : fartherPath.get(0);
            }
        }

        if (next != null) {
            int row = next[0] - head[0];
            int col = next[1] - head[1];
            if (row == 0 && col == 1) {
                direction = RIGHT;
            } else if (row == 0 && col == -1) {
                direction = Direction.LEFT;
            } else if (row == 1 && col == 0) {
                direction = Direction.DOWN;
            } else if (row == -1 && col == 0) {
                direction = Direction.UP;
            }
        }

        return direction;
    }


    /**
     * 返回一个点周围最长的一个路径,
     * list中的第一个值为在最长路径上离src最近的点
     *
     * @param src
     * @param dst
     * @return
     */
    public static List<int[]> findFartherPath(int[] src, int[] dst, int[][] tmpWorld) {
        List<List<int[]>> list = new ArrayList<>(4);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Math.abs(Math.abs(i) - Math.abs(j)) == 1) {
                    List tmp = searchShortestPath(new int[]{src[0] + i, src[1] + j}, dst, tmpWorld);
                    if (tmp != null) {
                        list.add(tmp);
                    }
                }
            }
        }
        list.sort((o1, o2) -> o2.size() - o1.size());

        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 寻找最短路径，返回值为 起始坐标到目标坐标的队列，不包含目标坐标和起始坐标
     *
     * @param src 起始坐标
     * @param dst 目标坐标
     * @return 返回null时表示，没有路径；返回长度为0的列表时，表示目标就在跟前.
     */
    public static List<int[]> searchShortestPath(int[] src, int[] dst, int[][] tmpWorld) {

        List<int[]> path = new ArrayList<>(COLS * ROWS);

        Map<Integer, Integer> m = new HashMap<>();
        Queue<int[]> q = new LinkedList();
        Set<Integer> s = new HashSet();

        q.offer(src);
        m.put(src[0] * COLS + src[1], -1);
        while (q.size() > 0) {
            int[] n = q.poll();
            if ((n[0] == dst[0] && Math.abs(n[1] - dst[1]) == 1)
                    || (n[1] == dst[1] && Math.abs(n[0] - dst[0]) == 1)) {
                int key = n[0] * COLS + n[1];
                while (key != -1) {
                    path.add(new int[]{key / COLS, key % COLS});
                    key = m.get(key);
                }
                Collections.reverse(path);
                return path;
            } else {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (Math.abs(Math.abs(i) - Math.abs(j)) == 1) {
                            int index = (n[0] + i) * COLS + n[1] + j;
                            int v = tmpWorld[n[0] + i][n[1] + j];
                            if ((v == BLANK || v == APPLE) && !s.contains(index)) {
                                q.offer(new int[]{n[0] + i, n[1] + j});
                                s.add(index);
                                m.put(index, n[0] * COLS + n[1]);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

}
