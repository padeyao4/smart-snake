package com.guojiank.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StateManager {
    State state;
    SpriteBatch batch;

    public StateManager(SpriteBatch batch) {
        this.batch = batch;
    }

    public void render() {
        state.render(batch);
    }


    public void dispose() {
        state.dispose();
    }

    public void setState(State state) {
        if (this.state != null) {
            dispose();
        }
        this.state = state;
    }
}
