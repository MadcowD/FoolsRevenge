package com.lostcodestudios.fools.scripts.intro;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Dialog7 extends Script {

	static {
		Script.Register(new Dialog7());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.dialog.showVoiceBubble("Make any moves, or try to get away, and we'll kill you.", world.specialEntities.get("s1"), 5f);
		
		world.scripts.delayScript("intro.Dialog8", 5.5f);
	}

}
