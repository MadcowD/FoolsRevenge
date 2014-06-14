package com.lostcodestudios.fools;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {

	private Array<Integer> pressedKeys = new Array<Integer>();
	private Array<Integer> heldKeys = new Array<Integer>();
	
	public void beginNewFrame() {
		pressedKeys.clear();
	}
	
	public boolean wasKeyPressed(int keycode) {
		return pressedKeys.contains(keycode, false);
	}
	
	public boolean isKeyHeld(int keycode) {
		return heldKeys.contains(keycode, false);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		pressedKeys.add(keycode);
		heldKeys.add(keycode);
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
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
		// TODO handle this
		
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
