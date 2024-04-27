package guojian.core;

import java.util.*;

import static java.lang.Math.abs;

public class BrainImpl implements Brain {

    private static List<Point> getPath(Point p, List<Point> path, Map<Point, Point> map) {
        while (p != null) {
            path.add(p);
            p = map.get(p);
        }
        Collections.reverse(path);
        return path;
    }

    private static void neighbors(Snake snake, Set<Point> visited, Queue<Snake> que, Map<Point, Point> map) {
        var cellTypes = snake.getCellTypes();
        var p = snake.head();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (abs(abs(i) - abs(j)) == 1) {
                    Point next = new Point(p.row + i, p.col + j);
                    CellType value = cellTypes[next.row][next.col];
                    if (value != CellType.WALL && value != CellType.SNAKE && !visited.contains(next)) {
                        visited.add(next);
                        var clone = snake.clone();
                        clone.move(next);
                        que.offer(clone);
                        map.put(next, p);
                    }
                }
            }
        }
    }

    private static boolean isNear(Point p1, Point p2) {
        return (abs(p1.row - p2.row) == 1 && p1.col == p2.col) ||
                (abs(p1.col - p2.col) == 1) && p1.row == p2.row;
    }

    /**
     * 搜索全图，找到所有路径
     */
    private List<Point> dfs(Snake snake, List<Point> directions, Point dst) {

        return List.of();
    }

    /**
     * 查找蛇到食物的最短距离
     */
    private List<Point> bfs(Snake snake, Point dst) {
        List<Point> paths = new LinkedList<>(); // 返回的最短路径
        Queue<Snake> que = new LinkedList<>();
        Map<Point, Point> map = new HashMap<>(); // 保存上一步和下一步的先后顺序
        Set<Point> visited = new HashSet<>(); // 保存访问过的记录

        var src = snake.head();
        que.offer(snake);
        map.put(src, null);
        visited.add(src);

        paths.add(dst);

        while (!que.isEmpty()) {
            var s = que.poll();
            if (isNear(s.head(), dst)) {
                return getPath(s.head(), paths, map);
            } else {
                neighbors(s, visited, que, map);
            }
        }
        return null;
    }

    private List<Point> normalPath(Snake snake) {
        var path = bfs(snake, snake.getFood());
        if (path == null) return null;
        var directions = Point.directions(path);
        if (Point.scoreDirections(directions) == 0) return directions;
        return null;
    }

    @Override
    public Optional<List<Point>> search(Snake snake) {
        if (snake.food().isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(bfs(snake, snake.getFood()));
        }
    }

    record PathAndSnake(List<Point> path, Snake snake) {
    }
}
