package io.github.guojiank.game.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static io.github.guojiank.game.core.Model.Cell.*;

public class Model implements Cloneable {
    public static final int COLS = 20;
    public static final int ROWS = 20;

    public void startOrStop() {
        running = !running;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }

    public enum Cell {
        SNAKE, WALL, BLANK, APPLE
    }

    public static class Coord implements Cloneable {
        int row;
        int col;

        public Coord(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getX() {
            return getCol();
        }

        public int getY() {
            return getRow();
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return row == coord.row &&
                    col == coord.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", row, col);
        }
    }

    private ArrayList<Coord> snakes; // 蛇的坐标，第一个元素为蛇尾
    private ArrayList<Coord> walls; // 墙的坐标
    private Coord apple; // 苹果坐标

    private boolean running = false;

    public boolean isRunning() {
        return running;
    }

    public List<Coord> getSnake() {
        return snakes;
    }

    public ArrayList<Coord> getWalls() {
        return walls;
    }

    public Coord getApple() {
        return apple;
    }

    public Model() {
        snakes = new ArrayList<>(COLS * ROWS + 1);
        walls = new ArrayList<>(2 * COLS + 2 * ROWS);
    }

    public void init() {
        snakes.clear();
        snakes.add(new Coord(ROWS / 2 + 1, COLS / 2));
        snakes.add(new Coord(ROWS / 2, COLS / 2));
        snakes.add(new Coord(ROWS / 2 - 1, COLS / 2));

        for (int row = 0; row < ROWS; row++) {
            walls.add(new Coord(row, 0));
            walls.add(new Coord(row, COLS - 1));
        }
        for (int col = 0; col < COLS; col++) {
            walls.add(new Coord(0, col));
            walls.add(new Coord(ROWS - 1, col));
        }

        apple = getRandomApple();
        bestPath = null;
    }

    /***
     * 返回蛇和墙组成的二维数组。
     * @return
     */
    public Cell[][] getWorld() {
        Cell[][] world = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                world[row][col] = BLANK;
            }
        }
        for (Coord coord : snakes) {
            world[coord.row][coord.col] = SNAKE;
        }
        for (Coord coord : walls) {
            world[coord.row][coord.col] = WALL;
        }

        return world;
    }

    /**
     * 在地图上随机生成苹果
     *
     * @return 随机生成的苹果坐标
     */
    private Coord getRandomApple() {
        Cell[][] world = getWorld();
        ArrayList<Coord> blanks = new ArrayList<>(ROWS * COLS + 1);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (world[row][col] == BLANK) {
                    blanks.add(new Coord(row, col));
                }
            }
        }
        int nextIndex = new Random().nextInt(blanks.size());
        return blanks.get(nextIndex);
    }

    public void update() {
        if (!running) return;

        Coord nextStep = getNextStep();
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

    public void move(Coord nextStep) {
        snakes.add(nextStep);
        snakes.remove(0);
    }

    public void eatApple(Coord nextStep) {
        snakes.add(nextStep);
    }

    public Coord getSnakeHead() {
        return snakes.get(snakes.size() - 1);
    }

    public Coord getSnakeNeck() {
        return snakes.get(snakes.size() - 2);
    }

    private Coord getSnakeTail() {
        return snakes.get(0);
    }

    private List<Coord> bestPath; //算法寻找的最好路径,包含目标头和尾

    private Coord step; // 手工输入的下一步

    public List<Coord> getBestPath() {
        return bestPath;
    }

    public void setBestPath(List<Coord> bestPath) {
        this.bestPath = bestPath;
    }

    public void moveLeft() {
        Coord head = getSnakeHead();
        step = new Coord(head.row, head.col - 1);
    }

    public void moveRight() {
        Coord head = getSnakeHead();
        step = new Coord(head.row, head.col + 1);
    }

    public void moveUp() {
        Coord head = getSnakeHead();
        step = new Coord(head.row - 1, head.col);
    }

    public void moveDown() {
        Coord head = getSnakeHead();
        step = new Coord(head.row + 1, head.col);
    }

    /**
     * 什么都不操作时，获取默认下一步
     *
     * @return
     */
    private Coord getDefaultStep() {
        Coord head = getSnakeHead();
        Coord neck = getSnakeNeck();
        return new Coord(2 * head.row - neck.row, 2 * head.col - neck.col);
    }

    /**
     * 获取下一步
     *
     * @return 返回下一步坐标
     */
    private Coord getNextStep() {
        Coord nextStep = getDefaultStep();
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

    @Override
    protected Model clone() throws CloneNotSupportedException {
        Model o = (Model) super.clone();
        o.snakes = (ArrayList<Coord>) this.snakes.clone();
        o.walls = (ArrayList<Coord>) this.walls.clone();
        o.apple = (Coord) this.apple.clone();
        o.bestPath = null;
        return o;
    }
}
