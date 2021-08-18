package guojian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ScreenUtils;

public class SplashScreen extends ScreenAdapter {
    Batch batch;
    Texture splash;

    public SplashScreen(Batch batch) {
        this.batch = batch;
        splash = new Texture("splash2.jpg");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        batch.begin();
        batch.draw(splash, (Gdx.graphics.getWidth() - splash.getWidth()) / 2f, (Gdx.graphics.getHeight() - splash.getHeight()) / 2f, splash.getWidth(), splash.getHeight());
        batch.end();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        splash.dispose();
    }
}
