package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;

import static com.badlogic.gdx.Input.Keys.SPACE;

public class GameState extends State {
    final static long DELAY_TIME = 200L;
    Sound sound;
    boolean musicPlay = true;
    long currentTime;

    public GameState(StateManagor stateManagor) {
        super(stateManagor);
        sound = Gdx.audio.newSound(Gdx.files.internal("WhereIstheLove.mp3"));
        sound.loop();
        sound.play();
        currentTime = System.currentTimeMillis();
    }

    @Override
    void handleInput() {
        if (Gdx.input.isKeyPressed(SPACE)) {
            if (System.currentTimeMillis() - currentTime > DELAY_TIME) {
                currentTime = System.currentTimeMillis();
                if (musicPlay = !musicPlay) {
                    sound.pause();
                } else {
                    sound.resume();
                }
            }

        }
    }

    @Override
    void render(Batch batch) {

    }

    @Override
    public void dispose() {
        sound.dispose();
    }
}
