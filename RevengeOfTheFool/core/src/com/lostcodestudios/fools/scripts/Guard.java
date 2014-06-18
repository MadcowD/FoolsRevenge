package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class Guard extends Script {
	
	private static final float SPEED = 4f;
	
	static {
		Script.Register(new Guard());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		Human fool = (Human) world.specialEntities.get("Fool");
		
		Human guard = (Human) args.get("e");
		
		Vector2 dir = fool.getPosition().cpy().sub(guard.getPosition().cpy());
		dir.nor();
		
		dir.scl(SPEED);
		
		guard.setVelocity(dir);
	}

}
