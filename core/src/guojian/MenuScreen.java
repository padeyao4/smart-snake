package guojian;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import guojian.menu.MenuInputProcessor;
import guojian.menu.Star;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends ScreenAdapter {
    SmartSnake smartSnake;
    Stage stage;
    List<Star> stars;
    OrthographicCamera camera;
    World world;
    RayHandler rayHandler; // 光线处理类

    public MenuScreen(SmartSnake smartSnake) {
        this.smartSnake = smartSnake;
    }

    @Override
    public void show() {
        // ui
        var table = new Table();
        table.setFillParent(true);
        table.defaults().pad(10); // 设置所有单元格默认内边距为10
        table.add(new Label("SNAKE GAME", getTitleStyle(smartSnake))).padBottom(50);
        table.row();
        table.add(getPlayButton());
        table.row();
        table.add(getAutoPlayButton());
        table.row();
        table.add(getExitButton());
        stage = new Stage(new ScreenViewport(), smartSnake.batch);
        stage.addActor(table);
        // 多处理器
        var multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new MenuInputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.ESCAPE -> Gdx.app.exit();
                    case Input.Keys.W -> System.out.println("watch");
                    case Input.Keys.P -> playGame();
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);

        // background particle
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        world = new World(Vector2.Zero, true);
        rayHandler = new RayHandler(world); // 创建光线处理对象
        rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 1f); // 设置环境光
        rayHandler.setBlurNum(3); // 设置模糊线程
        rayHandler.setShadows(false); // 设置阴影
        stars = new ArrayList<>();
        for (int i = 0; i < RandomUtils.nextInt(3, 10); i++) {
            stars.add(new Star(rayHandler));
        }
    }

    private TextButton getAutoPlayButton() {
        var autoPlayButton = new TextButton("[W] atch Greedy Snake Player", smartSnake.skin);
        autoPlayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("auto play");
            }
        });
        return autoPlayButton;
    }

    private TextButton getExitButton() {
        var exitButton = new TextButton("[ESC] Exit", smartSnake.skin);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        return exitButton;
    }

    private TextButton getPlayButton() {
        var playButton = new TextButton("[P] lay Snake Game", smartSnake.skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playGame();
            }
        });
        return playButton;
    }

    private void playGame() {
        smartSnake.setScreen(new GameScreen(smartSnake.batch));
    }

    private Label.LabelStyle getTitleStyle(SmartSnake smartSnake) {
        var bitmapFont = smartSnake.skin.getFont("font");
        bitmapFont.getData().setScale(1.5f);
        return new Label.LabelStyle(bitmapFont, Color.WHITE);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.CLEAR);
        stars.forEach(s -> {
            s.acc(stars);
            s.update(Gdx.graphics.getDeltaTime());
        });
        world.step(1f / 60f, 6, 2);
        camera.update();
        rayHandler.setCombinedMatrix(camera.combined, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
        rayHandler.updateAndRender();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        rayHandler.dispose();
    }
}
