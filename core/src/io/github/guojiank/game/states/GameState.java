package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import io.github.guojiank.game.model.*;
import io.github.guojiank.game.model.Model.Coord;

import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.*;

public class GameState extends State {
    int offset_y = Gdx.graphics.getHeight() / Model.ROWS;
    int offset_x = Gdx.graphics.getWidth() / Model.COLS;
    Pixmap pixmap;
    Texture texture;
    Random r;
    Model model;
    boolean debug = false;

    public GameState(StateManager stateManager) {
        super(stateManager);
        r = new Random();
        pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);
        pixmap.setColor(Color.GREEN);
        texture = new Texture(pixmap);
        model = new Model();
    }


    void handInput() {
        if (Gdx.input.isKeyJustPressed(UP))
            model.moveUp();
        else if (Gdx.input.isKeyJustPressed(DOWN))
            model.moveDown();
        else if (Gdx.input.isKeyJustPressed(LEFT))
            model.moveLeft();
        else if (Gdx.input.isKeyJustPressed(RIGHT))
            model.moveRight();
        else if (Gdx.input.isKeyJustPressed(ENTER))
            model.startOrStop();
        else if (Gdx.input.isKeyJustPressed(NUM_1))
            debug = !debug;
        else if (Gdx.input.isKeyJustPressed(F1))
            model.init();

    }

    @Override
    void render(Batch batch) {
        if (debug)
            debugDraw();
        else
            draw();
        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
        handInput();
        update(Gdx.graphics.getDeltaTime());
    }


    float tmpTime = 0;

    /**
     * 游戏逻辑更新
     * @param deltaTime
     */
    void update(float deltaTime) {
        tmpTime += deltaTime;
        if (tmpTime > 1) {
            model.update();
            tmpTime--;
        }
    }


    private void debugDraw() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        drawWord();
        draw_apple();
        draw_snake();
        texture.draw(pixmap, 0, 0);
    }

    private void drawWord() {
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
                if(type==Model.Cell.BLANK)
                    pixmap.setColor(Color.BLACK);

                pixmap.drawRectangle(col * offset_x, row * offset_y, offset_x, offset_y);
            }
        }
    }

    private void draw() {
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        draw_snake();
        draw_wall();
        draw_apple();
        texture.draw(pixmap, 0, 0);
    }

    private void draw_apple() {
        Coord a = model.getApple();
        pixmap.setColor(Color.RED);
        pixmap.drawRectangle(a.getX() * offset_x, a.getY() * offset_y, offset_x, offset_y);
    }

    private void draw_wall() {
        List<Coord> walls = model.getWalls();
        pixmap.setColor(Color.WHITE);
        for (int i = 0; i < walls.size(); i++) {
            Coord a = walls.get(i);
            pixmap.drawRectangle(a.getX() * offset_x, a.getY() * offset_y, offset_x, offset_y);
        }
    }

    private void draw_snake() {
        List<Coord> snake = model.getSnake();
        pixmap.setColor(Color.GRAY);
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
