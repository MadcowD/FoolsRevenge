package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Start extends Script {

	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		//world.dialog.showDialogFull("The King's rage shook the castle like thunder.");
		//world.dialog.showDialogWindow("His booming voice sent tremors down the Fool's spine.");

		world.dialog.showVoiceBubble("This is treasonous! Take this Fool to the dungeon!", world.specialEntities.get("Fool"), 5);
	}
	
	static{
		Script.Register(new Start());
	}
}
