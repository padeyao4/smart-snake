package snake.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import snake.SmartSnake;
import snake.core.Cell;
import snake.core.Config;
import snake.core.GameManager;
import snake.core.Point;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.*;
import static snake.core.Algorithm.*;

public class GameScreen extends ScreenAdapter {
    int offsetY = Gdx.graphics.getHeight() / Config.ROWS;
    int offsetX = Gdx.graphics.getWidth() / Config.COLS;
    Pixmap pixmap;
    Texture texture;
    Random r;
    GameManager gameManager;
    int status = 1; // 指定显示画面的样子
    int algo = 4; // 指定搜索算法
    Music bg;
    boolean music = true;

    SmartSnake smartSnake;
    SpriteBatch batch;

    public GameScreen(SmartSnake smartSnake) {
        this.smartSnake = smartSnake;
        batch = smartSnake.getBatch();
    }

    @Override
    public void show() {
        bg = Gdx.audio.newMusic(Gdx.files.internal("WhereIstheLove.mp3"));
        bg.setLooping(true);
        bg.play();
        r = new Random();
        pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);
        pixmap.setColor(Color.GREEN);
        texture = new Texture(pixmap);
        gameManager = new GameManager();
        gameManager.init();
        gameManager.start();
    }


    /**
     * 键盘监听，设置快捷方式
     */
    void handInput() {
        if (Gdx.input.isKeyJustPressed(ESCAPE)) {
            Gdx.app.exit();
            System.exit(0);
        } else if (Gdx.input.isKeyJustPressed(UP))
            gameManager.moveUp();
        else if (Gdx.input.isKeyJustPressed(DOWN))
            gameManager.moveDown();
        else if (Gdx.input.isKeyJustPressed(LEFT))
            gameManager.moveLeft();
        else if (Gdx.input.isKeyJustPressed(RIGHT))
            gameManager.moveRight();
        else if (Gdx.input.isKeyJustPressed(ENTER)) {
            gameManager.startOrStop();
        } else if (Gdx.input.isKeyJustPressed(F1))
            gameManager.init();
        else if (Gdx.input.isKeyJustPressed(F2)) {
            music = !music;
            if (music) {
                bg.play();
            } else {
                bg.pause();
            }
        } else if (Gdx.input.isKeyJustPressed(NUM_1)) {
            status = 1;
            Gdx.graphics.setTitle("SmartSnake");
        } else if (Gdx.input.isKeyJustPressed(NUM_2)) {
            status = 2;
            Gdx.graphics.setTitle("debug-1");
        } else if (Gdx.input.isKeyJustPressed(NUM_3)) {
            status = 3;
            Gdx.graphics.setTitle("debug-2");
        } else if (Gdx.input.isKeyJustPressed(Q)) {
            algo = 1;
            Gdx.graphics.setTitle("SmartSnake-最短路径搜索");
        } else if (Gdx.input.isKeyJustPressed(W)) {
            algo = 2;
            Gdx.graphics.setTitle("SmartSnake-最长路径搜索");
        } else if (Gdx.input.isKeyJustPressed(E)) {
            algo = 3;
            Gdx.graphics.setTitle("SmartSnake-串联路径");
        } else if (Gdx.input.isKeyJustPressed(R)) {
            algo = 4;
            Gdx.graphics.setTitle("SmartSnake-自动选择路径");
        }
    }


    @Override
    public void render(float delta) {
        switch (status) {
            case 1 -> draw();
            case 2 -> debugDraw1();
            case 3 -> debugDraw2();
        }

        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
        update(Gdx.graphics.getDeltaTime());
        handInput();
    }

    float tmpTime = 0;

    float dtTime = 0; //间隔时间

    /**
     * 游戏逻辑更新
     */
    void update(float deltaTime) {
        tmpTime += deltaTime;
        if (tmpTime > dtTime) {
            tmpTime -= dtTime;
            gameManager.update();

            List<Point> path = switch (algo) {
                case 1 -> findShortestPath(gameManager.getSnakeHead(), gameManager.getApple(), gameManager.getWorld(), null);
                case 2 -> findFarthestPath(gameManager.getSnakeHead(), gameManager.getApple(), gameManager.getWorld());
                case 3 -> findSeriesPath(gameManager.getSnakeHead(), gameManager.getApple(), gameManager.getSnakeTail(), gameManager);
                case 4 -> findBestPath(gameManager.getSnakeHead(), gameManager.getApple(), gameManager.getSnakeTail(), gameManager);
                default -> null;
            };

            if (path != null) {
                gameManager.setBestPath(path);
            }
        }
    }

    private void debugDraw2() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        GameManager m = getShadowModelByPath(gameManager, (LinkedList<Point>) gameManager.getBestPath());
        drawWord(m);
        drawApple(m);
        drawSnake(m);
        drawPath(m);
        texture.draw(pixmap, 0, 0);
    }


    private void debugDraw1() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        GameManager m = gameManager;
        drawWord(m);
        drawApple(m);
        drawSnake(m);
        drawPath(m);
        texture.draw(pixmap, 0, 0);
    }

    private void drawPath(GameManager gameManager) {
        List<Point> path = gameManager.getBestPath();
        if (path == null) return;
        pixmap.setColor(Color.YELLOW);
        draw(path);
    }

    private void draw(List<Point> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Point a = path.get(i);
            Point b = path.get(i + 1);
            pixmap.drawLine(a.getX() * offsetX + offsetX / 2, a.getY() * offsetY + offsetY / 2, b.getX() * offsetX + offsetX / 2, b.getY() * offsetY + offsetY / 2);
        }
    }

    private void drawWord(GameManager gameManager) {
        Cell[][] world = gameManager.getWorld();
        for (int row = 0; row < Config.ROWS; row++) {
            for (int col = 0; col < Config.COLS; col++) {
                Cell type = world[row][col];
                if (type == Cell.APPLE)
                    pixmap.setColor(Color.RED);
                if (type == Cell.SNAKE)
                    pixmap.setColor(Color.GRAY);
                if (type == Cell.WALL)
                    pixmap.setColor(Color.WHITE);
                if (type == Cell.BLANK)
                    pixmap.setColor(Color.BLACK);

                pixmap.drawRectangle(col * offsetX, row * offsetY, offsetX, offsetY);
            }
        }
    }

    private void draw() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        drawSnake(gameManager);
        drawWall(gameManager);
        drawApple(gameManager);
        texture.draw(pixmap, 0, 0);
    }

    private void drawApple(GameManager gameManager) {
        Point a = gameManager.getApple();
        pixmap.setColor(Color.GREEN);
        pixmap.fillRectangle(a.getX() * offsetX, a.getY() * offsetY, offsetX, offsetY);
    }

    private void drawWall(GameManager gameManager) {
        List<Point> walls = gameManager.getWalls();
        pixmap.setColor(Color.ORANGE);
        for (Point a : walls) {
            pixmap.fillRectangle(a.getX() * offsetX, a.getY() * offsetY, offsetX, offsetY);
        }
    }

    private void drawSnake(GameManager gameManager) {
        List<Point> snake = gameManager.getSnakes();
        pixmap.setColor(Color.WHITE);
        draw(snake);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        texture.dispose();
        pixmap.dispose();
    }
}
