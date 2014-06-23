package com.lostcodestudios.fools.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;
import com.lostcodestudios.fools.InputManager;
import com.lostcodestudios.fools.Revenge;
import com.lostcodestudios.fools.TextManager;
import com.lostcodestudios.fools.screens.MenuEntry.EntryCallback;

public class MenuScreen implements Screen {

	private static final String TITLE_FONT = "menu-title";
	private static final float TITLE_Y = 3f * Gdx.graphics.getHeight() / 4f;
	
	private static final float LIST_SPACING = 16f;
	
	protected Revenge revenge;
	protected SpriteBatch spriteBatch;
	protected InputManager input;
	
	private String title;
	private Array<MenuEntry> entries = new Array<MenuEntry>();
	
	protected float titleY = TITLE_Y;
	
	public MenuScreen(Revenge revenge, String title) {
		this.revenge = revenge;
		this.spriteBatch = revenge.batch;
		this.input = revenge.input;
		
		this.title = title;
	}
	
	public void addEntry(String text, EntryCallback callback, Vector2 position) {
		MenuEntry entry = new MenuEntry(text, position.cpy(), callback);
		
		entries.add(entry);
	}
	
	/**
	 * Adds a collection of MenuEntries to the MenuScreen in a vertical left-aligned list
	 * @param listEntries
	 * @param position
	 */
	public void addList(OrderedMap<String, EntryCallback> listEntries, Vector2 position) {
		Iterator<Entry<String, EntryCallback>> it = listEntries.iterator();

		Vector2 pos = position.cpy();
		
		while (it.hasNext()) {
			Entry<String, EntryCallback> entry = it.next();
			
			MenuEntry menuEntry = new MenuEntry(entry.key, pos.cpy(), entry.value);
			
			entries.add(menuEntry);
			
			pos.sub(new Vector2(0, menuEntry.getBounds().height + LIST_SPACING));
		}
	}
	
	@Override
	public void render(float delta) {
		spriteBatch.begin();
		
		TextManager.drawMultiline(spriteBatch, TITLE_FONT, title, 0, titleY, Gdx.graphics.getWidth(), HAlignment.CENTER);
		
		for (MenuEntry entry : entries) {
			entry.render(revenge, this, spriteBatch, input);
		}
		
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}
