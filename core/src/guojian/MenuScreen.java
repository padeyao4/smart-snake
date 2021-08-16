package guojian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen extends ScreenAdapter {
    Stage stage;

    public MenuScreen(SmartSnake smartSnake) {
        var table = new Table();
        table.setFillParent(true);
        table.defaults().pad(10); // 设置所有单元格默认内边距为10

        table.add(new Label("SNAKE GAME", getTitleStyle(smartSnake))).padBottom(50);
        table.row();
        table.add(getPlayButton(smartSnake));
        table.row();
        var autoPlayButton = new TextButton("[W] atch Greedy Snake Player", smartSnake.skin);
        table.add(autoPlayButton);

        stage = new Stage(new ScreenViewport(), smartSnake.batch);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private TextButton getPlayButton(SmartSnake smartSnake) {
        var playButton = new TextButton("[P] lay Snake Game", smartSnake.skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                smartSnake.setScreen(new GameScreen(smartSnake.batch));
            }
        });
        return playButton;
    }

    private Label.LabelStyle getTitleStyle(SmartSnake smartSnake) {
        var bitmapFont = smartSnake.skin.getFont("font");
        bitmapFont.getData().setScale(1.5f);
        var titleStyle = new Label.LabelStyle(bitmapFont, Color.WHITE);
        return titleStyle;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
