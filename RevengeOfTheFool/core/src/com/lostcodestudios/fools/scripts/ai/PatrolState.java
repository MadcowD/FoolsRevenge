package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class PatrolState extends PathState{

	private boolean running = false;
	
	public PatrolState(float speed, boolean loop) {
		super("Patrol", speed, loop, null);
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		if(world.flags.getFlag(0, 0) > 0) {
			running = true;
			super.run(world, args);
		}
	}

	@Override
	public void onSight(Entity self, Entity e) {
		super.onSight(self, e);
		
		if(running) {
			//saw the player! give chase
			parent.setState(new ChaseState(self.getPosition(), (Human) e,  6.7f, this));
		}
	}	

}
