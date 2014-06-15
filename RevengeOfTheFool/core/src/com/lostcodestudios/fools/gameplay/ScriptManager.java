package com.lostcodestudios.fools.gameplay;


import java.util.NoSuchElementException;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.scripts.Script;

public class ScriptManager {
	private GameWorld world;
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
		else
			throw new NoSuchElementException();
	}
	
	/**
	 * Runs a script without any arguments.
	 * @param name The name of the script to run.
	 */
	public void runScript(String name) {
		runScript(name, null);
	}


	/**
	 * The collection of cached scripts.
	 */
	public static ObjectMap<String, Script> Scripts = new ObjectMap<String, Script>();
}
