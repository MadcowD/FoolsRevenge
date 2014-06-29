package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class PatrolState extends PathState{

	public PatrolState(float speed, boolean loop) {
		super("Patrol", speed, loop, null);
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if(world.flags.getFlag(0, 0) == 1) {
			super.run(world, args);
		}
	}

}
