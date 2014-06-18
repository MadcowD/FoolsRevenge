package com.lostcodestudios.fools.gameplay;


import java.util.Iterator;
import java.util.NoSuchElementException;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.lostcodestudios.fools.scripts.Script;

public class ScriptManager {
	
	private class ScriptDelayInfo {
		public float delay;
		public ObjectMap<String, Object> scriptArgs;
		
		public ScriptDelayInfo(float delay, ObjectMap<String, Object> args) {
			this.delay = delay;
			this.scriptArgs = args;
		}
	}
	
	private GameWorld world;
	
	/**
	 * The collection of cached scripts.
	 */
	public final static ObjectMap<String, Script> Scripts = new ObjectMap<String, Script>();
	
	private ObjectMap<String, ScriptDelayInfo> delayedScripts = new ObjectMap<String, ScriptDelayInfo>();
	
	/**
	 * Initializes a script manager.
	 * @param world The world in which the script manager resides.
	 */
	public ScriptManager(GameWorld world) {
		this.world = world;
	}
	
	/**
	 * Runs a given script (given by class name)
	 * @param name The class name of the script to run.
	 * @param args The arguments to pass to the script.
	 */
	public void runScript(String name, ObjectMap<String, Object> args) {
		if(Scripts.containsKey(name))
			Scripts.get(name).run(world, args);
		else{
			
			try {
				Class.forName(name);
			} catch (ClassNotFoundException e) {
				throw new NoSuchElementException();
			}
			
			runScript(name, args);
		}
	}
	
	/**
	 * Runs a script without any arguments.
	 * @param name The name of the script to run.
	 */
	public void runScript(String name) {
		runScript(name, null);
	}
	
	public void delayScript(String name, float delay, ObjectMap<String, Object> args) {
		delayedScripts.put(name, new ScriptDelayInfo(delay, args));
	}
	
	public void delayScript(String name, float delay) {
		delayScript(name, delay, null);
	}
	
	public void update(float deltaSeconds) {
		Iterator<Entry<String, ScriptDelayInfo>> it = delayedScripts.iterator();
		
		while (it.hasNext()) {
			Entry<String, ScriptDelayInfo> entry = it.next();
			
			entry.value.delay -= deltaSeconds;
			
			if (entry.value.delay <= 0) {
				// run the script
				
				runScript(entry.key, entry.value.scriptArgs);
				
				it.remove(); // remove it from the queue
			}
		}
	}
	
}
