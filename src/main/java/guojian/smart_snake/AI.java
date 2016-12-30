package guojian.smart_snake;

public interface AI {
	/**
	 * 简单智能
	 * 
	 * @return 返回下一步的坐标
	 */
	int run(Maze m);
	
	
	/**
	 * 关闭ai时，清理保存的记录
	 */
	void clear();
}
