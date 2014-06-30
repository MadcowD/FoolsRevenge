package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
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
	boolean backup;
	int backuptick = 0;


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
		Vector2 pos = e.getPosition().cpy();
		Vector2 dir = tEnt.getPosition().cpy().add(tEnt.body.getLinearVelocity().cpy().nor().scl(0.2f)).sub(pos);

		
		if(dir.len2()<0.1){
			e.setVelocity(new Vector2());
			this.end();
		}
		else{
			//THIS BACKS SHIT UP
			if(backup){
				if(++backuptick >= 2e00){
					backup = false;
					backuptick = 0;
				}
				else
					e.setVelocity(new Vector2());
			}
			//IF NOT BACKING UP
			else
				e.setVelocity(dir.cpy().nor().scl(speed));
		}


		if(AStar.check(pos.x, pos.y) && AStar.check(tEnt.getPosition().x, tEnt.getPosition().y)){
			//Perform a ray cast and check if there are objects in between. If we're under a distance 
			//Then if there is invalidity in the path on the part of the initial position back up,
			PathCast pc = new PathCast(this, pos);
			world.world.rayCast(pc, pos, tEnt.getPosition());
			if(pc.fixtureCount > 4 || dir.len2() > 400 ){
				this.end();
				e.setVelocity(new Vector2());
			}
		}





		// TODO Auto-generated method stub
		//super.run(world, args);
	}

	@Override
	public void sightLost(Entity self, Entity e) {
		super.sightLost(self, e);

		// lost sight. Go to next state
		this.end();
	}

	/** performs a special raycast type **/
	private class PathCast implements RayCastCallback{
		private ChaseState singleton;
		private Vector2 initial;
		public PathCast(ChaseState singleton, Vector2 initial){
			this.singleton = singleton;
			this.initial = initial;
		}
		public int fixtureCount = 0;
		@Override
		public float reportRayFixture (Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			if(fixture.getBody().getType().equals(BodyType.StaticBody)){
				fixtureCount++;

				//On the fist encounter of a fixture
				if(fixtureCount == 1){
					if(!AStar.check(initial.x, initial.y))
						singleton.backup = true;
					else if(AStar.check(singleton.tEnt.getPosition().x, singleton.tEnt.getPosition().y)){
						singleton
						.parent.setState(
							new MovementState(
								initial.cpy(),
								singleton.tEnt.getPosition().cpy(),
								1, singleton.speed, singleton){
								@Override
								public void run (GameWorld world, ObjectMap<String, Object> args) {
									if(!AStar.check(singleton.tEnt.getPosition().x, singleton.tEnt.getPosition().y)
										||  !AStar.check(initial.x, initial.y))
										super.end();
									// TODO Auto-generated method stub
									super.run(world, args);
								}
							});
					}
				}
			}
			return -1;
		}

	}
}
