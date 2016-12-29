package guojian.smart_snake_v2;

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
public class Maze extends CommFunc implements Cloneable{
	byte[][] palace;
	int apple;
	List<Integer> snake;
	
	@Override
	protected Maze clone() {
		Maze m = new Maze();
		m.palace = palace.clone();
		m.apple = apple;
		for(int i:snake){
			m.snake.add(i);
		}
		return m;
	}

	public int getHead(){
		return snake.get(snake.size()-1);
	}
	
	public int getTail(){
		return snake.get(0);
	}

	public Maze() {
		palace = new byte[Config.MAZE_ROWS][Config.MAZE_COLS];
		snake = new LinkedList<>();
		apple = -1;
	}

	public void initPalace() {
		for (int i = 0; i < Config.MAZE_ROWS; i++) {
			for (int j = 0; j < Config.MAZE_COLS; j++) {
				palace[i][j] = Define.BLANK;
			}
		}

		for (int i = 0; i < Config.MAZE_COLS; i++) {
			palace[0][i] = Define.WALL;
			palace[Config.MAZE_ROWS - 1][i] = Define.WALL;
		}

		for (int i = 0; i < Config.MAZE_ROWS; i++) {
			palace[i][0] = Define.WALL;
			palace[i][Config.MAZE_COLS - 1] = Define.WALL;
		}
	}

	public void initSnake() {
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
				if (palace[i][j] == Define.BLANK) {
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
			palace[coord[0]][coord[1]] = Define.SNAKE;
		}
	}

	public void pressApple()  {
		if(apple==-1){
			return;
		}
		int[] coord = indexToCoord(apple);
		palace[coord[0]][coord[1]] = Define.APPLE;
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
			palace[coord[0]][coord[1]]=Define.PATH;
		}
	}

	public void move(int nextIndex) {
		// TODO Auto-generated method stub
		
	}
}
