package guojian.smart.snake;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;


public class View extends Stage {
    private Canvas canvas = new Canvas();

    public View() throws IOException {
        Util u = Util.getInstance();

        Group root = new Group();
        root.getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());//绑定canvas的长宽，保持与stage的长宽一致
        canvas.heightProperty().bind(heightProperty());
        setScene(new Scene(root));
        setTitle(new RamdonTitle().getTitle());
        setHeight(Double.parseDouble(u.get("window.height", "300d")));
        setWidth(Double.parseDouble(u.get("widonw.width", "400d")));
        getIcons().add(new Image(this.getClass().getResourceAsStream("/me.jpg")));
        setResizable(false);
        show();//显示出来啦
    }

    public void update() {
    }
}
