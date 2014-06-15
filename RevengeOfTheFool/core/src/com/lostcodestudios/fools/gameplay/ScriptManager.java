package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ScriptManager {
	
	private ObjectMap<String, String> loadedScripts = new ObjectMap<String, String>();
	
	private GroovyShell shell;
	private ObjectMap<String, Object> args = new ObjectMap<String, Object>();
	
	public ScriptManager(GameWorld world) {
		Binding binding = new Binding();
		binding.setVariable("world", world);
		binding.setVariable("args", args);
		
		shell = new GroovyShell();
	}
	
	public String getScriptBody(String path) {
		if (!loadedScripts.containsKey(path)) {
			// load the script into memory if it hasn't been used before
			loadedScripts.put(path, Gdx.files.internal(path).readString());
		}
		
		return loadedScripts.get(path); // return the body of the desired script from memory
	}
	
	public void runScript(String scriptBody, ObjectMap<String, Object> args) {
		this.args.clear();
		
		this.args.putAll(args);
		
//		shell.evaluate(scriptBody);
	}
	
	public void runScript(String scriptBody) {
		this.args.clear();
		
//		shell.evaluate(scriptBody);
	}
	
}
