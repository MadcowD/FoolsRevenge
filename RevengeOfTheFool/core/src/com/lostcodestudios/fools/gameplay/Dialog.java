package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.TextManager;

public class Dialog {
	
	private static final float DIALOG_WIDTH = Config.SCREEN_WIDTH / 2;
	
	private static final float DIALOG_X = Config.SCREEN_WIDTH - 3 * DIALOG_WIDTH / 2;
	private static final float DIALOG_Y = 2 * Config.SCREEN_HEIGHT / 3;
	
	private static final float DIALOG_BORDER_THICKNESS = 8;
	
	private static final float DIALOG_PADDING_X = 32;
	private static final float DIALOG_PADDING_Y = 32;
	
	public String fontKey;
	public String uiFontKey;
	public String text;
	
	public Color bgColor1;
	public Color bgColor2;
	public Color bgColor3;
	public Color bgColor4;
	
	public Color borderColor;
	
	public boolean drawFullBackground;
	public boolean drawBorder;
	
	public HAlignment alignment;
	
	public Dialog(String fontKey, String uiFontKey, String text, Color backgroundColor, Color borderColor, 
			boolean drawFullBackground, boolean drawBorder, HAlignment alignment) {
		
		this.fontKey = fontKey;
		this.uiFontKey = uiFontKey;
		this.text = text;
		
		this.bgColor1 = backgroundColor;
		this.bgColor2 = backgroundColor;
		this.bgColor3 = backgroundColor;
		this.bgColor4 = backgroundColor;
		
		this.borderColor = borderColor;
		
		this.drawFullBackground = drawFullBackground;
		this.drawBorder = drawBorder;
		this.alignment = alignment;
	}
	
	public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		
		spriteBatch.begin();
		
		//preliminary draw call must be done to get wrapped text measurement - there's no other way
		TextBounds bounds = TextManager.drawWrapped(spriteBatch, fontKey, text, 
				DIALOG_X + DIALOG_PADDING_X, DIALOG_Y - DIALOG_PADDING_Y, DIALOG_WIDTH - 2 * DIALOG_PADDING_X, alignment);
		
		spriteBatch.end();
		
		//now use the measurement to draw background and border
		float textHeight = bounds.height;
		
		float x, y, width, height;
		
		if (drawFullBackground) {
			x = 0;
			y = 0;
			width = Config.SCREEN_WIDTH;
			height = Config.SCREEN_HEIGHT;
		} else {
			width = DIALOG_WIDTH;
			height = bounds.height + 2 * DIALOG_PADDING_Y + TextManager.getFont(uiFontKey).getLineHeight() * (3f/2);
			
			x = DIALOG_X;
			y = DIALOG_Y - height; //rectangles use the bottom-left corner, text uses top-left. Adjust for that
		}
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(x, y, width, height, bgColor1, bgColor2, bgColor3, bgColor4);
		
		if (drawBorder) {
			shapeRenderer.setColor(borderColor);
			shapeRenderer.rect(x, y, width, DIALOG_BORDER_THICKNESS);
			shapeRenderer.rect(x, y, DIALOG_BORDER_THICKNESS, height);
			shapeRenderer.rect(x, DIALOG_Y - DIALOG_BORDER_THICKNESS, width, DIALOG_BORDER_THICKNESS);
			shapeRenderer.rect(x + width - DIALOG_BORDER_THICKNESS, y, DIALOG_BORDER_THICKNESS, height);
		}
		
		shapeRenderer.end();
		
		spriteBatch.begin();
		
		//now draw the text for real.
		TextManager.drawWrapped(spriteBatch, fontKey, text, 
				DIALOG_X + DIALOG_PADDING_X, DIALOG_Y - DIALOG_PADDING_Y, DIALOG_WIDTH - 2 * DIALOG_PADDING_X, alignment);
		
		//And draw an accept message
		TextManager.drawMultiline(spriteBatch, uiFontKey, Config.ACCEPT_TEXT, 
				DIALOG_X + DIALOG_PADDING_X, DIALOG_Y - DIALOG_PADDING_Y - textHeight - TextManager.getFont(uiFontKey).getLineHeight(), 
				DIALOG_WIDTH - 2 * DIALOG_PADDING_X, HAlignment.RIGHT);
		
		spriteBatch.end();
	}
	
}
