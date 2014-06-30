package com.lostcodestudios.fools.scripts.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class GreatHall extends Script {

	static {
		Script.Register(new GreatHall());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if (world.flags.getFlag(2, 5) == 0) {
			world.flags.setFlag(2, 5, 1);
			
			world.dialog.showDialogWindow("The expansive Great Hall, with its dais to the west overlooking all.");
		}
	}

}
