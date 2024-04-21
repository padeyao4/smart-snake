package guojian.core;

import java.util.List;
import java.util.Optional;

public interface Brain {
    /**
     * 寻找最好的路径，包含头和尾
     */
    Optional<List<Point>> search(Snake snake);
}
