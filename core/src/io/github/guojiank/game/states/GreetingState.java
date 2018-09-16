package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class GreetingState extends State {
    Texture texture;



    public GreetingState(StateManager stateManager) {
        super(stateManager);
        texture = new Texture("splash2.jpg");
    }

    void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
                Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                        Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            stateManager.setState(new HelpState(stateManager));

    }

    @Override
    void render(Batch batch) {
        batch.begin();
        batch.draw(texture,0,0);
        batch.end();
        handleInput();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
