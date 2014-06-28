package com.lostcodestudios.fools.gameplay.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.Config;

public class ItemSprite {

	private TextureRegion textureRegion;
	
	Vector2 origin;
	float width, height;
	
	public ItemSprite(Texture sheetTexture, String sourceKey) {
		Rectangle source = Config.itemSpriteInfo.get(sourceKey);
		
		width = source.width;
		height = source.height;
		origin = new Vector2(source.width / 2, source.height / 2);
		
		textureRegion = new TextureRegion(sheetTexture, (int)source.x, (int)source.y, (int)source.width, (int)source.height);
	}
	
	public void render(SpriteBatch spriteBatch, Vector2 position, float scale) {
		float originX = origin.x * scale;
		float originY = origin.y * scale;
		
		spriteBatch.draw(textureRegion, position.x - originX, position.y - originY, originX, originY, width * scale, height * scale, 1f, 1f, 0f);
	}
	
}
