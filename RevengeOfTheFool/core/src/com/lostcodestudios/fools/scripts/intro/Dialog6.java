package com.lostcodestudios.fools.scripts.intro;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.scripts.Script;

public class Dialog6 extends Script {

	static {
		Script.Register(new Dialog6());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		Human fool = (Human) world.specialEntities.get("Fool");
		
		fool.body.setLinearVelocity(new Vector2());
		
		world.dialog.showDialogWindow("The guards shoved the Fool forward, onto his knees.");
	}

}
