package snake.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import snake.SmartSnake;

import static com.badlogic.gdx.Input.Keys.*;

public class GreetingScreen implements Screen {
    Texture texture;
    SmartSnake smartSnake;

    public GreetingScreen(SmartSnake smartSnake) {
        this.smartSnake = smartSnake;
    }

    @Override
    public void show() {
        texture = new Texture("splash2.jpg");
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(ENTER) ||
                Gdx.input.isKeyJustPressed(SPACE) ||
                Gdx.input.isKeyJustPressed(ESCAPE)) {
            smartSnake.setScreen(new HelpScreen(smartSnake));
        }
    }

    private static float REMAIN = 5f;
    private float sum = 0;

    @Override
    public void render(float delta) {
        sum += delta;
        if (sum >= REMAIN) {
            smartSnake.setScreen(new HelpScreen(smartSnake));
        }
        smartSnake.batch.begin();
        smartSnake.batch.setColor(1, 1, 1, gradientAlpha(sum));
        smartSnake.batch.draw(texture, 0, 0);
        smartSnake.batch.setColor(1, 1, 1, 1);
        smartSnake.batch.end();
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
