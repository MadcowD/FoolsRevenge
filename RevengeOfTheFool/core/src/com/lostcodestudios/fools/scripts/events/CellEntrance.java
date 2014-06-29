package com.lostcodestudios.fools.scripts.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.SoundManager;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.scripts.Script;

public class CellEntrance extends Script {

	static {
		Script.Register(new CellEntrance());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if (world.flags.getFlag(1, 0) == 5) {
			world.flags.setFlag(1, 1, 1);
			
			Human fool = (Human)world.specialEntities.get("Fool");
			
			if (!fool.hasItem("Gold Key")) {
				SoundManager.playSound("snd_metal_door");
				
				world.dialog.showDialogFull("The cell door swung closed. The Fool never saw the light of day again.");
		
				world.flags.setFlag(0,1,1); // "death"
				
				world.scripts.runScript("End");
			}
		}
	}

}
