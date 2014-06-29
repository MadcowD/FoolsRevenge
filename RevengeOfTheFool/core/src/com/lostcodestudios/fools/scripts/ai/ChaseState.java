package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class ChaseState extends MovementState {

	private Entity tEnt;

	public ChaseState(Vector2 initial, Entity target, float radius,
			float speed, State nextState) {
		super(initial, target.getPosition(), radius, speed, nextState);
		
		this.tEnt = target;
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		
		this.target = tEnt.getPosition();
		this.path = this.calculatePath(((Human)args.get("e")).getPosition());
		// TODO Auto-generated method stub
		super.run(world, args);
	}
	
	@Override
	public void sightLost(Entity self, Entity e) {
		super.sightLost(self, e);
		
		// lost sight. Go to next state
		parent.setState(nextState);
	}

}
