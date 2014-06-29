package com.lostcodestudios.fools.scripts.items;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Key extends Script {

	static {
		Script.Register(new Key());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if (world.flags.getFlag(0, 5) == 0) {
			world.dialog.showDialogWindow("The Fool deftly stole the keys from the pocket of one of his guards.");
		}
		
		world.flags.incFlag(0, 5);
		
		if (world.flags.getFlag(0, 5) == 2) {
			world.flags.setFlag(0, 0, 1); // guards are angry
		}
	}

}
