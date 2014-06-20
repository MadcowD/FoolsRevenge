package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Start extends Script {
	
	static{
		Script.Register(new Start());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.camera.position.set(50f * 64f, 94f * 64f, 0f);
		world.camera.update();
		
		world.scripts.runScript("com.lostcodestudios.fools.scripts.intro.Dialog1");
	}
	
}
