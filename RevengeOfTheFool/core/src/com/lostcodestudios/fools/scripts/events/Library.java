package com.lostcodestudios.fools.scripts.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Library extends Script {

	static {
		Script.Register(new Library());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if (world.flags.getFlag(2, 1) == 0) {
			world.flags.setFlag(2, 1, 1);
			
			world.dialog.showDialogWindow("The Fool had no pressing desire to read books.");
		}
	}

}
