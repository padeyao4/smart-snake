package guojian;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import guojian.core.Snake;
import guojian.core.Point;

import static com.badlogic.gdx.Input.Keys.*;

import java.util.List;

import static com.badlogic.gdx.maps.tiled.TiledMapTileLayer.*;
import static guojian.SmartSnake.*;
import static guojian.core.Algorithm.findBestPath;

public class GameScreen extends ScreenAdapter {
    Cell backgroundCell;
    Cell snakeCell;
    Cell foodCell;

    Music bg;

    OrthographicCamera camera;
    Batch batch;
    Snake snake;

    TiledMap tiledMap;
    TiledMapTileLayer mapTileLayer;
    TiledMapRenderer renderer;

    Stage hub;
    SmartSnake smartSnake;

    public GameScreen(SmartSnake smartSnake) {
        this.batch = smartSnake.batch;
        this.smartSnake = smartSnake;
    }

    @Override
    public void show() {
        tiledMap = new TiledMap();
        mapTileLayer = new TiledMapTileLayer(GRID_WIDTH, GRID_HEIGHT, TITLED_SIZE, TITLED_SIZE);
        tiledMap.getLayers().add(mapTileLayer);
        renderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, (GRID_WIDTH - 2) * TITLED_SIZE, (GRID_HEIGHT - 2) * TITLED_SIZE);
        camera.translate(16, 16);

        bg = Gdx.audio.newMusic(Gdx.files.internal("WhereIsTheLove.mp3"));
        bg.setLooping(true);
        bg.play();

        backgroundCell = new Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("snake/blue.gif"))));
        snakeCell = new Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("snake/indigo.png"))));
        foodCell = new Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("snake/pumpkin.png"))));

        snake = new Snake();
        snake.init();
        snake.start();

        createHub();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case SPACE, ENTER -> snake.startOrStop(); // 暂停和恢复
                    case F1 -> snake.init(); // 重新开始游戏
                    case F2 -> bgPlayOrStop();
                    case ESCAPE -> Gdx.app.exit(); // 退出
                    case BACKSPACE -> smartSnake.setScreen(new MenuScreen(smartSnake));
                }
                return false;
            }
        });
    }

    Label scoreLabel;

    /**
     * 创建hub
     */
    private void createHub() {
        hub = new Stage(new ScreenViewport(), batch);
        var table = new Table();
        table.setFillParent(true);
        table.top();
        table.row();
        scoreLabel = new VisLabel("SCORE " + snake.getPositions().size());

        table.add(scoreLabel).expandX().padTop(20);
        table.add(new VisLabel("[F1] RESTART")).expandX().padTop(20);
        table.add(new VisLabel("[F2] PAUSE BGM")).expandX().padTop(20);
        table.add(new VisLabel("[ENTER / SPACE] PAUSE OR PLAY GAME")).expandX().padTop(20);
        table.add(new VisLabel("[ESC] EXIT GAME")).expandX().padTop(20);
        table.add(new VisLabel("[BACK] BACK TO MENU")).expandX().padTop(20);

        hub.addActor(table);
    }

    private void bgPlayOrStop() {
        if (bg.isPlaying()) {
            bg.pause();
        } else {
            bg.play();
        }
    }

    @Override
    public void render(float delta) {
        updateGame(delta);
        updateTiled();
        updateHub();
        ScreenUtils.clear(Color.CLEAR);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        hub.draw();
    }

    private void updateHub() {
        scoreLabel.setText("SCORE: " + snake.getPositions().size());
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        bg.dispose();
        hub.dispose();
        tiledMap.dispose();
    }

    float tmpTime = 0;
    float dtTime = 0; //间隔时间

    private void updateGame(float delta) {
        tmpTime += delta;
        if (tmpTime > dtTime) {
            tmpTime -= dtTime;
            snake.update();

            List<Point> path = findBestPath(snake.positions.getLast(), snake.getFood(), snake.getSnakeTail(), snake);
            if (path != null) {
                snake.setBestPath(path);
            }
        }
    }

    private void updateTiled() {
        var world = snake.getCellTypes();
        for (int i = 0; i < GRID_HEIGHT; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                var cell = world[i][j];
                switch (cell) {
                    case BLANK -> mapTileLayer.setCell(j, i, backgroundCell);
                    case SNAKE -> mapTileLayer.setCell(j, i, snakeCell);
                }
            }
        }
        var apple = snake.getFood();
        mapTileLayer.setCell(apple.getX(), apple.getY(), foodCell);
    }
}
