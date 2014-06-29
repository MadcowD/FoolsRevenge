package com.lostcodestudios.fools.scripts.death;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class Guard extends Script {

	static {
		Script.Register(new Guard());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		world.flags.incFlag(0, 4);
	}

}
