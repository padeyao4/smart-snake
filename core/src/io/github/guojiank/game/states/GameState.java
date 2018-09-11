package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.T;

public class GameState extends State {
    Pixmap pixmap;
    Texture texture;
    Random r;

    public GameState(StateManagor stateManagor) {
        super(stateManagor);
        r = new Random();
        pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);
        pixmap.setColor(Color.GREEN);
        texture = new Texture(pixmap);
    }

    @Override
    void handleInput() {

    }

    @Override
    void update() {
        super.update();
        long t = System.currentTimeMillis();
        int x2 = (int) (t % 10000);
        int y2 = (int) (t % 1000);
        // 画一条线段, 线段两点为 (0, 0) 到 (256, 128)
        pixmap.drawLine(0, 0, x2 / 1000, y2 / 100);
        texture.draw(pixmap, 0, 0);
    }

    @Override
    void render(Batch batch) {
        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();

    }

    @Override
    public void dispose() {
        texture.dispose();
        pixmap.dispose();
    }
}
