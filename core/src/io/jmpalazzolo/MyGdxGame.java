package io.jmpalazzolo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.jmpalazzolo.states.GameSateManager;
import io.jmpalazzolo.states.MenuState;

// ApplicationAdapter to only override needed methods, other way use ApplicationListener interface
public class MyGdxGame extends ApplicationAdapter {

	// The size of the app is set as a constant
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	public static final String TITLE = "Flappy Bird";

	private GameSateManager gsm;
	private SpriteBatch batch;

	// Music is streamed from HD
	private Music music;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameSateManager();

		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);
		music.play();

		Gdx.gl.glClearColor(1, 0, 0, 1);

		// Main menu loaded on app creation
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Update and render what the GameSateManager has on the stack.peek()
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		music.dispose();
	}
}
