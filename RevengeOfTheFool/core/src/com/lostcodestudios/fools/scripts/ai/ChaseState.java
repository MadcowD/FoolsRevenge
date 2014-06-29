package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class ChaseState extends MovementState {

	private Entity tEnt;
	int lostSight = 0;

	public ChaseState(Vector2 initial, Entity target, float radius,
		float speed, State nextState) {
		super(initial, target.getPosition(), radius, speed, nextState);

		this.tEnt = target;
	}

	@Override
	public void run(GameWorld world, final ObjectMap<String, Object> args) {
		if(tEnt.getPosition().dst2(this.target) > 10){
			initialize(((Human)args.get("e")).getPosition(), tEnt.getPosition(), 1, this.speed);
			if(astar){
				this.path.removeIndex(path.size-1);
				this.path.removeIndex(path.size-1);
				this.path.removeIndex(path.size-1);
				this.path.removeIndex(path.size-1);
				this.path.removeIndex(path.size-1);
				this.path.removeIndex(path.size-1);
			}

			

				


			}
		
		
		
		// TODO Auto-generated method stub
		super.run(world, args);
		

		



	}

}
