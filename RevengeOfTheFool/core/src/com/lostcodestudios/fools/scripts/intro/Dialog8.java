package com.lostcodestudios.fools.scripts.intro;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Dialog8 extends Script {

	static {
		Script.Register(new Dialog8());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.dialog.showDialogWindow("The Fool found his feet.");
		world.cutsceneMode = false;
	}

}
