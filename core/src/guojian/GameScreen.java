package guojian;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {
    Batch batch;
    BitmapFont font;

    public GameScreen(Batch batch) {
        this.batch = batch;
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        batch.begin();
        font.draw(batch,"hello",100,100);
        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
