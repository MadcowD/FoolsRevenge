package com.lostcodestudios.fools.scripts.intro;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Dialog2 extends Script {

	static {
		Script.Register(new Dialog2());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.dialog.showVoiceBubble("This is treasonous! Take this Fool to the dungeon!", world.specialEntities.get("King"), 5f);
	}

}
