package snake.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import snake.SmartSnake;

import static com.badlogic.gdx.Input.Keys.*;

public class HelpScreen implements Screen {
    private Texture texture;
    private SpriteBatch batch;
    SmartSnake smartSnake;

    public HelpScreen(SmartSnake smartSnake) {
        batch = smartSnake.getBatch();
        this.smartSnake = smartSnake;
    }

    @Override
    public void show() {
        texture = new Texture("help.jpg");
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(ENTER) ||
                Gdx.input.isKeyJustPressed(SPACE) ||
                Gdx.input.isKeyJustPressed(ESCAPE)) {
            smartSnake.setScreen(new GameScreen(smartSnake));
        }
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(texture, 0, 0);
        batch.end();
        handleInput();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
