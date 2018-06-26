/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/19 8:32 AM
 * PS: Not easy to write code, please indicate.
 */
package guojian.smart.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class App extends Application {
    private long tmptime = 0;

    @Override
    public void start(Stage primaryStage) {
        // 数据模型
        Model model = new Model();
        // 用来界面显示
        View view = new View(model);
        // 用来处理鼠标键盘事件
        Controller controller = new Controller(model);

        // 默认每秒刷新60次
        new AnimationTimer() {

            // now显示的是当前系统时间
            @Override
            public void handle(long now) {
                //控制主主程序运行时间
                if (now - tmptime < 1000_000) {
                    return;
                } else {
                    tmptime = now;
                    model.update();
                    view.render();
                }

            }
        }.start();

        // 给场景添加按键监听
        view.getScene().addEventHandler(KeyEvent.KEY_PRESSED, controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
