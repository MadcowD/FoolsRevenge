package com.lostcodestudios.fools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {

	private Array<Integer> pressedKeys = new Array<Integer>();
	private Array<Integer> releasedKeys = new Array<Integer>();
	private Array<Integer> heldKeys = new Array<Integer>();
	
	private boolean mouseClicked;
	
	public void beginNewFrame() {
		pressedKeys.clear();
		releasedKeys.clear();
		mouseClicked = false;
	}
	
	public boolean wasKeyPressed(int keycode) {
		return pressedKeys.contains(keycode, false);
	}
	
	public boolean wasKeyReleased(int keycode) {
		return releasedKeys.contains(keycode, false);
	}
	
	public boolean isKeyHeld(int keycode) {
		return heldKeys.contains(keycode, false);
	}
	
	public boolean containsMouse(Rectangle bounds) {
		return bounds.contains(new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()));
	}
	
	public boolean wasMouseClicked() {
		return mouseClicked;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		pressedKeys.add(keycode);
		heldKeys.add(keycode);
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		releasedKeys.add(keycode);
		heldKeys.removeValue(keycode, false);
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		//TODO handle this
		
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mouseClicked = true;
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO handle this
		
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO handle this
		
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO handle this
		
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		//TODO handle this
		
		return true;
	}

}
