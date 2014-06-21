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
	public AI(Script initialState){
		this.currentState = initialState;
	}
	
	
	/**
	 * Runs the current AI State.
	 */
	public  void run(GameWorld world, ObjectMap<String, Object> args){
		try{
			currentState.run(world, args);
		}
		catch(NullPointerException e){
			System.out.println("FAILED TO IINITIALIZE STATES PROPERLY. SET CURRENT STATE.");
			}
		}
	
	
	public void setState(Script  newState){
		this.currentState = newState;
	}
	protected Script currentState;
}
