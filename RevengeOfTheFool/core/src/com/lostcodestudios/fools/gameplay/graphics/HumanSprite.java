package com.lostcodestudios.fools.gameplay.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.SoundManager;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class HumanSprite {
	
	private static final float FRAME_SPACING = 1;
	
	private static final float BASE_FRAME_TIME = 1f; // movement animations will speed up when entities move faster, so this isn't the real frame time
	
	public enum Direction {
		Right,
		Down,
		Left,
		Up
	}
	
	private float movementSpeed = 0f;
	private Direction direction = Direction.Down;
	private ObjectMap<Direction, TextureRegion> standingSprites = new ObjectMap<Direction, TextureRegion>();
	private ObjectMap<Direction, Animation> walkingAnimations = new ObjectMap<Direction, Animation>();
	private float elapsedAnimation = 0f;
	private Vector2 origin;
	private float width, height;
	
	/**
	 * Creates a new AnimatedSprite.
	 * @param sheetPath Internal file path of the sprite sheet.
	 * @param x The x-coordinate of the top left corner of the sprite's first frame.
	 * @param y The y-coordinate of the top left corner of the sprite's first frame.
	 * @param frameWidth The width of each of the sprite's frames.
	 * @param frameHeight The height of each of the sprite's frames.
	 */
	public HumanSprite(Texture sheetTexture, int x, int y, int frameWidth, int frameHeight) {
		// set rendering values
		
		this.origin = new Vector2(frameWidth * Config.SPRITE_SCALE / 2, Human.RADIUS * Config.PIXELS_PER_METER);
		this.width = frameWidth * Config.SPRITE_SCALE;
		this.height = frameHeight * Config.SPRITE_SCALE;
		
		// iterate through rows of the sprite's frames, creating stationary sprite and walking animation for each direction
		
		int frameX = x, frameY = y;
		for (Direction direction : Direction.values()) {
			
			// create the stationary sprite
			TextureRegion standingFrame = new TextureRegion(sheetTexture, frameX, frameY, frameWidth, frameHeight);
			standingSprites.put(direction, standingFrame);
			
			// create the walking animation
			frameX += frameWidth + FRAME_SPACING;
			TextureRegion frame1 = new TextureRegion(sheetTexture, frameX, frameY, frameWidth, frameHeight);
			
			frameX += frameWidth + FRAME_SPACING;
			TextureRegion frame2 = new TextureRegion(sheetTexture, frameX, frameY, frameWidth, frameHeight);
			
			Animation walkingAnimation = new Animation(BASE_FRAME_TIME, frame1, frame2);
			walkingAnimation.setPlayMode(PlayMode.LOOP);
			walkingAnimations.put(direction, walkingAnimation);
			
			frameX = x;
			frameY += frameHeight + FRAME_SPACING;
			
		}
		
	}
	
	public void setMovementSpeed (float movementSpeed) {
		this.movementSpeed = Math.max(movementSpeed, 0f);
		
		if (this.movementSpeed == 0) {
			elapsedAnimation = 0f; //reset the walk animation if stopping
		}
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	private int lastFrame = 0;
	private int currentFrame = 0;
	
	public void update(float delta) {
		Animation currentAnimation = walkingAnimations.get(direction);
		lastFrame = currentAnimation.getKeyFrameIndex(elapsedAnimation);
		elapsedAnimation += delta * movementSpeed; // animate faster when moving faster
		currentFrame = currentAnimation.getKeyFrameIndex(elapsedAnimation);
	}
	
	public void render(SpriteBatch spriteBatch, Vector2 position, Vector2 foolPos, GameWorld world) {
		position.sub(origin);
		
		if (movementSpeed > 0) {
			Animation currentAnimation = walkingAnimations.get(direction);
			
			TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedAnimation);
			
			spriteBatch.draw(currentFrame, position.x, position.y, origin.x, origin.y, width, height, 1f, 1f, 0f);
		} else {
			TextureRegion currentSprite = standingSprites.get(direction);
			
			spriteBatch.draw(currentSprite, position.x, position.y, origin.x, origin.y, width, height, 1f, 1f, 0f);
		}
		
		if (lastFrame != currentFrame) {
			// when the frame changes, play a footstep
			SoundManager.playFootstep(position.cpy().scl(1f / Config.PIXELS_PER_METER), foolPos, world);
			
			lastFrame = 0;
			currentFrame = 0; // don't repeat if paused
		}
	}
	
}
