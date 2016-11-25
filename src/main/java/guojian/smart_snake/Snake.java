package guojian.smart_snake;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 蛇<br>
 * list第一个元素为蛇尾，最后一个为蛇头
 * @author guojian
 * @date 2016年11月23日 下午11:41:30
 * @email 1181819395@qq.com
 */
public class Snake implements Serializable,Cloneable{
	private static final long serialVersionUID = 1211631935294087884L;
	private List<Point> list = new LinkedList<>();//第一个元素为蛇尾
	
	/**
	 * 蛇吃苹果
	 * @param apple
	 */
	public void eatApple(Point apple){
		list.get(list.size()-1).setType(Type.Body);
		list.add(new Point(apple.getRow(), apple.getCol(),Type.Head));
	}
	
	public void move(Point point){
		list.get(list.size()-1).setType(Type.Body);
		list.add(new Point(point.getRow(), point.getCol(),Type.Head));
		list.remove(0);
		list.get(0).setType(Type.Tail);
	}

	public Point getHead() {
		return list.get(list.size()-1);
	}

	public Point getTail() {
		return list.get(0);
	}

	public void add(Point p) {
		list.add(p);
	}
	
	public List<Point> getList(){
		return list;
	}
	
	@Override
	protected Snake clone()   {
		Snake s = new Snake();
		list.forEach(p->{
			s.list.add(p.clone());
		});
		return s;
	}
	
}
