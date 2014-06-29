package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Entity;

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

	@Override
	public void onSight(Entity self, Entity e) {
		super.onSight(self, e);
		
		//saw the player! give chase
		parent.setState(new ChaseState(self.getPosition(), e, 0, 5f, this));
	}	

}
