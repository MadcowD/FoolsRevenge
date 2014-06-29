package com.lostcodestudios.fools.scripts.items;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Crown extends Script {

	static {
		Script.Register(new Crown());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.dialog.showDialogWindow("The Fool took a trophy from the old man's corpse.");
		
		world.flags.setFlag(0, 3, 1);
	}

}
