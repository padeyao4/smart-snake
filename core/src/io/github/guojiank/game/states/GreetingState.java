package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class GreetingState extends State {
    Texture texture;
    long currentTime;
    final long DELAY_TIME = 1000L; // 单位毫秒


    public GreetingState(StateManagor stateManagor) {
        super(stateManagor);
        texture = new Texture("me.png");
        currentTime = System.currentTimeMillis();
    }

    @Override
    void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER))
            if (System.currentTimeMillis() - currentTime > DELAY_TIME)
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
