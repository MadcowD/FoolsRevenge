package com.lostcodestudios.fools.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.OrderedMap;
import com.lostcodestudios.fools.Revenge;
import com.lostcodestudios.fools.screens.MenuEntry.EntryCallback;

public class PauseMenuScreen extends MenuScreen {

	private GameplayScreen gameplayScreen;
	
	public PauseMenuScreen(Revenge revenge, GameplayScreen gameplayScreen) {
		super(revenge, "Paused");
		
		this.gameplayScreen = gameplayScreen;
		
		OrderedMap<String, EntryCallback> entries = new OrderedMap<String, EntryCallback>();
		
		entries.put("Resume", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				((PauseMenuScreen) menu).resumeGame();
			}
			
		});
		
		entries.put("Options", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				revenge.getScreen().dispose();
				// TODO create an options menu
			}
			
		});
		
		entries.put("Return to Menu", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				((PauseMenuScreen)menu).gameplayScreen.dispose();
				revenge.getScreen().dispose();
				revenge.setScreen(new MainMenuScreen(revenge));
			}
			
		});
		
		addList(entries, new Vector2(400, 400));
	}
	
	private void resumeGame() {
		revenge.getScreen().dispose();
		
		revenge.setScreen(gameplayScreen);
	}

	@Override
	public void render(float delta) {
		if (input.wasKeyPressed(Keys.ESCAPE)) {
			resumeGame();
			return;
		}
		
		super.render(delta);
	}
	
	
	
}
