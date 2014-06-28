package com.lostcodestudios.fools.scripts.intro;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Dialog4 extends Script {
	
	static {
		Script.Register(new Dialog4());
	}

	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.dialog.showDialogWindow("The Fool cackled as he was dragged limply to the door.");
	}
	
	
	
}
