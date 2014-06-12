package com.lostcodestudios.fools;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lostcodestudios.fools.gameplay.EntityWorld;

public class Revenge extends Game {
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;
	
	SpriteBatch batch;
	
	EntityWorld world;
	
	@Override
	public void create () {
		Gdx.graphics.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		
		batch = new SpriteBatch();
		
		world = new EntityWorld();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.render(Gdx.graphics.getDeltaTime());
	}
}
