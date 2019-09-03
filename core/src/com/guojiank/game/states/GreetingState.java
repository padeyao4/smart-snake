package com.guojiank.game.states;

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

    float REMAIN = 5f;
    float sum=0;
    @Override
    void render(Batch batch) {
        float delta= Gdx.graphics.getDeltaTime();
        sum += delta;

        if (sum >= REMAIN) {
            stateManager.setState(new HelpState(stateManager));
        }
        batch.begin();
        batch.setColor(1, 1, 1, gradientAlpha(sum));
        batch.draw(texture,0,0);
        batch.setColor(1, 1, 1, 1);
        batch.end();
        handleInput();
    }

    private float gradientAlpha(float x) {
        final int param = 2; //渐变时间
        float y;
        if (x < param && x >= 0) {
            y = (float) (Math.sin(x * Math.PI / param + 3 * Math.PI / 2) + 1) / 2;
        } else if (x >= param && x < (REMAIN - param)) {
            y = 1;
        } else {
            x = (REMAIN - x);
            y = (float) (Math.sin(x * Math.PI / param + 3 * Math.PI / 2) + 1) / 2;
        }
        return y;
    }
    @Override
    public void dispose() {
        texture.dispose();
    }
}
