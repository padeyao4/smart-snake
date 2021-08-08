package snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import snake.screen.GreetingScreen;


public class SmartSnake extends Game {

    static SmartSnake instance;

    SpriteBatch batch;

    public SmartSnake() {
        instance = this;
    }

    public static SmartSnake getInstance() {
        return instance;
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GreetingScreen());
    }


    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
