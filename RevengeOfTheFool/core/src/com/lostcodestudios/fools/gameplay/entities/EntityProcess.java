package com.lostcodestudios.fools.gameplay.entities;
import com.lostcodestudios.fools.gameplay.GameWorld;

/**
 * An entity process functor which runs a command on entities.
 * If the world is needed just make it final.
 * @author William
 */
public interface EntityProcess {
	/**
	 * The process to run.
	 * @param e The entity on which the process will run.
	 */
	public void run(Entity e);
}
