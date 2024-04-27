package guojian.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Point implements Cloneable {

    public static final Point UP = new Point(1, 0);
    public static final Point DOWN = new Point(-1, 0);
    public static final Point LEFT = new Point(0, -1);
    public static final Point RIGHT = new Point(0, 1);

    int row;
    int col;

    public static Point direction(Point p1, Point p2) {
        return new Point(p1.row - p2.row, p1.col - p2.col);
    }

    public static int scoreDirections(List<Point> points) {
        AtomicInteger score = new AtomicInteger();
        var reduce = points.stream().reduce((acc, current) -> {
            if (acc.equals(current)) {
                score.getAndIncrement();
            }
            return current;
        });
        return score.get();
    }

    public static List<Point> directions(List<Point> points) {
        List<Point> result = new ArrayList<>();
        var reduce = points.stream().reduce((acc, point) -> {
            result.add(direction(acc, point));
            return point;
        });
        return result;
    }

    public int getX() {
        return getCol();
    }

    public int getY() {
        return getRow();
    }

    public int x() {
        return getX();
    }

    public int y() {
        return getY();
    }

    public Point cp() {
        return new Point(row, col);
    }

    @Override
    public Point clone() {
        try {
            return (Point) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
