package guojian.smart.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View();
        Model model = new Model();
        new AnimationTimer() {

            // now显示的是当前系统时间
            @Override
            public void handle(long now) {
                model.update();
                view.update();
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
