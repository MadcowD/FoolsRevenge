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
		world.dialog.showDialogFull("His booming voice sent tremors down the Fool's spine.");
		
		world.scripts.delayScript("com.lostcodestudios.fools.scripts.intro.Dialog2", 1.5f);
	}

}
