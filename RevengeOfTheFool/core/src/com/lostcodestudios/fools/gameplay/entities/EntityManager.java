package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.math.Rectangle;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class EntityManager {

	private GameWorld gameWorld;
	EntityRegion root;

	
	//-----------------------------------
	//------------- CONSTRUCTOR
	//-----------------------------------
	
	/**
	 * Creates a ENtityManager sotring and EntityRegion tree of depth height.
	 * @param gameWorld The game world singleton to reference.
	 * @param height The height of the tree.
	 */
	public EntityManager(GameWorld gameWorld, int height) {
		this.gameWorld = gameWorld;
		
		//Build the EntityRegion.
		root = new EntityRegion(height, gameWorld.getBounds());
	}
	
	//-----------------------------------
	//------------- PROCESSING
	//-----------------------------------
	
	public void update(final float deltaTime){
		root.update(deltaTime, gameWorld);
	}
	
	public void render(final float deltaTime, Rectangle cameraBounds){
		root.executeBy(cameraBounds, new EntityProcess(){
			public void run(Entity e) {
				if (!gameWorld.isPaused()) {
					e.update(deltaTime, gameWorld);
				}
				
				e.render(deltaTime, gameWorld);
			}
		});
	}
	
	//-----------------------------------
	//------------- MODIFICATION FUNCTIONS
	//-----------------------------------
	
	public void add(Entity e){
		root.add(e);
	}
	
	public void remove(Entity e){
		e.delete();
	}
	

}
