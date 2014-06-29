package com.lostcodestudios.fools.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.OrderedMap;
import com.lostcodestudios.fools.Revenge;
import com.lostcodestudios.fools.screens.MenuEntry.EntryCallback;

public class IntroScreen extends MenuScreen {

	public IntroScreen(Revenge revenge) {
		super(revenge, "Introduction");

		titleY = Gdx.graphics.getHeight() * 0.9f;
		
		OrderedMap<String, EntryCallback> entries = new OrderedMap<String, EntryCallback>();
		
		entries.put("The Fool's Revenge is a stealth game with a gameplay-driven narrative.", null);
		entries.put("You will have to start from the beginning each time you die, so be careful.", null);
		
		addList(entries, new Vector2(150, 500));
		entries.clear();
		
		entries.put("Controls: ", null);
		entries.put("Move: WASD", null);
		entries.put("Look Around: Mouse", null);
		entries.put("Drink Health Potions: Q", null);
		entries.put("Interact/Take Items: E", null);
		entries.put("Inventory: R", null);
		entries.put("Run into enemies to fight them. Attack from behind to backstab.", null);
		
		addList(entries, new Vector2(240, 400));
		
		addEntry("Back", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				revenge.getScreen().dispose();
				revenge.setScreen(new MainMenuScreen(revenge));
			}
			
		}, new Vector2(550, 50));
	}

}
