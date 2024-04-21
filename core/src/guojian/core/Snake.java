package guojian.core;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static guojian.SmartSnake.GRID_HEIGHT;
import static guojian.SmartSnake.GRID_WIDTH;
import static guojian.core.CellType.*;

/**
 * todo
 * 重构
 * - 不要返回world,直接返回walls snake等，通过这些渲染画面
 */
@Getter
public class Snake {
    public List<Point> positions; // 蛇的坐标，第一个元素为蛇尾
    public List<Point> walls; // 墙的坐标
    public Point food;
    public boolean running;
    @Setter
    private List<Point> bestPath; //算法寻找的最好路径,包含目标头和尾
    private Point step; // 手工输入的下一步

    public void startOrStop() {
        running = !running;
    }

    public void start() {
        running = true;
    }

    public Snake() {
        running = false;
        positions = new ArrayList<>(GRID_WIDTH * GRID_HEIGHT + 1); // 蛇的坐标，第一个元素为蛇尾
        walls = new ArrayList<>(2 * GRID_WIDTH + 2 * GRID_HEIGHT); // 墙的坐标
        for (int row = 0; row < GRID_HEIGHT; row++) {
            walls.add(new Point(row, 0));
            walls.add(new Point(row, GRID_WIDTH - 1));
        }
        for (int col = 0; col < GRID_WIDTH; col++) {
            walls.add(new Point(0, col));
            walls.add(new Point(GRID_HEIGHT - 1, col));
        }
    }

    public void init() {
        positions.clear();
        positions.add(new Point(GRID_HEIGHT / 2 + 1, GRID_WIDTH / 2));
        positions.add(new Point(GRID_HEIGHT / 2, GRID_WIDTH / 2));
        positions.add(new Point(GRID_HEIGHT / 2 - 1, GRID_WIDTH / 2));

        food = randomFood();
        bestPath = null;
    }

    /***
     * 返回蛇和墙组成的二维数组。
     */
    public CellType[][] getCellTypes() {
        CellType[][] world = new CellType[GRID_HEIGHT][GRID_WIDTH];
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                world[row][col] = BLANK;
            }
        }
        for (Point p : positions) {
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
    private Point randomFood() {
        CellType[][] cellTypes = getCellTypes();
        ArrayList<Point> blanks = new ArrayList<>(GRID_HEIGHT * GRID_WIDTH + 1);
        for (int row = 0; row < GRID_HEIGHT; row++) {
            for (int col = 0; col < GRID_WIDTH; col++) {
                if (cellTypes[row][col] == BLANK) {
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
        CellType[][] world = getCellTypes();
        CellType cellType = world[nextStep.row][nextStep.col];

        if (cellType == SNAKE || cellType == WALL) {
            running = false;
        } else if (nextStep.equals(food)) {
            eatFood(nextStep);
            food = randomFood();
        } else if (cellType == BLANK) {
            move(nextStep);
        }
    }

    public void move(Point nextStep) {
        positions.add(nextStep);
        positions.removeFirst();
    }

    public void eatFood(Point nextStep) {
        positions.add(nextStep);
    }

    public Point getSnakeNeck() {
        return positions.get(positions.size() - 2);
    }

    public Point getSnakeTail() {
        return positions.getFirst();
    }

    public void moveLeft() {

        Point head = positions.getLast();
        step = new Point(head.row, head.col - 1);
    }

    public void moveRight() {
        Point head = positions.getLast();
        step = new Point(head.row, head.col + 1);
    }

    public void moveUp() {

        Point head = positions.getLast();
        step = new Point(head.row - 1, head.col);
    }

    public void moveDown() {

        Point head = positions.getLast();
        step = new Point(head.row + 1, head.col);
    }

    /**
     * 什么都不操作时，获取默认下一步
     */
    private Point getDefaultStep() {
        Point head = positions.getLast();
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

    protected Snake cp() {
        var o = new Snake();
        o.positions = positions.stream().map(Point::cp).collect(Collectors.toList());
        o.walls = walls.stream().map(Point::cp).collect(Collectors.toList());
        o.food = food.cp();
        o.bestPath = null;
        return o;
    }
}
