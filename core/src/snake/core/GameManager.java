package snake.core;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static snake.core.Cell.*;
import static snake.core.Config.*;

@Getter
public class GameManager {
    List<Point> snakes = new ArrayList<>(COLS * ROWS + 1); // 蛇的坐标，第一个元素为蛇尾
    List<Point> walls = new ArrayList<>(2 * COLS + 2 * ROWS); // 墙的坐标
    Point apple; // 苹果坐标
    boolean running = false;

    public void startOrStop() {
        running = !running;
    }

    public void start() {
        running = true;
    }

    public void init() {
        snakes.clear();
        snakes.add(new Point(ROWS / 2 + 1, COLS / 2));
        snakes.add(new Point(ROWS / 2, COLS / 2));
        snakes.add(new Point(ROWS / 2 - 1, COLS / 2));

        for (int row = 0; row < ROWS; row++) {
            walls.add(new Point(row, 0));
            walls.add(new Point(row, COLS - 1));
        }
        for (int col = 0; col < COLS; col++) {
            walls.add(new Point(0, col));
            walls.add(new Point(ROWS - 1, col));
        }

        apple = getRandomApple();
        bestPath = null;
    }

    /***
     * 返回蛇和墙组成的二维数组。
     */
    public Cell[][] getWorld() {
        Cell[][] world = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                world[row][col] = BLANK;
            }
        }
        for (Point p : snakes) {
            world[p.row][p.col] = SNAKE;
        }
        for (Point p : walls) {
            world[p.row][p.col] = WALL;
        }

        return world;
    }

    /**
     * 在地图上随机生成苹果
     *
     * @return 随机生成的苹果坐标
     */
    private Point getRandomApple() {
        Cell[][] world = getWorld();
        ArrayList<Point> blanks = new ArrayList<>(ROWS * COLS + 1);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (world[row][col] == BLANK) {
                    blanks.add(new Point(row, col));
                }
            }
        }
        int nextIndex = new Random().nextInt(blanks.size());
        return blanks.get(nextIndex);
    }

    public void update() {
        if (!running) return;

        Point nextStep = getNextStep();
        Cell[][] world = getWorld();
        Cell v = world[nextStep.row][nextStep.col];

        if (v == SNAKE || v == WALL) {

            running = false;

        } else if (nextStep.equals(apple)) {

            eatApple(nextStep);
            apple = getRandomApple();

        } else if (v == BLANK) {

            move(nextStep);
        }

    }

    public void move(Point nextStep) {
        snakes.add(nextStep);
        snakes.remove(0);
    }

    public void eatApple(Point nextStep) {
        snakes.add(nextStep);
    }

    public Point getSnakeHead() {
        return snakes.get(snakes.size() - 1);
    }

    public Point getSnakeNeck() {
        return snakes.get(snakes.size() - 2);
    }

    public Point getSnakeTail() {
        return snakes.get(0);
    }

    private List<Point> bestPath; //算法寻找的最好路径,包含目标头和尾

    private Point step; // 手工输入的下一步

    public List<Point> getBestPath() {
        return bestPath;
    }

    public void setBestPath(List<Point> bestPath) {
        this.bestPath = bestPath;
    }

    public void moveLeft() {
        Point head = getSnakeHead();
        step = new Point(head.row, head.col - 1);
    }

    public void moveRight() {
        Point head = getSnakeHead();
        step = new Point(head.row, head.col + 1);
    }

    public void moveUp() {
        Point head = getSnakeHead();
        step = new Point(head.row - 1, head.col);
    }

    public void moveDown() {
        Point head = getSnakeHead();
        step = new Point(head.row + 1, head.col);
    }

    /**
     * 什么都不操作时，获取默认下一步
     */
    private Point getDefaultStep() {
        Point head = getSnakeHead();
        Point neck = getSnakeNeck();
        return new Point(2 * head.row - neck.row, 2 * head.col - neck.col);
    }

    /**
     * 获取下一步
     *
     * @return 返回下一步坐标
     */
    private Point getNextStep() {
        Point nextStep = getDefaultStep();
        if (bestPath != null) {
            nextStep = bestPath.get(1);
            bestPath = null;
        }
        if (step != null && !step.equals(getSnakeNeck())) {
            nextStep = step;
            step = null;
        }

        return nextStep;
    }

    protected GameManager cp() {
        var o = new GameManager();
        o.snakes = snakes.stream().map(Point::cp).collect(Collectors.toList());
        o.walls = walls.stream().map(Point::cp).collect(Collectors.toList());
        o.apple = apple.cp();
        o.bestPath = null;
        return o;
    }
}
