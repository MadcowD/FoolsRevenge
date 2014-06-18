package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.lostcodestudios.fools.Config;
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
	
	public Array<Entity> entitiesAtCursor(Rectangle cameraBounds) {
		
		final Array<Entity> entities = new Array<Entity>();
		final Vector2 worldCursorPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		worldCursorPosition.add(gameWorld.camera.position.x - gameWorld.camera.viewportWidth / 2, gameWorld.camera.position.y - gameWorld.camera.viewportHeight / 2);
		worldCursorPosition.scl(1f / Config.PIXELS_PER_METER);
		
		root.executeBy(cameraBounds, new EntityProcess() {
			public void run(Entity e) {
				Body body = null;
				
				if (e instanceof Human) {
					body = ((Human) e).body;
				} else if (e instanceof Item) {
					body = ((Item) e).body;
				}
				
				if (body != null) {
					Array<Fixture> fixtures = body.getFixtureList();
					
					for (Fixture fixture : fixtures) {
						if (fixture.testPoint(worldCursorPosition)) {
							entities.add(e);
							break;
						}
					}
				}
			}
		});
		
		return entities;
		
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
