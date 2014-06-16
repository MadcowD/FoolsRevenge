package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.ScriptManager;

public abstract class Script {
	
	public abstract void run(GameWorld world, ObjectMap<String, Object> args);
	
	public final static <T extends Script> void Register(T script){
		ScriptManager.Scripts.put(script.getClass().getName(), script);
	}
}
