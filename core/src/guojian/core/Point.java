package guojian.core;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Point implements Cloneable{
    int row;
    int col;

    public int getX() {
        return getCol();
    }

    public int getY() {
        return getRow();
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
