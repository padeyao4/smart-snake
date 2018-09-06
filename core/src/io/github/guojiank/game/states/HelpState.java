package io.github.guojiank.game.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class HelpState extends State {
    Texture texture;

    public HelpState(StateManagor stateManagor) {
        super(stateManagor);
        this.texture = new Texture("badlogic.jpg");
    }

    @Override
    void handleInput() {

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