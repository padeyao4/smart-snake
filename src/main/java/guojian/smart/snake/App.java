package guojian.smart.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class App extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model);

        // 默认每秒刷新60次
        new AnimationTimer() {

            // now显示的是当前系统时间
            @Override
            public void handle(long now) {
                model.update();
                view.render();
            }
        }.start();

        view.getScene().addEventHandler(KeyEvent.KEY_PRESSED,controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
