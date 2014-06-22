package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.BinaryHeap.Node;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class MovementState extends State{

	private Vector2 target;
	private Integer mapWidth;
	private Integer mapHeight;
	private IntArray path;
	private float speed;
	int pathIndex = 0;
	
	

	public MovementState (Vector2 initial, Vector2 target, float speed, State nextState) {
		super(nextState);
		this.target = target;
		
		this.mapWidth = GameWorld.ASTARWORLD;
		this.mapHeight =GameWorld.ASTARWORLD;
		path = calculatePath(initial);
		this.speed = speed;
		pathIndex = path.size-2;
	}

	/**
	 * MOVES THE ENTITIY USING A*
	 */
	@Override
	public void run (GameWorld world, ObjectMap<String, Object> args) {
		Human e = (Human)args.get("e");
		if(pathIndex <0){
			e.setVelocity(new Vector2());
			this.end();
			return;
		}
		
		// Actually move to the calculated path.

		
		Vector2 nextPos = new Vector2(path.get(pathIndex)*GameWorld.ASTARRECIP, path.get(pathIndex+1)*GameWorld.ASTARRECIP);
		Vector2 dir = nextPos.cpy().sub(e.getPosition().cpy());
		if(dir.len2() < 0.005){
			pathIndex-=2;
		}
		dir.nor();
		dir.scl(speed);

		e.setVelocity(dir);
	}
	
	public IntArray calculatePath(Vector2 initial){
		AStar pathFinder = new AStar(mapWidth, mapHeight);
		IntArray calculated = pathFinder.getPath((int)(GameWorld.ASTARSIZE*initial.x), (int)(GameWorld.ASTARSIZE*initial.y), (int)(GameWorld.ASTARSIZE*target.x), (int)(GameWorld.ASTARSIZE*target.y));
		
		
		return calculated;
	}



}
