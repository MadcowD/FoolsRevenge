package com.lostcodestudios.fools.scripts.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Kitchen extends Script {

	static {
		Script.Register(new Kitchen());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if (world.flags.getFlag(2, 4) == 0) {
			world.flags.setFlag(2, 4, 1);
			
			world.dialog.showDialogWindow("The kitchen, abandoned for the night.");
		}
	}

}
