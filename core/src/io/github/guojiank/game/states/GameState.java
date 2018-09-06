package io.github.guojiank.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;

public class GameState extends State {
    Sound sound;

    public GameState(StateManagor stateManagor) {
        super(stateManagor);
        sound = Gdx.audio.newSound(Gdx.files.internal("bg.mp3"));
    }

    @Override
    void handleInput() {

    }

    @Override
    void render(Batch batch) {

    }

    @Override
    public void dispose() {
        sound.dispose();
    }
}
