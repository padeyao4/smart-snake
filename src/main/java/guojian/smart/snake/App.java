package guojian.smart.snake;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class App extends Application{
    int count =10;

    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View();
        Model model = new Model();
        Controller controller = new Controller(model);

        new AnimationTimer() {

            // now显示的是当前系统时间
            @Override
            public void handle(long now) {
                if(count-- > 0){
                    System.out.println(now);
                }
                model.update();
                view.update();
            }
        }.start();

        view.getScene().addEventHandler(KeyEvent.KEY_PRESSED,controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
