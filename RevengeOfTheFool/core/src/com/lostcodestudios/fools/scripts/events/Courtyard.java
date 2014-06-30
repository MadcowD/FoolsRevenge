package com.lostcodestudios.fools.scripts.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Courtyard extends Script {

	static {
		Script.Register(new Courtyard());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if (world.flags.getFlag(2, 0) == 0) {
			world.flags.setFlag(2, 0, 1);
			
			world.dialog.showDialogWindow("The courtyard was bathed in moonlight.");
		}
	}

}
