package com.lostcodestudios.fools.gameplay.entities;

import java.util.ArrayList;
import java.util.Stack;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * The entity region class essentially is a node on the world bounded volume hierachry. 
 * Stores entities at a certain 'activity level'.
 * @author William
 */
public class EntityRegion {

	private EntityRegion[] subRegions;
	private EntityRegion superRegion;
	private Rectangle region;

	private Array<Entity> entities;
	private Stack<Entity> changed;
	private Stack<Entity> added;
	private Array<Entity> removed;
	private int depth;

	//-----------------------------------
	//------------- CONSTRUCTOR
	//-----------------------------------

	public EntityRegion(int depth, Rectangle region, EntityRegion superRegion){
		entities = new Array<Entity>();
		changed = new Stack<Entity>();



		this.depth = depth;
		this.region = region;
		this.superRegion = superRegion;
	}


	//-----------------------------------
	//------------- MODIFICATION FUNCTIONS
	//-----------------------------------

	/**
	 * Rebalances the entity tree.
	 */
	public void rebalance(){
		//Remove entities.
		this.entities.removeAll(removed, false);
		removed.clear();

		//Add entities
		while(added.size() > 0){
			Entity toAdd = added.pop();
			toAdd.setRegion(this);
			this.entities.add(toAdd);
		}
		
		while(changed.size() > 0)
			change(changed.pop());

		//TODO: SHOULD THIS BE RECURSIVE?
	}

	/**
	 * Recursive rebalance of a given entity.
	 * @param e The entity to rebalance.
	 */
	private void change(Entity e){
		if(superRegion == null || this.contains(e.getPosition()))
			this.add(e);
		else
			superRegion.change(e);
	}

	/**
	 * Adds an entity to the given node at the entities correct depth.
	 * @param e
	 */
	public void add(Entity e){
		if(this.depth == e.getDepth() && this.contains(e.getPosition()))
			this.added.push(e);
		else if(this.subRegions != null)
			for(EntityRegion sub : subRegions)
				sub.add(e);
	}

	/**
	 * Removes and entity. O(NLOGN)
	 * @param e The entity to remove
	 * @return 
	 */
	public boolean remove(Entity e){
		if(this.contains(e.getPosition())){
			if(this.entities.contains(e, false))
				removed.add(e);
			else if(this.subRegions != null)
				for(EntityRegion sub : subRegions)
					sub.remove(e);
		}
		return false;
	}
	
	/**
	 * Removes an entity given that it is known to be contained.
	 * @param e The entity to remove.
	 * @return
	 */
	boolean unsafeRemove(Entity e){
		removed.add(e);
	}



	//-----------------------------------
	//------------- HELPER FUNCTIONS
	//-----------------------------------

	/**
	 * Excutes an entity process on all entities.
	 * @param p The process to execute.
	 */
	public void executeAll(EntityProcess p){
		for(Entity e : entities)
			p.run(e);

		if(this.subRegions != null)
			for(EntityRegion sub : this.subRegions)
				sub.executeAll(p);
	}

	/**
	 * Executes an entity process on a given rectangle bounds
	 * @param r The rectangle within which to execute the entity process.
	 * @param p The process to execute.
	 */
	public void execute(Rectangle r, EntityProcess p){
		if(this.overlaps(r)){
			for(Entity e : entities)
				p.run(e);

			if(this.subRegions != null)
				for(EntityRegion sub : this.subRegions)
					sub.execute(r, p);
		}
	}


	/**
	 * CAUTION COSTLY OPERATION (NLOGN)
	 * Selects entities in a given rectangle.
	 * @param r The rectangle
	 * @returns An array of entity. DO NOT MODIFY.
	 */
	public Array<Entity> select(Rectangle r){
		Array<Entity> result = new Array<Entity>();
		select(r, result);

		return result;
	}

	/**
	 * Recursive selection function
	 * @param r
	 * @param temp
	 */
	protected void select(Rectangle r, Array<Entity> temp){
		if(this.overlaps(r)){
			//Gets all the entities at this level.
			temp.addAll(this.entities);

			//Gathers the proper enttiies from the sub regions.
			if(this.subRegions != null)
				for(EntityRegion sub : this.subRegions)
					sub.select(r, temp);

		}		
	}


	/**
	 * CAUTION COSTLY OPERATION O(N)+O(NLOGN)
	 * Searches the region and following sub regions for the given entity. (Uses recursion)
	 * @param e The entity e to search for.
	 * @return Whether or not the entity is contained within the region or any following sub-regions.
	 */
	public boolean contains(Entity e){
		if(this.entities.contains(e, false))
			return true;

		//Checks the sub regions.
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


	//-----------------------------------
	//------------- GETTERS & SETTERS
	//-----------------------------------


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
