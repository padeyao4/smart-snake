package io.github.guojiank.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static io.github.guojiank.game.model.Model.Cell.*;

public class Model {
    public static final int COLS = 20;
    public static final int ROWS = 20;

    public enum Cell {
        SNAKE, WALL, BLANK, APPLE
    }

    public static class Coord {
        int row;
        int col;

        public Coord(int row, int col) {
            this.row = row;
            this.col = col;
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
    }

    private ArrayList<Coord> snakes; // 蛇的坐标，第一个元素为蛇尾
    private ArrayList<Coord> walls; // 墙的坐标
    private Coord apple; // 苹果坐标

    private boolean running = false;

    public Model() {
        snakes = new ArrayList<>(COLS * ROWS + 1);
        walls = new ArrayList<>(2 * COLS + 2 * ROWS);
        init();
    }

    private void init() {
        snakes.clear();
        snakes.add(new Coord(ROWS / 2 - 1, COLS / 2));
        snakes.add(new Coord(ROWS / 2, COLS / 2));
        snakes.add(new Coord(ROWS / 2, COLS / 2 + 1));

        for (int row = 0; row < ROWS; row++) {
            walls.add(new Coord(row, 0));
            walls.add(new Coord(row, COLS - 1));
        }
        for (int col = 0; col < COLS; col++) {
            walls.add(new Coord(0, col));
            walls.add(new Coord(ROWS - 1, col));
        }

        apple = getRandomApple();
    }

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
        }
        if (v == APPLE) {
            eatApple(nextStep);
        }
        if (v == BLANK) {
            move(nextStep);
        }

    }

    private void move(Coord nextStep) {
        snakes.add(nextStep);
        snakes.remove(0);
    }

    private void eatApple(Coord nextStep) {
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
        if (step != null && step != getSnakeNeck()) {
            nextStep = step;
            step = null;
        }

        return nextStep;
    }
}
