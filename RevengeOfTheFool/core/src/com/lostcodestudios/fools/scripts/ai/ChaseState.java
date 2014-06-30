package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class ChaseState extends State{

	private Human tEnt;
	int lostSight = 0;
	private float speed;

	public ChaseState(Vector2 initial, Human target, 
		float speed, State nextState) {
		super(nextState);
		this.speed = speed;
		//super(initial, target.getPosition(), radius, speed, nextState);

		this.tEnt = target;
	}

	@Override
	public void run(GameWorld world, final ObjectMap<String, Object> args) {

		Human e = (Human)args.get("e");
		Vector2 dir = tEnt.getPosition().cpy().add(tEnt.body.getLinearVelocity().cpy().nor().scl(0.2f)).sub(e.getPosition().cpy());
		if(dir.len2()<0.1){
			e.setVelocity(new Vector2());
			this.end();
		}
		else
			e.setVelocity(dir.nor().scl(speed));
		
		


		// TODO Auto-generated method stub
		//super.run(world, args);
	}

	@Override
	public void sightLost(Entity self, Entity e) {
		super.sightLost(self, e);

		// lost sight. Go to next state
		this.end();
	}

}
