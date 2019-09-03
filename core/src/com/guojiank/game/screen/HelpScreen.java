package com.guojiank.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guojiank.game.SmartSnake;

import static com.badlogic.gdx.Input.Keys.*;

public class HelpScreen implements Screen {
    private Texture texture;
    private SpriteBatch batch;

    @Override
    public void show() {
        texture = new Texture("help.jpg");
        batch = SmartSnake.getInstance().getBatch();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(ENTER) ||
                Gdx.input.isKeyJustPressed(SPACE) ||
                Gdx.input.isKeyJustPressed(ESCAPE)) {
            SmartSnake.getInstance().setScreen(new GameScreen());
        }
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
        handleInput();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
