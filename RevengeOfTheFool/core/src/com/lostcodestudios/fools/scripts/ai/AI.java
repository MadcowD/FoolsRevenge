package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.scripts.Script;

public class AI extends Script {
	
	protected State currentState;
	
	/**
	 * Initializes the AI with an initial state.
	 * @param initialState The initial state of the AI script.
	 */
	public AI(State initialState) {
		setState(initialState);
	}
	
	/**
	 * Runs the current AI State.
	 */
	public void run(GameWorld world, ObjectMap<String, Object> args){
		currentState.run(world, args); 
	}
	
	public void setState(State newState) {
		this.currentState = newState;
		this.currentState.parent = this;
	}
	
	public State getState() {
		return this.currentState;
	}
	
	public void onSight(Entity self, Entity e) {
		currentState.onSight(self, e);
	}
	
	public void sightLost(Entity self, Entity e) {
		currentState.sightLost(self, e);
	}

}
