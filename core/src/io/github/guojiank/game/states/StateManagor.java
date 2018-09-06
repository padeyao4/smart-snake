package io.github.guojiank.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StateManagor {
    State state;
    SpriteBatch batch;

    public StateManagor(SpriteBatch batch) {
        this.batch = batch;
    }

    public void render() {
        state.render(batch);
    }

    public void update() {
        state.update();
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
