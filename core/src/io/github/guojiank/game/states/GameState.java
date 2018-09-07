package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;

import static com.badlogic.gdx.Input.Keys.SPACE;

public class GameState extends State {
    Sound sound;
    boolean musicPlay = false;

    public GameState(StateManagor stateManagor) {
        super(stateManagor);
        sound = Gdx.audio.newSound(Gdx.files.internal("WhereIstheLove.mp3"));
        sound.loop();
        sound.play();
    }

    @Override
    void handleInput() {
        if (Gdx.input.isKeyJustPressed(SPACE)) {
            if (musicPlay = !musicPlay) {
                sound.pause();
            } else {
                sound.resume();
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
