package guojian.smart_snake;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Snake implements Serializable,Cloneable{
	private static final long serialVersionUID = 1211631935294087884L;
	private List<Point> list = new ArrayList<>();//第一个元素为蛇尾
	
	/**
	 * 蛇吃苹果
	 * @param apple
	 */
	public void eatApple(Point apple){
		list.get(list.size()-1).changeType(Type.Body);
		list.add(new Point(apple.row, apple.col,Type.Head));
	}
	
	public void move(Point point){
		list.get(list.size()-1).changeType(Type.Body);
		list.add(new Point(point.row, point.col,Type.Head));
		list.remove(0);
		list.get(0).changeType(Type.Tail);
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
	protected Object clone() throws CloneNotSupportedException {
		Snake s = new Snake();
		for(int i=0;i<list.size();i++){
			s.getList().add(new Point(list.get(i).row,list.get(i).col,list.get(i).type));
		}
		return s;
	}
	
}
