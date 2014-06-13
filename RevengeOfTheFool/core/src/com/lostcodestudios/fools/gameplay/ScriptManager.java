package com.lostcodestudios.fools.gameplay;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ScriptManager {
	
	private EntityWorld world;
	
	public ScriptManager(EntityWorld world) {
		this.world = world;
	}
	
	public void runScript(String path, Map<String, Object> args) {
		Binding binding = new Binding();
		binding.setVariable("world", world); //all scripts have public access to the world
		
		for (Entry<String, Object> arg : args.entrySet()) {
			binding.setVariable(arg.getKey(), arg.getValue());
		}
		
		GroovyShell shell = new GroovyShell();
		shell.evaluate(Gdx.files.internal(path).reader());
	}
	
	public void runSCript(String path) {
		runScript(path, new HashMap<String, Object>());
	}
	
}
