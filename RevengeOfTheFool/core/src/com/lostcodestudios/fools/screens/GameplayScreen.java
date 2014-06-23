package com.lostcodestudios.fools.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lostcodestudios.fools.InputManager;
import com.lostcodestudios.fools.Revenge;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class GameplayScreen implements Screen {

	private GameWorld world;
	private Revenge revenge;
	private SpriteBatch spriteBatch;
	private InputManager input;
	
	public GameplayScreen(Revenge revenge) {
		this.revenge = revenge;
		this.input = revenge.input;
		this.spriteBatch = revenge.batch;
		
		world = new GameWorld(input);
	}
	
	@Override
	public void render(float delta) {
		world.render(spriteBatch, delta);
		
		if (input.wasKeyPressed(Keys.ESCAPE)) {
			revenge.setScreen(new PauseMenuScreen(revenge, this));
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		world.resume();
		
		if (!world.started) {
			world.start();
			world.started = true;
		}
	}

	@Override
	public void hide() {
		world.pause();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		world.dispose();
	}

}
