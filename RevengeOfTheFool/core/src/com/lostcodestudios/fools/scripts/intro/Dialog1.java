package com.lostcodestudios.fools.scripts.intro;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Dialog1 extends Script {

	static {
		Script.Register(new Dialog1());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.dialog.showDialogFull("The King's rage shook the castle like thunder.");
		world.cutsceneMode = true;
		
		world.scripts.delayScript("intro.Dialog2", 0.5f);
	}

}
