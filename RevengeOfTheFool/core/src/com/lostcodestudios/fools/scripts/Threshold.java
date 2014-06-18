package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class Threshold extends Script {

	static {
		Script.Register(new Threshold());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		Human e = (Human) args.get("e");
		
		if (e == world.specialEntities.get("Fool")) {
		
			if (world.flags.getFlag(0, 0) == 0) {
				world.dialog.showDialogWindow("The Fool knew he would be back.");
				world.flags.setFlag(0, 0, 1);
			}
			
		}
	}

}
