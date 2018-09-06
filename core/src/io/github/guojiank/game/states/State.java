package io.github.guojiank.game.states;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class State {
    StateManagor stateManagor;

    public State(StateManagor stateManagor) {
        this.stateManagor = stateManagor;
    }

    abstract void handleInput();

    abstract void render(Batch batch);

    void update() {
        handleInput();
    }

    public abstract void dispose();
}
