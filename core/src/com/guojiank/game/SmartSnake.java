package com.guojiank.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.guojiank.game.states.GreetingState;
import com.guojiank.game.states.StateManager;

public class SmartSnake extends Game {

    SpriteBatch batch;
    StateManager stateManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        stateManager = new StateManager(batch);
        stateManager.setState(new GreetingState(stateManager));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateManager.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stateManager.dispose();
    }
}
