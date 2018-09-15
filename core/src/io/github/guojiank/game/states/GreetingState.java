package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GreetingState extends State {
    Texture texture;

    Sprite sprite;


    public GreetingState(StateManager stateManager) {
        super(stateManager);
        texture = new Texture("me.png");
        sprite = new Sprite(texture);
        sprite.setSize(150f, 150f);
        sprite.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    }

    void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            stateManager.setState(new HelpState(stateManager));

    }

    @Override
    void render(Batch batch) {
        batch.begin();
        sprite.draw(batch);
        batch.end();
        handleInput();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
