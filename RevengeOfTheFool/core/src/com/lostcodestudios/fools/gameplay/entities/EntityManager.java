package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
			//root.rebalance();
			root.update(deltaTime, gameWorld);
			root.rebalance();
			root.rebalance();

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
	
	public void renderHUD(final float deltaTime, Rectangle cameraBounds) {
		if (!gameWorld.cutsceneMode) {
			
			// render health bars
			
			gameWorld.worldShapeRenderer.begin(ShapeType.Filled);
			
			root.executeBy(cameraBounds, new EntityProcess(){
				public void run(Entity e) {
					if (e instanceof Human) {
						((Human) e).renderHealthBar(gameWorld);
					}
				}
			});
			
			gameWorld.worldShapeRenderer.end();
			
			// render HUD texts
			
			gameWorld.spriteBatch.begin();
			
			root.executeBy(cameraBounds, new EntityProcess(){
				public void run(Entity e) {
					if (e instanceof Item) {
						Item i = (Item)e;
						
						if (i.selected) 
							i.renderText(gameWorld);
					} else if (e instanceof Door) {
						Door d = (Door)e;
						
						if (d.selected) {
							d.renderText(gameWorld);
						}
					} else if (e instanceof Switch) {
						Switch s = (Switch)e;
						
						if (s.selected) {
							s.renderText(gameWorld);
						}
					}
				}
			});
		
			gameWorld.spriteBatch.end();
		}
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
