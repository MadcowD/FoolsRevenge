package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.TextManager;
import com.lostcodestudios.fools.gameplay.entities.Entity;

public class VoiceBubble {

	private static final float BUBBLE_PADDING = 24f;
	private static final float BORDER_WIDTH = 8f;
	private static final Color BUBBLE_COLOR = Color.WHITE;
	private static final Color BORDER_COLOR = Color.GRAY;
	
	private String text;
	private Entity speaker;
	private float timeRemaining;
	
	public VoiceBubble(String text, Entity speaker, float time) {
		this.text = text;
		this.speaker = speaker;
		this.timeRemaining = time;
	}
	
	/**
	 * Updates the voice bubble's lifespan timer.
	 * @param delta
	 * @return True if the bubble has run out of time and should be removed.
	 */
	public boolean update(float delta) {
		timeRemaining -= delta;
		
		return timeRemaining <= 0;
	}
	
	public void render(SpriteBatch worldSpriteBatch, ShapeRenderer shapeRenderer) {
		Vector2 position = speaker.getPosition().scl(GameWorld.PIXELS_PER_METER);
		
		position.y += 64;
		
		TextBounds bounds = TextManager.getFont("voice").getBounds(text);
		
		Rectangle bubbleRect = new Rectangle(
				position.x - bounds.width / 2 - BUBBLE_PADDING / 2, 
				position.y - bounds.height / 2 - BUBBLE_PADDING / 2,
				bounds.width + BUBBLE_PADDING, bounds.height + BUBBLE_PADDING);
		
		shapeRenderer.setColor(BUBBLE_COLOR);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(bubbleRect.x, bubbleRect.y, bubbleRect.width, bubbleRect.height);
		
		shapeRenderer.setColor(BORDER_COLOR);
		shapeRenderer.rect(bubbleRect.x - BORDER_WIDTH, bubbleRect.y, BORDER_WIDTH, bubbleRect.height);
		shapeRenderer.rect(bubbleRect.x, bubbleRect.y - BORDER_WIDTH, bubbleRect.width, BORDER_WIDTH);
		shapeRenderer.rect(bubbleRect.x, bubbleRect.y + bubbleRect.height, bubbleRect.width, BORDER_WIDTH);
		shapeRenderer.rect(bubbleRect.x + bubbleRect.width, bubbleRect.y, BORDER_WIDTH, bubbleRect.height);
		
		shapeRenderer.end();
		
		worldSpriteBatch.begin();
		TextManager.draw(worldSpriteBatch, "voice", text, position.x - bounds.width / 2, position.y + bounds.height / 2);
		worldSpriteBatch.end();
	}
	
}
