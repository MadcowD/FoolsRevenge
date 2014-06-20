package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Corpse extends Entity {

	private Vector2 position;
	private TextureRegion spriteRegion;
	private Vector2 origin;
	
	private float width, height;
	
	public Corpse(Texture spriteSheet, Vector2 position, String spriteKey) {
		super(2);
		
		this.position = position.cpy();
		
		Rectangle frame = Config.corpseSpriteInfo.get(spriteKey);
		spriteRegion = new TextureRegion(spriteSheet, (int)frame.x, (int)frame.y, (int)frame.width, (int)frame.height);
		
		origin = new Vector2(spriteRegion.getRegionWidth() * Config.SPRITE_SCALE / 2, 0);
		
		width = spriteRegion.getRegionWidth() * Config.SPRITE_SCALE;
		height = spriteRegion.getRegionHeight() * Config.SPRITE_SCALE;
	}

	@Override
	public void render(float deltaTime, GameWorld gameWorld) {
		gameWorld.spriteBatch.draw(spriteRegion, position.x * Config.PIXELS_PER_METER - origin.x, position.y * Config.PIXELS_PER_METER - origin.y,
				origin.x, origin.y, width, height, 1f, 1f, 0f);
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

}
