package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Door;

public class Switch extends Script {

	static {
		Script.Register(new Switch());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		Door d = (Door)world.specialEntities.get("KingDoor");
		d.toggleOpen();
	}

}
