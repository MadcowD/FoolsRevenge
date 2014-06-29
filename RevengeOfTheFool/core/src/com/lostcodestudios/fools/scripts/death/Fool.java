package com.lostcodestudios.fools.scripts.death;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Fool extends Script {

	static {
		Script.Register(new Fool());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		
		world.flags.setFlag(0, 1, 1);
		
		world.dialog.showDialogWindow("The Fool collapsed from his wounds and lay watching his blood trickle forth.");
		
		if (world.flags.getFlag(0, 2) == 1) {
			world.dialog.showDialogWindow("As he drifted away, he felt satisfaction knowing he had taken the King down with him.");
		} else {
			world.dialog.showDialogWindow("No... this couldn't be happening, he thought to himself. The King still lived. Had it all been... for nothing?");
		}
		
		world.scripts.runScript("End");
	}

}
