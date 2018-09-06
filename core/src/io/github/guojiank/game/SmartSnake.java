package io.github.guojiank.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.guojiank.game.states.GreetingState;
import io.github.guojiank.game.states.StateManagor;

public class SmartSnake extends Game {
	SpriteBatch batch;
	StateManagor stateManagor;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		stateManagor = new StateManagor(batch);
		stateManagor.setState(new GreetingState(stateManagor));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateManagor.render();
		stateManagor.update();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		stateManagor.dispose();
	}
}
