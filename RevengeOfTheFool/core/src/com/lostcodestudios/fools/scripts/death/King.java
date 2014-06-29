package com.lostcodestudios.fools.scripts.death;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.Item;
import com.lostcodestudios.fools.scripts.Script;

public class King extends Script {

	static {
		Script.Register(new King());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		// handle the King's death
		Entity e = world.specialEntities.get("King");
		
		Item crown = new Item(world, e.getPosition(), "Crown");
		crown.takeScript = "items.Crown";
		
		world.entities.add(crown);
		
		world.dialog.showDialogWindow("Unarmed, the King fell quite easily. The Fool bore a wide grin as he pulled his knife from the old man's stomach.");
		world.dialog.showDialogWindow("With guards all over the castle, the Fool knew he had to escape through the front gate.");
		
		world.flags.setFlag(0, 2, 1);
	}

}
