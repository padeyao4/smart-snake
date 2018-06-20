/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/19 8:32 AM
 * PS: Not easy to write code, please indicate.
 */
package guojian.smart.snake;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * 处理键盘事件
 */
public class Controller implements EventHandler {
    Model m;

    public Controller(Model model) {
        m = model;
    }

    @Override
    public void handle(Event event) {
        if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
            KeyCode keyCode = ((KeyEvent) event).getCode();
            switch (keyCode) {
                case ENTER:
                    m.changeState();
                    break;
                case UP:
                    m.moveUp();
                    break;
                case DOWN:
                    m.moveDown();
                    break;
                case LEFT:
                    m.moveLeft();
                    break;
                case RIGHT:
                    m.moveRight();
                    break;
                default:
                    System.out.println(keyCode);
            }

        }
    }
}
