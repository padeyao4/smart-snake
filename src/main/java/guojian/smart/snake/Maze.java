package guojian.smart.snake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author guojian
 * @date 2016年12月29日 上午12:46:59
 * @email 1181819395@qq.com
 */
public class Maze implements Cloneable{
	byte[][] palace;//
	int apple;//随机生成的苹果的位置
	List<Integer> snake;

    /**
     * 将坐标转换为索引
     *
     * @param col
     * @param row
     * @return
     */
    protected static int coordToIndex(int col, int row) {
        int index = col + row * Config.MAZE_COLS;
        return index;
    }

    /**
     * @param index
     * @return int[2] row col
     */
    protected static int[] indexToCoord(int index) {
        int row = index / Config.MAZE_COLS;
        int col = index % Config.MAZE_COLS;
        return new int[] { row, col };
    }

    @Override
	protected Maze clone() {
		Maze m = new Maze();
		for (int i = 0; i < Config.MAZE_ROWS; i++) {
			for (int j = 0; j < Config.MAZE_COLS; j++) {
				m.palace[i][j] = palace[i][j];
			}
		}
		m.apple = apple;
		for(int i:snake){
			m.snake.add(i);
		}
		return m;
	}
	
	public int getsPreStraightIndex(){
		int head = getHead();
		int body = snake.get(snake.size()-2);
		int pre = head-body;
		return head+pre;
	}

	public int getHead(){
		return snake.get(snake.size()-1);
	}
	
	public int getSecondBody(){
		return snake.get(snake.size()-2);
	}
	
	public int getTail(){
		return snake.get(0);
	}

	public Maze() {
        super();
        palace = new byte[Config.MAZE_ROWS][Config.MAZE_COLS];
		snake = new LinkedList<>();
		apple = -1;
	}

	public void initPalace() {
		for (int i = 0; i < Config.MAZE_ROWS; i++) {
			for (int j = 0; j < Config.MAZE_COLS; j++) {
				palace[i][j] = Block.BLANK;
			}
		}

		for (int i = 0; i < Config.MAZE_COLS; i++) {
			palace[0][i] = Block.WALL;
			palace[Config.MAZE_ROWS - 1][i] = Block.WALL;
		}

		for (int i = 0; i < Config.MAZE_ROWS; i++) {
			palace[i][0] = Block.WALL;
			palace[i][Config.MAZE_COLS - 1] = Block.WALL;
		}
	}

	public void initSnake() {
		snake.clear();
		int center_col = Config.MAZE_COLS / 2;
		int center_row = Config.MAZE_ROWS / 2;
		for (int i = 1; i < 4; i++) {
			snake.add(coordToIndex(center_col, center_row + i));
		}
		Collections.reverse(snake);
	}
	
	public int getApple(){
		return apple;
	}

	public void RandonApple() {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < Config.MAZE_ROWS; i++) {
			for (int j = 0; j < Config.MAZE_COLS; j++) {
				if (palace[i][j] == Block.BLANK) {
					list.add(coordToIndex(j, i));
				}
			}
		}
		if(list.isEmpty()){//吃满屏幕的情况
			apple =  -1;
		}else{
			apple =  list.get(new Random().nextInt(list.size()));
		}
	}

	public void pressSnakeAndPalace() {
		for (int i : snake) {
			int[] coord = indexToCoord(i);
			palace[coord[0]][coord[1]] = Block.SNAKE;
		}
	}

	public void pressApple()  {
		if(apple==-1){
			return;
		}
		int[] coord = indexToCoord(apple);
		palace[coord[0]][coord[1]] = Block.APPLE;
	}

	public void log() {
		for (int i = 0; i < Config.MAZE_ROWS; i++) {
			for (int j = 0; j < Config.MAZE_COLS; j++) {
				System.out.print(palace[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Maze maze = new Maze();
		maze.initPalace();
		maze.log();
		maze.initSnake();
		maze.pressSnakeAndPalace();
		maze.log();
		maze.RandonApple();
		maze.pressApple();
		maze.log();
	}

	public void pressPaths(List<Integer> paths) {
		for(int i:paths){
			int[] coord = indexToCoord(i);
			palace[coord[0]][coord[1]]= Block.PATH;
		}
	}

	/**
	 * 根据给的坐标向坐标移动一格
	 * 并更新地图，更新蛇，更新苹果
	 * @param nextIndex
	 */
	public void move(int nextIndex) {
		int[] coord = indexToCoord(nextIndex);
		if(nextIndex==apple){//是苹果
			snake.add(nextIndex);
			palace[coord[0]][coord[1]]= Block.SNAKE;
			RandonApple();
			pressApple();
		}else{
			if(palace[coord[0]][coord[1]]== Block.BLANK){//是空白
				snake.add(nextIndex);
				int tail = snake.remove(0);
				palace[coord[0]][coord[1]]= Block.SNAKE;
				int[] coord_tail = indexToCoord(tail);
				palace[coord_tail[0]][coord_tail[1]]= Block.BLANK;
			}else{
				apple=-1;
			}
		}
	}
}
