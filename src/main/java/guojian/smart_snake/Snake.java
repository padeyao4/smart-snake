package guojian.smart_snake;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Snake implements Serializable{
	private static final long serialVersionUID = 1211631935294087884L;
	private List<Point> bodys = new ArrayList<>();//下标越小的元素离蛇尾越近。bodys[0]的元素为蛇的倒数第2节（蛇尾为倒数第一节）
	private Head head;
	private Tail tail;
	
	/**
	 * 蛇吃苹果
	 * @param apple
	 */
	public void eatApple(Point apple){
		bodys.add(new Body(head.row, head.col));
		head=new Head(apple.row, apple.col);
	}

	public void addHead(Head head) {
		this.head=head;
	}
	
	public void move(Point point){
		bodys.add(new Body(head.row, head.col));
		head=new Head(point.row,point.col);
		Point p = bodys.get(0);
		tail=new Tail(p.row,p.col);
		bodys.remove(0);
	}

	public void addBody(Body body) {
		bodys.add(body);
	}

	public void addTail(Tail tail) {
		this.tail=tail;
	}

	public List<Point> getBodys() {
		return bodys;
	}

	public void setBodys(List<Point> bodys) {
		this.bodys = bodys;
	}

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public Tail getTail() {
		return tail;
	}

	public void setTail(Tail tail) {
		this.tail = tail;
	}
	
	
}
