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

	/**
	 * 获取二维数组中一个点周围的4个点<br>
	 * DFS中可以用到
	 * @param array
	 * @param p1
	 * @return 返回按离p2近排序的list
	 */
	protected static List<Point> get4PointSortByDist(Point[][] array, Point p1,Point p2) {
		List<Point> sourceList = Arrays.asList(new Point[] { array[p1.row + 1][p1.col], array[p1.row - 1][p1.col],
				array[p1.row][p1.col - 1], array[p1.row][p1.col + 1] });
		List<Point> list = new ArrayList<>();
		list.addAll(sourceList);
		Collections.shuffle(list, new Random());//防止出现循环情况
		Collections.sort(list, (o1, o2) -> {
			double dist1 = Math.pow(Math.abs(o1.x-p2.x), 2)+Math.pow(Math.abs(o1.y-p2.y), 2);
			double dist2 = Math.pow(Math.abs(o2.x-p2.x), 2)+Math.pow(Math.abs(o2.y-p2.y), 2);
			if(dist1>dist2){
				return 1;
			}else if(dist1==dist2){
				return 0;
			}else{
				return -1;
			}
		});
		return list;
	}

	/**
	 * 获取一个点周围4个点
	 * @param array
	 * @param p1
	 * @return 返回4个点。顺序随机
	 */
	private static List<Point> get4Point(Point[][] array, Point p1) {
		List<Point> sourceList = Arrays.asList(new Point[] { array[p1.row + 1][p1.col], array[p1.row - 1][p1.col],
				array[p1.row][p1.col - 1], array[p1.row][p1.col + 1] });
		List<Point> list = new ArrayList<>();
		list.addAll(sourceList);
		Collections.shuffle(list, new Random());//防止出现循环情况
		return list;
	}
	
	/**
	 * 
	 *  在二维数组中寻找两点间的最短距离
	 * <p>
	 * 1.访问A点,将A点 标记为 访问的点(用一个集合 保存已经访问的点,Set s),判断A点是否是B点 <br>
	 * 2.如果是B，跳出循环,返回路径。<br>
	 * 3.如果不是 ,将 A周围4个点符合要求的点(不是墙，不是蛇，不是访问的点)加入 队列(队列Queue q)。并标记 加入队列的点的
	 * 父节点为A。<br>
	 * 
	 * 4.取出队列q中的一个点。当这个点为A。循环 1234
	 * </p>
	 * @param p1 A
	 * @param p2 B
	 * @param mazeArray 二维数组
	 * @return 返回最短路径
	 */
	public static Path searchShortPath(Point p1, Point p2, Point[][] mazeArray) {
		Queue<Point> q = new LinkedList<>();// 没访问的点
		Set<Point> s = new HashSet<>();// 已经访问的点
		Point A =  p1.clone();
		Point B =  p2.clone();
		Point[][] array = mazeArray.clone();
		q.add(A);// 初始化，第一个点
		while (!q.isEmpty()) {
			A = q.poll();
			s.add(A);
			if (isNear(A, B)) {
				List<Point> l = new ArrayList<>();
				l.add(B);
				while (true) {
					l.add(A);
					if (A.parent == null) {
						break;
					} else {
						A=A.parent;
					}
				}
				Collections.reverse(l);
				return new Path(l);
			} else {
				List<Point> list = get4PointSortByDist(array, A,B);
				for(Point n:list){// A周围4个点，把符合要求的点加入q
					Type type = n.getType();
					if (type != Type.Head && type != Type.Body && type != Type.Tail && type != Type.Wall
							&& !s.contains(n)) {
						n.setParent(A);
						q.add(n);
					}else{
						continue;
					}
				}
			}
		}
		return new Path(null);
	}

	/**
	 * <p>路径</p>
	 * <p>list为搜索到的最短路径,包含起始点和结束点</p>
	 * @author guojian
	 * @date 2016年11月24日 上午12:18:36
	 * @email 1181819395@qq.com
	 */
	static class Path {
		List<Point> list;
		
		public Path(List<Point> list) {
			if (list == null) {
				this.list = new ArrayList<>();
			} else {
				this.list = list;
			}
		}

		/**
		 * @return 最短路径长度
		 */
		public int size() {
			return list.size();
		}

		/**
		 * @return 返回起始点的下一个点
		 */
		public Point getNextPoint() {
			if(list.size()<2){
				return null;
			}else{
				return list.get(1);
			}
			
		}

		public boolean isEmpty() {
			return list.isEmpty();
		}
	}


	/**
	 * 搜索a点到b点的最长路径。<br>
	 * 找a点周围的4个点离b点最短距离。选出其中长度最长的路径。该路径的第一个点就是4个点中离b点最远的点。<br>
	 * 以此类推，找该路径的第一个点的周围4个点，并找其最短路径.....
	 * @param a
	 * @param b
	 * @param pointArray
	 * @return 返回a点周围4个点离b点最短距离中 最长的路径
	 */
	public static Path searchLongPath(Point a, Point b, Point[][] pointArray) {
		Point p1 = a.clone();
		Point p2 = b.clone();
		Point[][] array = pointArray.clone();
		
		List<Point> list = get4Point(array, p1);
		List<Path> pathList = new ArrayList<>();
		
		for(int i=0;i<list.size();i++){
			Type type = list.get(i).getType();
			if (type ==Type.Apple|| type==Type.Cell) {
				Path searchShortPath = searchShortPath(list.get(i), p2, array);
				searchShortPath.list.add(0, p1);
				pathList.add(searchShortPath);
			}else{
				continue;
			}
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
			return new Path(null);
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
	public static Path searchBodysPath(Point snakeHead, Snake snake, Point[][] array) {
		for (Point p : snake.getList()) {
			Path tempPath = searchShortPath(snakeHead, p, array);
			if (tempPath.isEmpty()) {
				continue;
			} else {
				if (tempPath.size() > 1) {
					tempPath=null;
					return  searchLongPath(snakeHead, p, array);
				} else {
					continue;
				}
			}
		}
		return new Path(null);
	}
}
