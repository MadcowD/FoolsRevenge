package com.lostcodestudios.fools.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.InputManager;
import com.lostcodestudios.fools.Revenge;
import com.lostcodestudios.fools.TextManager;

public class MenuEntry {
	
	public interface EntryCallback {
		public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry);
	}
	
	public static final String ENTRY_FONT = "menu-entry";
	public static final String ENTRY_FONT_SELECTED = "menu-entry-selected";
	public static final String LABEL_FONT = "menu-label";
	
	private String text;
	private Vector2 position;
	private Rectangle bounds;
	private EntryCallback onSelect;
	
	public MenuEntry(String text, Vector2 position, EntryCallback onSelect) {
		this.position = position;
		
		setText(text);
		
		this.onSelect = onSelect;
	}
	
	public void setText(String text) {
		this.text = text;
		
		TextBounds textBounds = TextManager.getFont(ENTRY_FONT).getBounds(text);
		
		bounds = new Rectangle(position.x, position.y - textBounds.height, textBounds.width, textBounds.height);
	}
	
	public void render(Revenge revenge, MenuScreen menu, SpriteBatch spriteBatch, InputManager input) {
		boolean selected = false;
		if (onSelect != null) {
			selected = input.containsMouse(bounds);
		}
		
		String font = (selected ? ENTRY_FONT_SELECTED : ENTRY_FONT);
		
		if (onSelect == null) {
			font = LABEL_FONT;
		}
		
		TextManager.draw(spriteBatch, font, text, position.x, position.y);
		
		// handle this entry's selection event
		if (selected && input.wasMouseClicked()) {
			onSelect.invoke(revenge, menu, this);
		}
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
}
