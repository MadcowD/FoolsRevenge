package com.lostcodestudios.fools;

import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Revenge extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	GroovyShell shell;
	Binding binding;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		binding = new Binding();		
		binding.setVariable("batch", batch);
		binding.setVariable("img", img);
		
		shell = new GroovyShell(binding);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		try {
			shell.evaluate(Gdx.files.internal("scripts/HelloWorld.groovy").reader());
		} catch (CompilationFailedException e) {
			e.printStackTrace();
		}
	}
}
