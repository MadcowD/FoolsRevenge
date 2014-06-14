package com.lostcodestudios.fools.gameplay;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ScriptManager {
	
	private GameWorld world;
	
	public ScriptManager(GameWorld world) {
		this.world = world;
	}
	
	public void runScript(String path, ObjectMap<String, Object> args) {
		Binding binding = new Binding();
		binding.setVariable("world", world); //all scripts have public access to the world
		
		Iterator<Entry<String, Object>> it = args.iterator();
		
		while (it.hasNext()) {
			Entry<String, Object> arg = it.next();
			binding.setVariable(arg.key, arg.value);
		}
		
		GroovyShell shell = new GroovyShell(binding);
		shell.evaluate(Gdx.files.internal(path).reader());
	}
	
	public void runScript(String path) {
		runScript(path, new ObjectMap<String, Object>());
	}
	
}
