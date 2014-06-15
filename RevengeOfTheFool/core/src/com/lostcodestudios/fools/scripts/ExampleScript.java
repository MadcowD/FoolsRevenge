package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class ExampleScript extends Script {
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		//RUN CODE IN HERE. 	
		Human fool = (Human)args.get("fool");
		fool.delete();

	}
	
	//Registers the script with the script manager.
	public static ExampleScript Script = new ExampleScript();

}
