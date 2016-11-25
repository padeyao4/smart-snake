package guojian.smart_snake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * <p>
 * 利用广度优先算法寻找最短路径
 * </p>
 * 
 * @author guojian
 * @date 2016年11月20日 下午2:26:00
 * @email 1181819395@qq.com
 */
public class BFS {

	private static boolean isNear(Point a, Point b) {
		return (Math.abs(a.x - b.x) == 1 && a.y == b.y) || (a.x == b.x && Math.abs(a.y - b.y) == 1);
	}

	private static Random r = new Random();

	/**
	 * 获取二维数组中一个点周围的4个点<br>
	 * DFS中可以用到
	 * 
	 * @param array
	 * @param p1
	 * @return 返回按离p2近排序的list
	 */
	protected static List<Point> get4PointSortByDist(Point[][] array, Point p1, Point p2) {
		List<Point> sourceList = Arrays.asList(array[p1.row + 1][p1.col], array[p1.row - 1][p1.col],
				array[p1.row][p1.col - 1], array[p1.row][p1.col + 1]);
		List<Point> list = new ArrayList<>();

		list.addAll(sourceList);

		Collections.shuffle(list, r);// 防止出现循环情况
		Collections.sort(list, (o1, o2) -> {
			double dist1 = Math.pow(Math.abs(o1.x - p2.x), 2) + Math.pow(Math.abs(o1.y - p2.y), 2);
			double dist2 = Math.pow(Math.abs(o2.x - p2.x), 2) + Math.pow(Math.abs(o2.y - p2.y), 2);
			if (dist1 > dist2) {
				return 1;
			} else if (dist1 == dist2) {
				return 0;
			} else {
				return -1;
			}
		});
		return list;
	}

	/**
	 * 获取一个点周围4个点
	 * 
	 * @param array
	 * @param p1
	 * @return 返回4个点。
	 */
	private static List<Point> get4Point(Point[][] array, Point p1) {
		return Arrays.asList(array[p1.row + 1][p1.col], array[p1.row - 1][p1.col], array[p1.row][p1.col - 1],
				array[p1.row][p1.col + 1]);
	}

	/**
	 * 
	 * 在二维数组中寻找两点间的最短距离
	 * <p>
	 * 1.访问A点,将A点 标记为 访问的点(用一个集合 保存已经访问的点,Set s),判断A点是否是B点 <br>
	 * 2.如果是B，跳出循环,返回路径。<br>
	 * 3.如果不是 ,将 A周围4个点符合要求的点(不是墙，不是蛇，不是访问的点)加入 队列(队列Queue q)。并标记 加入队列的点的
	 * 父节点为A。<br>
	 * 
	 * 4.取出队列q中的一个点。当这个点为A。循环 1234
	 * </p>
	 * 
	 * @param p1
	 *            A
	 * @param p2
	 *            B
	 * @param mazeArray
	 *            二维数组
	 * @return 返回最短路径,包含开始点和结束点
	 */
	public static List<Point> searchShortPath(Point p1, Point p2, Point[][] mazeArray) {
		Queue<Point> q = new LinkedList<>();// 没访问的点
		Set<Point> s = new HashSet<>();// 已经访问的点
		Point A = p1;
		A.parent = null;
		Point B = p2;
		Point[][] array = mazeArray;
		q.add(A);// 初始化，第一个点
		while (!q.isEmpty()) {
			A = q.poll();
			s.add(A);
			if (isNear(A, B)) {
				return findPathByParent(A, B);
			} else {// 运算次数为AB最短距离的平方
				findFitPoint(q, s, A, B, array);
			}
		}
		return new LinkedList<Point>();
	}

	/**
	 * 将合适的点加入搜索
	 * 
	 * @param q
	 * @param s
	 * @param A
	 * @param B
	 * @param array
	 */
	private static void findFitPoint(Queue<Point> q, Set<Point> s, Point A, Point B, Point[][] array) {
		List<Point> list = get4Point(array, A);
		List<Point> fitPoint = new LinkedList<>();
		for (Point n : list) {// A周围4个点，把符合要求的点加入q
			Type type = n.getType();
			if (type == Type.Apple || type == Type.Cell) {
				if (s.contains(n)) {
					continue;
				} else {
					fitPoint.add(n);
				}
			} else {
				continue;
			}
		}

		Collections.sort(fitPoint, (o1, o2) -> {
			int dist1 = 0;
			int dist2 = 0;

			List<Point> list1 = get4Point(array, o1);
			for (Point p : list1) {
				if (p.type != Type.Cell) {
					dist1++;
				}
			}
			List<Point> list2 = get4Point(array, o1);
			for (Point p : list2) {
				if (p.type != Type.Cell) {
					dist2++;
				}
			}

			if (dist1 > dist2) {// 按dist由大到小排序
				return -1;
			} else if (dist1 == dist2) {
				return 0;
			} else {
				return 1;
			}
		});

		if (r.nextBoolean()) {
			Collections.shuffle(fitPoint, r);
		}

		fitPoint.forEach(p -> {
			p.setParent(A);
			q.add(p);
		});
	}

	/**
	 * 根据父节点找到整个路径
	 * 
	 * @param A
	 * @param B
	 * @return
	 */
	private static List<Point> findPathByParent(Point A, Point B) {
		List<Point> list = new LinkedList<>();
		list.add(B);
		while (true) {
			list.add(A);
			if (A.parent != null) {
				A = A.parent;
			} else {
				break;
			}
		}
		Collections.reverse(list);
		return list;
	}

	/**
	 * 搜索a点到b点的最长路径。<br>
	 * 找a点周围的4个点离b点最短距离。选出其中长度最长的路径。该路径的第一个点就是4个点中离b点最远的点。<br>
	 * 以此类推，找该路径的第一个点的周围4个点，并找其最短路径.....
	 * 
	 * @param a
	 * @param b
	 * @param pointArray
	 * @return 返回a点周围4个点离b点最短距离中 最长的路径
	 */
	public static List<Point> searchLongPath(Point a, Point b, Point[][] pointArray) {
		Point p1 = a;
		Point p2 = b;
		Point[][] array = pointArray;

		List<Point> list = get4Point(array, p1);
		List<List<Point>> pathList = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {
			Type type = list.get(i).getType();
			if (type == Type.Apple || type == Type.Cell) {
				List<Point> shortPath = searchShortPath(list.get(i), p2, array);
				shortPath.add(0, p1);
				pathList.add(shortPath);
			} else {
				continue;
			}
		}

		if (r.nextBoolean()) {
			Collections.shuffle(pathList, r);
		}

		Collections.sort(pathList, (o1, o2) -> {
			if (o1.size() > o2.size()) {
				return 1;
			} else if (o1.size() == o2.size()) {
				return 0;
			} else {
				return -1;
			}
		});

		if (pathList.size() > 0) {
			return pathList.get(pathList.size() - 1);
		} else {
			return new LinkedList<>();
		}

	}

	/**
	 * 从尾巴到头部，可以走的最远距离
	 * 
	 * @param snakeHead
	 * @param snake
	 * @param array
	 * @return
	 */
	public static List<Point> searchBodysPath(Point snakeHead, Snake snake, Point[][] array) {
		for (Point p : snake.getList()) {
			List<Point> tempPath = searchShortPath(snakeHead, p, array);
			if (tempPath.isEmpty()) {
				continue;
			} else {
				if (tempPath.size() > 1) {
					return searchLongPath(snakeHead, p, array);
				} else {
					continue;
				}
			}
		}
		return new LinkedList<>();
	}
}
