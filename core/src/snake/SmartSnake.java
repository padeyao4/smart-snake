package snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import snake.screen.GreetingScreen;


public class SmartSnake extends Game {
    public SpriteBatch batch;

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        super.render();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GreetingScreen(this));
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
