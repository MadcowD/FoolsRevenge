package com.lostcodestudios.fools.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.OrderedMap;
import com.lostcodestudios.fools.Revenge;
import com.lostcodestudios.fools.screens.MenuEntry.EntryCallback;

public class CreditScreen extends MenuScreen {

	public CreditScreen(Revenge revenge) {
		super(revenge, "Credits");
		
		OrderedMap<String, EntryCallback> entries = new OrderedMap<String, EntryCallback>();
		
		entries.put("Written and designed by Nathaniel Nelson", null);
		entries.put("Programmed by William Guss and Nathaniel Nelson", null);
		entries.put("Music and sound effects by Mark Sparling", null);
		entries.put("Art by Nathaniel Nelson, based on sprites from oryxdesignlab.com", null);
		
		addList(entries, new Vector2(400, 400));
		
		addEntry("Back", new EntryCallback() {

			@Override
			public void invoke(Revenge revenge, MenuScreen menu, MenuEntry entry) {
				revenge.getScreen().dispose();
				revenge.setScreen(new MainMenuScreen(revenge));
			}
			
		}, new Vector2(550, 50));
	}

}
