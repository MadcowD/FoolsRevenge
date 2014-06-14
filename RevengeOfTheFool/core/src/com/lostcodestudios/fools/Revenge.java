package com.lostcodestudios.fools;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Revenge extends Game {	
	private SpriteBatch batch;
	
	private GameWorld world;
	
	private InputManager input;
	
	@Override
	public void create () {
		Gdx.graphics.setDisplayMode(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT, false);
		Gdx.graphics.setTitle("The Fool's Revenge");
		
		batch = new SpriteBatch();
		
		input = new InputManager();
		Gdx.input.setInputProcessor(input);
		
		world = new GameWorld(input);
		
		Config.loadFonts();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.render(batch, Gdx.graphics.getDeltaTime());
		
		if (input.wasKeyPressed(Keys.F1)) {
			Config.debug = !Config.debug;
		}
		
		input.beginNewFrame();
		
		if (Config.debug) {
			batch.begin();
			TextManager.draw(batch, "debug", "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 14);
			batch.end();
		}
	}
}
