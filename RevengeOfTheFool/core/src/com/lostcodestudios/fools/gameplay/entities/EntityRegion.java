package com.lostcodestudios.fools.gameplay.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * The entity region class essentially is a node on the world bounded volume hierachry. 
 * Stores entities at a certain 'activity level'.
 * @author William
 */
public class EntityRegion {
	
	private EntityRegion[] subRegions;
	private EntityRegion superRegion;
	private Rectangle region;
	
	private ArrayList<Entity> entities;
	
	//-----------------------------------
	//-------------
	//-----------------------------------
	
	public EntityRegion(Rectangle region, EntityRegion superRegion){
		entities = new ArrayList<Entity>();
		
		this.region = region;
		this.superRegion = superRegion;
	}
	

	//-----------------------------------
	//-------------
	//-----------------------------------
	
	
	//TODO: CREATE SELECTS FUNCTIONS FOR ENTITIES AND REGIONS.

	/**
	 * CAUTION COSTLY OPERATION O(N)+O(NLOGN)
	 * Searches the region and following sub regions for the given entity. (Uses recursion)
	 * @param e The entity e to search for.
	 * @return Whether or not the entity is contained within the region or any following sub-regions.
	 */
	public boolean contains(Entity e){
		if(this.entities.contains(e))
			return true;
		else if (this.subRegions != null)
			for(EntityRegion sub : subRegions)
				if(sub.contains(e))
					return true;
		
		return false;
	}
	
	/**
	 * Checks whether or a given coordinate resides within the region.
	 * @param position The given coordinate.
	 * @return
	 */
	public boolean contains(Vector2 position){
		return this.region.contains(position);
	}
	
	/**
	 * Returns whether or not a rectangle is a subset of the region.
	 * @param r The rectangle to which this entity region will be comapred.
	 * @return Whether or not this entity region contains the rectangle r.
	 */
	public boolean contains(Rectangle r){
		return this.region.contains(r);
	}
	
	
	/**
	 * Tests for region overlap.
	 * @param r The region r to which this entity region will be compared.
	 * @return Whether or not the entityregion intersects the rectangle.
	 */
	public boolean overlaps(Rectangle r){
		return this.region.overlaps(r);
	}
	
	
	/**
	 * @return the subRegions
	 */
	public EntityRegion[] getSubRegions() {
		return subRegions;
	}
	
	/**
	 * Only accesible by the entities package.
	 * @param subRegions the subRegions to set
	 */
	void setSubRegions(EntityRegion[] subRegions) {
		this.subRegions = subRegions;
	}

	/**
	 * @return the superRegion
	 */
	public EntityRegion getSuperRegion() {
		return superRegion;
	}

	/**
	 * @return the region
	 */
	public Rectangle getRegion() {
		return region;
	}
}
