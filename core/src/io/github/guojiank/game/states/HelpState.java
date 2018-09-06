package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class HelpState extends State {
    Texture texture;
    long currentTime;
    final long DELAY_TIME = 1000L; //单位毫秒

    public HelpState(StateManagor stateManagor) {
        super(stateManagor);
        this.texture = new Texture("badlogic.jpg");
        currentTime = System.currentTimeMillis();
    }

    @Override
    void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            if (System.currentTimeMillis() - currentTime > DELAY_TIME)
                stateManagor.setState(new GameState(stateManagor));
        }
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
