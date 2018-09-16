package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import io.github.guojiank.game.core.Model;
import io.github.guojiank.game.core.Model.Coord;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.*;
import static io.github.guojiank.game.core.Algorithm.*;

public class GameState extends State {
    int offset_y = Gdx.graphics.getHeight() / Model.ROWS;
    int offset_x = Gdx.graphics.getWidth() / Model.COLS;
    Pixmap pixmap;
    Texture texture;
    Random r;
    Model model;
    int status = 1; // 指定显示画面的样子
    int algo = 1; // 指定搜索算法
    Music bg;
    boolean music = true;

    public GameState(StateManager stateManager) {
        super(stateManager);
        bg = Gdx.audio.newMusic(Gdx.files.internal("WhereIstheLove.mp3"));
        bg.setLooping(true);
        bg.play();
        r = new Random();
        pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);
        pixmap.setColor(Color.GREEN);
        texture = new Texture(pixmap);
        model = new Model();
        model.init();
        model.start();
    }


    /**
     * 键盘监听，设置快捷方式
     */
    void handInput() {
        if (Gdx.input.isKeyJustPressed(UP))
            model.moveUp();
        else if (Gdx.input.isKeyJustPressed(DOWN))
            model.moveDown();
        else if (Gdx.input.isKeyJustPressed(LEFT))
            model.moveLeft();
        else if (Gdx.input.isKeyJustPressed(RIGHT))
            model.moveRight();
        else if (Gdx.input.isKeyJustPressed(ENTER)) {
            model.startOrStop();
        } else if (Gdx.input.isKeyJustPressed(F1))
            model.init();
        else if (Gdx.input.isKeyJustPressed(F2)) {
            if (music = !music) {
                bg.play();
            } else {
                bg.pause();
            }
        } else if (Gdx.input.isKeyJustPressed(NUM_1)) {
            status = 1;
            Gdx.graphics.setTitle("SmartSnake");
        } else if (Gdx.input.isKeyJustPressed(NUM_2)) {
            status = 2;
            Gdx.graphics.setTitle("debug-最好路径");
        } else if (Gdx.input.isKeyJustPressed(NUM_3)) {
            status = 3;
            Gdx.graphics.setTitle("debug-平行世界");
        }

        if (Gdx.input.isKeyJustPressed(Q)) {
            algo = 1;
            Gdx.graphics.setTitle("SmartSnake-最短路径搜索");
        } else if (Gdx.input.isKeyJustPressed(W)) {
            algo = 2;
            Gdx.graphics.setTitle("SmartSnake-最长路径搜索");
        } else if (Gdx.input.isKeyJustPressed(E)) {
            algo = 3;
            Gdx.graphics.setTitle("SmartSnake-串联路径");
        }


    }

    @Override
    void render(Batch batch) {

        switch (status) {
            case 1:
                draw();
                break;
            case 2:
                debugDraw1();
                break;
            case 3:
                debugDraw2();
                break;
        }

        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
        update(Gdx.graphics.getDeltaTime());
        handInput();
    }


    float tmpTime = 0;

    /**
     * 游戏逻辑更新
     *
     * @param deltaTime
     */
    void update(float deltaTime) {
        tmpTime += deltaTime;
        if (tmpTime > 1) {
            tmpTime--;
            model.update();

            List<Coord> path = null;
            switch (algo) {
                case 1: // 最短路径
                    path = findShortestPath(model.getSnakeHead(), model.getApple(), model.getWorld(), new HashSet<Coord>());
                    break;
                case 2:
                    path = findFarthestPath(model.getSnakeHead(), model.getApple(), model.getWorld());
                    break;
                case 3:
                    path = findSeriesPath(model.getSnakeHead(), model.getApple(), model.getSnakeTail(), model);
            }

            if (path != null) {
                model.setBestPath(path);
            }
        }
    }

    private void debugDraw2() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Model m = getShadowModelByPath(model, (LinkedList<Coord>) model.getBestPath());
        drawWord(m);
        drawApple(m);
        drawSnake(m);
        drawPath(m);
        texture.draw(pixmap, 0, 0);
    }


    private void debugDraw1() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Model m = model;
        drawWord(m);
        drawApple(m);
        drawSnake(m);
        drawPath(m);
        texture.draw(pixmap, 0, 0);
    }

    private void drawPath(Model model) {
        List<Coord> path = model.getBestPath();
        if (path == null) return;
        pixmap.setColor(Color.GREEN);
        for (int i = 0; i < path.size() - 1; i++) {
            Coord a = path.get(i);
            Coord b = path.get(i + 1);
            pixmap.drawLine(a.getX() * offset_x + offset_x / 2, a.getY() * offset_y + offset_y / 2, b.getX() * offset_x + offset_x / 2, b.getY() * offset_y + offset_y / 2);
        }
    }

    private void drawWord(Model model) {
        Model.Cell[][] world = model.getWorld();
        for (int row = 0; row < Model.ROWS; row++) {
            for (int col = 0; col < Model.COLS; col++) {
                Model.Cell type = world[row][col];
                if (type == Model.Cell.APPLE)
                    pixmap.setColor(Color.RED);
                if (type == Model.Cell.SNAKE)
                    pixmap.setColor(Color.GRAY);
                if (type == Model.Cell.WALL)
                    pixmap.setColor(Color.WHITE);
                if (type == Model.Cell.BLANK)
                    pixmap.setColor(Color.BLACK);

                pixmap.drawRectangle(col * offset_x, row * offset_y, offset_x, offset_y);
            }
        }
    }

    private void draw() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        drawSnake(model);
        drawWall(model);
        drawApple(model);
        texture.draw(pixmap, 0, 0);
    }

    private void drawApple(Model model) {
        Coord a = model.getApple();
        pixmap.setColor(Color.RED);
        pixmap.drawRectangle(a.getX() * offset_x, a.getY() * offset_y, offset_x, offset_y);
    }

    private void drawWall(Model model) {
        List<Coord> walls = model.getWalls();
        pixmap.setColor(Color.WHITE);
        for (int i = 0; i < walls.size(); i++) {
            Coord a = walls.get(i);
            pixmap.drawRectangle(a.getX() * offset_x, a.getY() * offset_y, offset_x, offset_y);
        }
    }

    private void drawSnake(Model model) {
        List<Coord> snake = model.getSnake();
        pixmap.setColor(Color.BLUE);
        for (int i = 0; i < snake.size() - 1; i++) {
            Coord a = snake.get(i);
            Coord b = snake.get(i + 1);
            pixmap.drawLine(a.getX() * offset_x + offset_x / 2, a.getY() * offset_y + offset_y / 2, b.getX() * offset_x + offset_x / 2, b.getY() * offset_y + offset_y / 2);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        pixmap.dispose();
    }
}
