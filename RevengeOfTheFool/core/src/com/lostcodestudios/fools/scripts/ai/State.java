/**
 * 
 */
package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

/**
 * @author William
 * The state based AI scripting class.
 */
public abstract class State extends Script {
	State nextState;
	protected AI parent;

	/**
	 * Initializes the state
	 * @param parent
	 * @param nextState
	 */
	public State(State nextState){
		this.nextState = nextState;
	}
	
	/**
	 * Ends the state
	 */
	public final void end(){
		onEnd();
		parent.setState(nextState);
	}
	
	/**
	 * Called when the state has ended.
	 */
	protected void onEnd(){
	}

}
