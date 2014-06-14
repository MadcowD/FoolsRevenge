package com.lostcodestudios.fools;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Revenge extends Game {	
	SpriteBatch batch;
	
	GameWorld world;
	
	@Override
	public void create () {
		Gdx.graphics.setDisplayMode(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT, false);
		
		batch = new SpriteBatch();
		
		world = new GameWorld();
		
		Config.loadFonts();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.render(batch, Gdx.graphics.getDeltaTime());
		
		if (Config.DEBUG) {
			batch.begin();
			TextManager.draw(batch, "debug", "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 14);
			batch.end();
		}
	}
}
