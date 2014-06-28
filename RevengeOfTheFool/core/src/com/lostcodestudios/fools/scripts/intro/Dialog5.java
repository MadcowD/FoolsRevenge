package com.lostcodestudios.fools.scripts.intro;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.scripts.Script;

public class Dialog5 extends Script {

	static {
		Script.Register(new Dialog5());
	}

	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		Human fool = (Human) world.specialEntities.get("Fool");
		
		fool.body.setLinearVelocity(new Vector2(0, -7f));
		
		world.scripts.delayScript("intro.Dialog6", 0.4f);
	}
	
}
