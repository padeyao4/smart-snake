package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class GreetingState extends State {
    Texture texture;

    public GreetingState(StateManagor stateManagor) {
        super(stateManagor);
        texture = new Texture("me.png");
    }

    @Override
    void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            stateManagor.setState(new HelpState(stateManagor));

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
    }
}
