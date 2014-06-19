package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Start extends Script {
	
	static{
		Script.Register(new Start());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.scripts.runScript("com.lostcodestudios.fools.scripts.intro.Dialog1");
	}
	
}