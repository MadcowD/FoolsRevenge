package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class PathState extends State {
	protected String pathType;
	private int elapsedLocationPause = 0;
	private boolean moving = false;
	private boolean loop;
	private int targetIndex = -2;
	private Vector2 target;
	private float speed;

	public PathState(String pathType, float speed, boolean loop, State nextState){
		super(nextState);
		this.pathType = pathType;
		this.loop = loop;
		this.speed = speed;
	}

	/**
	 * Follows the path of path type given above.
	 */
	@Override
	public void run (GameWorld world, ObjectMap<String, Object> args) {
 		if(args.containsKey("paths")){

			@SuppressWarnings("unchecked")
			ObjectMap<String, Object> paths = (ObjectMap<String, Object>)args.get("paths");
			if(paths.containsKey(pathType)){
				PolylineMapObject plmp = (PolylineMapObject)paths.get(pathType);

				if(moving){
					Human e = (Human)args.get("e");

					parent.setState(new MovementState(e.getPosition().cpy(), target.cpy(),  1f, speed, this));
					moving = false;
					elapsedLocationPause = 0;
				}
				else{
					elapsedLocationPause++;
					if(Integer.parseInt((String)plmp.getProperties().get("pause")) < elapsedLocationPause){
						elapsedLocationPause = 0;
						if(targetIndex+2 < plmp.getPolyline().getVertices().length){
							moving = true;
							targetIndex+=2;
							//TODO: CONVERT
							float x =  plmp.getPolyline().getTransformedVertices()[targetIndex];
							float y =plmp.getPolyline().getTransformedVertices()[targetIndex+1];
							target = new Vector2(x,y);
						}
						else if(loop)
							targetIndex = -2;
						else {
							parent.setState(nextState);
							end();
						}
					}
				}
			}
		}
	}

}
