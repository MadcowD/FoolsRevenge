package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lostcodestudios.fools.Config;

public class DialogManager {
	
	private boolean wasAcceptKeyPressed = false;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	public void render(SpriteBatch spriteBatch, float delta) {
		boolean acceptKeyPressed = Gdx.input.isKeyPressed(Config.ACCEPT_KEY);
		
		if (acceptKeyPressed && !wasAcceptKeyPressed) {
			//handle a new press of the accept key
			
		}
		
		wasAcceptKeyPressed = acceptKeyPressed;
		
		
		//TODO delete this test code
		new Dialog("dialog-white", "ui-white", 
				"The King's rage shook the castle like thunder.", 
				Color.BLACK, Color.BLACK, true, false, 
				HAlignment.CENTER).render(spriteBatch, shapeRenderer);
	}
	
}
