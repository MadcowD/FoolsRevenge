package com.lostcodestudios.fools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.ObjectMap;

public final class TextManager {

	private static ObjectMap<String, BitmapFont> fonts;
	
	public static void addFont(String key, String ttfPath, int size, Color color) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(ttfPath));
		
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = size;
		
		BitmapFont font = generator.generateFont(parameter);
		font.setColor(color);
		
		fonts.put(key, font);
		
		generator.dispose();
	}
	
	public static TextBounds draw(SpriteBatch spriteBatch, String fontKey, String text, 
			float x, float y) {
		
		return fonts.get(fontKey).draw(spriteBatch, text, x, y);
		
	}
	
	public static TextBounds drawMultiline(SpriteBatch spriteBatch, String fontKey, 
			String text, float x, float y) {
		
		return fonts.get(fontKey).drawMultiLine(spriteBatch, text, x, y);
		
	}
	
	public static TextBounds drawMultiline(
			SpriteBatch spriteBatch, String fontKey, 
			String text, float x, float y, 
			float alignmentWidth, HAlignment alignment) {
		
		return fonts.get(fontKey).drawMultiLine(spriteBatch, text, x, y, alignmentWidth, alignment);
		
	}
	
	public static TextBounds drawWrapped(SpriteBatch spriteBatch, String fontKey, 
			String text, float x, float y, float wrapWidth) {
		
		return fonts.get(fontKey).drawWrapped(spriteBatch, text, x, y, wrapWidth);
		
	}
	
	public static TextBounds drawWrapped(SpriteBatch spriteBatch, String fontKey, 
			String text, float x, float y, float wrapWidth, HAlignment alignment) {
		
		return fonts.get(fontKey).drawWrapped(spriteBatch, text, x, y, wrapWidth, alignment);
		
	}
	
}
