package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.math.Rectangle;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class EntityManager {

	private GameWorld gameWorld;
	EntityRegion root;
	private int height;

	
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
		
		Rectangle bounds = gameWorld.getBounds();
		//Build the EntityRegion.
		root = new EntityRegion(0,bounds, null);
		root.setSubRegions(generateRegionTree(1, root));
	}
	
	/**
	 * Generates the subRegions of the root node.
	 * @param depth
	 * @param root
	 * @return
	 */
	private EntityRegion[] generateRegionTree(int depth, EntityRegion root){
		if(depth < height){
			EntityRegion[] regions = new EntityRegion[4];
			
			for(int i =0; i< 4; i++)
			{
				
				//Partition tge rectangles
				Rectangle supRect = root.getRegion();
				Rectangle subRect = new Rectangle(
						supRect.x+ (i%2)*(supRect.width/2f),
						supRect.y+ (i/(int)2)*(supRect.height/2f),
						supRect.width/2f,
						supRect.height/2f);
				
				//Establish new rectangle.
				regions[i] = new EntityRegion(depth,subRect, root);
				regions[i].setSubRegions(generateRegionTree(depth+1, regions[i]));
			}
			
			return regions;
		}
		else
			return null;

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
