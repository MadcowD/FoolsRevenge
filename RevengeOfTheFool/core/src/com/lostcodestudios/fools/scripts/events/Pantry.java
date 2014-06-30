package com.lostcodestudios.fools.scripts.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Pantry extends Script {

	static {
		Script.Register(new Pantry());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if (world.flags.getFlag(2, 3) == 0) {
			world.flags.setFlag(2, 3, 1);
			
			world.dialog.showDialogWindow("The pantry lay unguarded. The Fool was not hungry.");
		}
	}

}
