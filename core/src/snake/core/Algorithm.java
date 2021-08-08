package snake.core;


import java.util.*;


import static java.lang.Math.abs;

public class Algorithm {

    /**
     * 寻找最短路径
     *
     * @param src     开始地址
     * @param dst     目标地址
     * @param world
     * @param exclude 要排查的路径上的点,如没有要排查的点 传入 null
     * @return 返回src 和 dst 路径的列表，包含src和dst。如果没有找到路径返回None。
     * 返回的列表中的第一个元素是src,最后一个元素是dst
     */
    public static LinkedList<Point> findShortestPath(Point src, Point dst, Cell[][] world, Set<Point> exclude) {
        LinkedList<Point> paths = new LinkedList<>(); // 返回的最短路径
        Queue<Point> q = new LinkedList<>();
        Map<Point, Point> m = new HashMap<>(); // 保存上一步和下一步的先后顺序
        Set<Point> s = new HashSet<>(); // 保存访问过的记录

        q.offer(src);
        m.put(src, null);
        s.add(src);
        if (exclude != null)
            s.addAll(exclude);

        paths.add(dst);

        while (q.size() > 0) {
            Point n = q.poll();
            if ((abs(n.row - dst.row) == 1 && n.col == dst.col) ||
                    (abs(n.col - dst.col) == 1) && n.row == dst.row) {
                Point k = n;
                while (k != null) {
                    paths.add(k);
                    k = m.get(k);
                }
                Collections.reverse(paths);
                return paths;
            } else {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (abs(abs(i) - abs(j)) == 1) {
                            Point o = new Point(n.row + i, n.col + j);
                            Cell value = world[o.row][o.col];
                            if (value != Cell.WALL && value != Cell.SNAKE && !s.contains(o)) {
                                s.add(o);
                                q.offer(o);
                                m.put(o, n);
                            }
                        }
                    }
                }
            }

        }

        return null;
    }

    /**
     * 复制一份迷宫世界，根据给出的路径，让复制出的世界按照给出的路径运行。<br/>
     * <p>
     * 路径中如果 包含 除head和apple以外的其他点，那么蛇不会走到终点，会返回中途的平行世界
     *
     * @param gameManager
     * @param path        路径必须包含蛇头(head)到目标点，目标点可以是 snake ,apple,walls ,blank
     * @return 返回一个平行世界
     */
    public static GameManager getShadowModelByPath(GameManager gameManager, LinkedList<Point> path) {


        var shadowGameManager = gameManager.cp();
        if (path == null) return shadowGameManager;
        var shadowPath = new LinkedList<Point>();
        for (Point c : path) {
            shadowPath.add(c.cp());
        }

        shadowPath.remove(0);

        for (Point p : shadowPath) {
            if (p.equals(shadowGameManager.getApple()))
                shadowGameManager.eatApple(p);
            else if (shadowGameManager.getWalls().contains(p) || shadowGameManager.getSnakes().contains(p)) {
                break;
            } else shadowGameManager.move(p);
        }

        return shadowGameManager;
    }


    /**
     * 搜索最长路径
     *
     * @param src
     * @param dst
     * @param world
     * @return 如果有最长路径返回包含src 和dst的路径，否则返回None
     */
    public static LinkedList<Point> findFarthestPath(Point src, Point dst, Cell[][] world) {
        ArrayList<LinkedList<Point>> paths = new ArrayList<>(4);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (abs(abs(i) - abs(j)) == 1) {
                    Point o = new Point(src.row + i, src.col + j);
                    Cell v = world[o.row][o.col];
                    if (v != Cell.WALL && v != Cell.SNAKE) {
                        LinkedList<Point> path = findShortestPath(o, dst, world, null);
                        if (path != null) {
                            path.add(0, src);
                            paths.add(path);
                        }
                    }

                }
            }
        }
        // 由小到大排序
        Collections.sort(paths, Comparator.comparingInt(LinkedList::size));
        return paths.size() == 0 ? null : paths.get(paths.size() - 1);
    }


    /**
     * 找到一条可以将 3个点串联起来的 路径。
     * <p>
     * 每运动一次model会跟着变化
     *
     * @param src         第一个点
     * @param mid         中间点
     * @param dst         最后一个点
     * @param gameManager
     * @return 返回 一条路径 从 src出发 穿过 mid 在 dst结束
     */
    public static LinkedList<Point> findSeriesPath(Point src, Point mid, Point dst, GameManager gameManager) {
        LinkedList<Point> srcToMidPath = findShortestPath(src, mid, gameManager.getWorld(), null);
        if (srcToMidPath == null) return null;
        GameManager shadowGameManager = getShadowModelByPath(gameManager, srcToMidPath);
        LinkedList<Point> midToDstPath = findShortestPath(mid, dst, shadowGameManager.getWorld(), null);
        if (midToDstPath == null) return null;
        srcToMidPath.removeLast();
        srcToMidPath.addAll(midToDstPath);
        return srcToMidPath;
    }

    /**
     * 找当前最好的路径
     *
     * @param head
     * @param apple
     * @param tail
     * @param gameManager
     * @return
     */
    public static LinkedList<Point> findBestPath(Point head, Point apple, Point tail, GameManager gameManager) {
        LinkedList<Point> seriesPath = findSeriesPath(head, apple, tail, gameManager);
        if (seriesPath != null) return seriesPath;
        else return findFarthestPath(head, tail, gameManager.getWorld());
    }
}
