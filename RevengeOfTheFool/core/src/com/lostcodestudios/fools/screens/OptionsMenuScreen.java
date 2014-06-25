package com.lostcodestudios.fools.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.OrderedMap;
import com.lostcodestudios.fools.Revenge;
import com.lostcodestudios.fools.SoundManager;
import com.lostcodestudios.fools.screens.MenuEntry.EntryCallback;

public class OptionsMenuScreen extends MenuScreen {

	private static float VOLUME_INC = 0.25f;
	private static final String[] VOLUME_STRINGS = new String[] {
		"Off", // 0f
		"Low", // 0.25f
		"Medium", // 0.5f
		"High", // 0.75f
		"Full" // 1f
	};
	
	private MenuEntry soundEntry;
	private MenuEntry musicEntry;
	private MenuScreen previousScreen;
	
	public OptionsMenuScreen(Revenge revenge, MenuScreen previousScreen) {
		super(revenge, "Options");
		
		this.previousScreen = previousScreen;
		
		OrderedMap<String, EntryCallback> entries = new OrderedMap<String, EntryCallback>();
		
		entries.put("Sound", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				OptionsMenuScreen options = (OptionsMenuScreen) menu;
				
				float newVolume = (SoundManager.getSoundVolume() + VOLUME_INC) % 1.25f;
				
				SoundManager.setSoundVolume(newVolume);
				
				options.updateSoundEntry();
			}
			
		});
		
		entries.put("Music", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				OptionsMenuScreen options = (OptionsMenuScreen) menu;
				
				float newVolume = (SoundManager.getMusicVolume() + VOLUME_INC) % 1.25f;
				
				SoundManager.setMusicVolume(newVolume);
				
				options.updateMusicEntry();
			}
			
		});
		
		addList(entries, new Vector2(400, 400));
		
		soundEntry = getEntry(0);
		musicEntry = getEntry(1);
		
		updateSoundEntry();
		updateMusicEntry();
		
		addEntry("Back", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				revenge.getScreen().dispose();
				
				revenge.setScreen(((OptionsMenuScreen)menu).previousScreen);
			}
			
		}, new Vector2(550, 50));
	}
	
	public void updateSoundEntry() {
		soundEntry.setText("Sound Volume: " + volumeString(SoundManager.getSoundVolume()));
	}
	
	public void updateMusicEntry() {
		musicEntry.setText("Music Volume: " + volumeString(SoundManager.getMusicVolume()));
	}
	
	private String volumeString(float volume) {
		int index = (int)(volume / VOLUME_INC);
		return VOLUME_STRINGS[index];
	}

}
