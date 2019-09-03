package com.guojiank.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guojiank.game.screen.GreetingScreen;
import lombok.Getter;

public class SmartSnake extends Game {

    @Getter
    static SmartSnake instance;

    @Getter
    SpriteBatch batch;

    public SmartSnake() {
        instance = this;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GreetingScreen());
    }


    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
