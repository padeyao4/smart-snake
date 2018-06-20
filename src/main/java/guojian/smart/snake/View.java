/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/19 8:39 AM
 * PS: Not easy to write code, please indicate.
 */
package guojian.smart.snake;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


/**
 * 视图，负责画面显示
 */
public class View extends Stage {
    /**
     * 画布
     */
    Canvas canvas;
    /**
     * 画笔
     */
    GraphicsContext pen;

    /**
     * 窗口高
     */
    double height = Conf.WINDOW_HEIGHT;
    /**
     * 窗口宽
     */
    double width = Conf.WINDOW_WIDTH;
    Model m;

    /**
     * 方块和颜色对应
     */
    Map<Integer, Color> colorMap;
    /**
     * 方格大小
     * 设置为正方形窗口边 除以 正方形地图边的值，地图刚好填充满窗口
     */
    int cellSize = 20;

    public View(Model model) {
        m = model;
        canvas = new Canvas();
        pen = canvas.getGraphicsContext2D();

        initMap();

        Group root = new Group();
        root.getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());//绑定canvas的长宽，保持与stage的长宽一致
        canvas.heightProperty().bind(heightProperty());
        setScene(new Scene(root));
        setTitle(new RamdonTitle().getTitle());//标题
        setHeight(height);
        setWidth(width);
        getIcons().add(new Image(this.getClass().getResourceAsStream("/me.jpg")));
        setResizable(false);
        show();//显示
    }

    private void initMap() {
        colorMap = new HashMap<>();
        colorMap.put(Model.BODY, Color.BURLYWOOD);
        colorMap.put(Model.APPLE, Color.GREEN);
        colorMap.put(Model.BLANK, Color.BLACK);
        colorMap.put(Model.WALL, Color.DARKCYAN);
    }

    /**
     * 使用白色 清理画布
     */
    protected void clearCanvas() {
        pen.setFill(Color.WHITE);
        pen.fillRect(0, 0, width, height);
    }


    protected void draw() {
        for (int row = 0; row < m.ROWS; row++) {
            for (int col = 0; col < m.COLS; col++) {
                pen.setFill(colorMap.get(m.world[row][col]));
                pen.fillRect(col * cellSize, row * cellSize, cellSize - 1, cellSize - 1);
            }
        }
    }


    /**
     * 渲染画面到屏幕
     */
    public void render() {
        clearCanvas();
        draw();
    }
}
