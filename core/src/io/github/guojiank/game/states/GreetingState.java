package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GreetingState extends State {
    Texture texture;

    Sprite sprite;


    public GreetingState(StateManagor stateManagor) {
        super(stateManagor);
        texture = new Texture("me.png");
        sprite = new Sprite(texture);
        sprite.setSize(150f,150f);
        sprite.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
    }

    @Override
    void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            stateManagor.setState(new HelpState(stateManagor));

    }

    @Override
    void render(Batch batch) {
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
