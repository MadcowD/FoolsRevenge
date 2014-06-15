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
		if (!gameWorld.isPaused()) {
			root.update(deltaTime, gameWorld);
		}
	}
	
	public void render(final float deltaTime, Rectangle cameraBounds){
		gameWorld.spriteBatch.begin();
		
		root.executeBy(cameraBounds, new EntityProcess(){
			public void run(Entity e) {
				e.render(deltaTime, gameWorld);
			}
		});
		
		gameWorld.spriteBatch.end();
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
