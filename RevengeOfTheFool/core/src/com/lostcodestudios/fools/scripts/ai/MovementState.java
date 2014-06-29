package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class MovementState extends State{

	public Vector2 target;
	private Integer mapWidth;
	private Integer mapHeight;
	public IntArray path;
	private float speed;
	int pathIndex = 0;
	private float radius;
	
	

	public MovementState (Vector2 initial, Vector2 target, float radius, float speed, State nextState) {
		super(nextState);
		this.target = target;
		
		this.mapWidth = GameWorld.ASTARWORLD;
		this.mapHeight =GameWorld.ASTARWORLD;
		path = calculatePath(initial);
		this.speed = speed;
		this.radius = radius;
		pathIndex = path.size-2;
	}

	/**
	 * MOVES THE ENTITIY USING A*
	 */
	@Override
	public void run (GameWorld world, ObjectMap<String, Object> args) {
		final Human e = (Human)args.get("e");
		if(pathIndex <0){
			e.setVelocity(new Vector2());
			this.end();
			return;
		}
		
		// Actually move to the calculated path.

		
		Vector2 nextPos = new Vector2(path.get(pathIndex)*GameWorld.ASTARRECIP, path.get(pathIndex+1)*GameWorld.ASTARRECIP);
		Vector2 dir = nextPos.cpy().sub(e.getPosition().cpy());
		
		
		//STOP IF REALLY CLOSE LOL.
		if(pathIndex == 1 && dir.len2() < 0.005 || dir.len2() < 0.5){
			pathIndex-=2;
		}
		
		dir.nor();
		
		
		//Perform steering.
//		final Vector2 steering = new Vector2();
//		for(double theta = 0; theta <2*Math.PI; theta+=0.02222*Math.PI)
//			world.world.rayCast(new RayCastCallback(){
//				@Override
//				public float reportRayFixture (Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
//					if(!AStar.check(point.x, point.y))
//						steering.add(new Vector2().sub(point.sub(e.getPosition().cpy())));
//					return 0;
//				}
//				
//			},
//			e.getPosition().cpy(), 
//				// CASTS THE VECTOR RADIALLY OUTWARDS
//				new Vector2((float)Math.cos(theta),(float)Math.sin(theta))
//					.scl(radius)
//					.add(e.getPosition().cpy()));
//		
//		steering.nor();
//		dir.add(steering.scl(0.9f));

		dir.nor();
		dir.scl(speed);

		e.setVelocity(dir);
	}

	public IntArray calculatePath(Vector2 initial){
		AStar pathFinder = new AStar(mapWidth, mapHeight);
		IntArray calculated = pathFinder.getPath((int)(GameWorld.ASTARSIZE*initial.x), (int)(GameWorld.ASTARSIZE*initial.y), (int)(GameWorld.ASTARSIZE*target.x), (int)(GameWorld.ASTARSIZE*target.y));
		
		//PROCESS THE LIST AND REMOVE USELESS SHIT SUCH AS STRAIGHAWAYS
//		Vector2 lastSlope = new Vector2();
//		for(int i = 0; i < calculated.size-2; i+=2){
//			Vector2 slope = new Vector2(calculated.get(i+2)-calculated.get(i), calculated.get(i+3) -calculated.get(i+1));
//			if(slope.equals(lastSlope)){
//				calculated.removeIndex(i);
//				calculated.removeIndex(i);
//				i-=2;
//			}
//			lastSlope = slope;	
//		}
		return calculated;
	}



}
