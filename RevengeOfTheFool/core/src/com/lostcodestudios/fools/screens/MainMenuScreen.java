package com.lostcodestudios.fools.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.OrderedMap;
import com.lostcodestudios.fools.Revenge;
import com.lostcodestudios.fools.screens.MenuEntry.EntryCallback;

public class MainMenuScreen extends MenuScreen {

	public MainMenuScreen(Revenge revenge) {
		super(revenge, "The Fool's Revenge");
		
		OrderedMap<String, EntryCallback> entries = new OrderedMap<String, EntryCallback>();
		
		entries.put("Introduction", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				revenge.getScreen().dispose();
				revenge.setScreen(new IntroScreen(revenge));
			}
			
		});
		
		entries.put("Play", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				revenge.getScreen().dispose();
				
				GameplayScreen gameScreen = new GameplayScreen(revenge);
				
				revenge.setScreen(gameScreen);
			}
			
		});
		
		entries.put("Options", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				//revenge.setScreen(new OptionsMenuScreen(revenge, menu));
			}
			
		});
		
		entries.put("Credits", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				revenge.getScreen().dispose();
				revenge.setScreen(new CreditScreen(revenge));
			}
			
		});
		
		addEntry("Quit", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				Gdx.app.exit();
			}
			
		}, new Vector2(1150, 56));
		
		addList(entries, new Vector2(400, 400));
	}

}
