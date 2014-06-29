package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class SkipScene extends Script {

	static {
		Script.Register(new SkipScene());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.cutsceneMode = false;
	}

}
