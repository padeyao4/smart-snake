package io.github.guojiank.game.states;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class State {
    StateManager stateManager;

    public State(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    abstract void render(Batch batch);

    public abstract void dispose();
}
