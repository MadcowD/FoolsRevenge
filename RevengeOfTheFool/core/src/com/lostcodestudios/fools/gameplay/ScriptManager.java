package com.lostcodestudios.fools.gameplay;


import java.util.Iterator;
import java.util.NoSuchElementException;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.lostcodestudios.fools.scripts.Script;

public class ScriptManager {
	
	private Array<String> deathScriptsToRun = new Array<String>();
	
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
	 * NOTE: class name is now relative to the package "com.lostcodestudios.fools.scripts"
	 * @param name The class name of the script to run, relative to the package "com.lostcodestudios.fools.scripts"
	 * @param args The arguments to pass to the script.
	 */
	public void runScript(String name, ObjectMap<String, Object> args) {
		String fullName = "com.lostcodestudios.fools.scripts." + name;
		
		if(Scripts.containsKey(fullName))
			Scripts.get(fullName).run(world, args);
		else{
			
			try {
				Class.forName(fullName);
			} catch (ClassNotFoundException e) {
				throw new NoSuchElementException();
			}
			
			runScript(name, args);
		}
	}
	
	public void runDeathScript(String name) {
		deathScriptsToRun.add(name);
	}
	
	public void runDeathScripts() {
		if (deathScriptsToRun.size > 0) {
			// first run the King's script so the others know the King died
			Iterator<String> it = deathScriptsToRun.iterator();
			
			while (it.hasNext()) {
				String script = it.next();
				
				if (script.equals("death.King")) {
					runScript(script);
					it.remove();
				}
			}
			
			// run the rest of them
			it = deathScriptsToRun.iterator();
			
			while (it.hasNext()) {
				String script = it.next();
				
				runScript(script);
				it.remove();
			}
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
