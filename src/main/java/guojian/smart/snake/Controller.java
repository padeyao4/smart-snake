/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/19 8:32 AM
 * PS: Not easy to write code, please indicate.
 */
package guojian.smart.snake;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class Controller implements EventHandler{
    Model m;

    public Controller(Model model) {
        m = model;
    }

    @Override
    public void handle(Event event) {
        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            System.out.println("key pressed!");
        }
    }
}
