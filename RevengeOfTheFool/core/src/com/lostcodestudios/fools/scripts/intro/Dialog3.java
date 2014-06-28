package com.lostcodestudios.fools.scripts.intro;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Dialog3 extends Script {
	static{
		Script.Register(new Dialog3());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.dialog.showVoiceBubble("You cannot stop me, my lord! I will be back!", world.specialEntities.get("Fool"), 5f);
		
		world.flags.setFlag(1, 0, 2);
		
		world.scripts.delayScript("intro.Dialog4", 2f);
	}

}
