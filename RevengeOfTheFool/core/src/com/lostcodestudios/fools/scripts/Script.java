package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.ScriptManager;

public abstract class Script {
	public Script(){
		ScriptManager.Scripts.put(this.getClass().getSimpleName(), this);
	}
	
	public abstract void run(GameWorld world, ObjectMap<String, Object> args);
}
