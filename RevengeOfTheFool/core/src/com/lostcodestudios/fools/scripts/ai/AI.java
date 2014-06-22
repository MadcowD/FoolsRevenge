package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class AI extends Script{
	/**
	 * Intiitalizes the AI with a current state.
	 * @param inititalState The initial state of the AI script.
	 * @param initialState 
	 */
	public AI(State initialState){
		setState(initialState);
	}
	
	
	/**
	 * Runs the current AI State.
	 */
	public  void run(GameWorld world, ObjectMap<String, Object> args){
			currentState.run(world, args); 
	}
	
	
	public void setState(State  newState){
		this.currentState = newState;
		this.currentState.parent = this;
	}
	protected State currentState;
}
