package guojian;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.kotcrab.vis.ui.VisUI;

public class SmartSnake extends Game {
    public static final int GRID_WIDTH = 20;
    public static final int GRID_HEIGHT = 20;
    public static final int TITLED_SIZE = 16;
    public static final boolean HUB_VISIBLE = false;

    /**
     * splash画面的显示时间
     */
    static int SPLASH_MINIMUM_MILLIS = 3;
    Batch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        VisUI.load();
        setScreen(new SplashScreen(batch));
        new Thread(() -> Gdx.app.postRunnable(this::run)).start();// 设置2秒的定时任务，2秒过后启动启动主菜单界面
    }

    @Override
    public void dispose() {
        batch.dispose();
        VisUI.dispose();
    }

    private void run() {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                setScreen(new MenuScreen(SmartSnake.this));
            }
        }, SPLASH_MINIMUM_MILLIS);
    }
}
