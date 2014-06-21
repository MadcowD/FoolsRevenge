package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.scripts.Script;

public class PathState extends Script {
	protected String pathType;
	private int elapsedLocationPause = 0;
	boolean moving = false;
	boolean loop;
	int targetIndex = -2;
	Vector2 target;
	float speed;

	public PathState(String pathType, float speed, boolean loop){
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
					Vector2 dir = target.cpy().sub(e.getPosition().cpy());
					if(dir.len2() < 0.05){
						moving = false;
						e.setVelocity(new Vector2());
						return;
					}
					dir.nor();
					dir.scl(speed);

					e.setVelocity(dir);
					
					
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
					}
				}
			}
		}
	}

}
