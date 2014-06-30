package com.lostcodestudios.fools.scripts.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Chapel extends Script {

	static {
		Script.Register(new Chapel());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if (world.flags.getFlag(2, 2) == 0) {
			world.flags.setFlag(2, 2, 1);
			
			world.dialog.showDialogWindow("The chapel was dark and empty.");
		}
	}

}
