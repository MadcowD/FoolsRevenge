package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.gameplay.GameWorld;

public abstract class Entity {

	protected int depth;
	protected EntityRegion region;
	
	


	public abstract void render(float deltaTime, GameWorld gameWorld);

	public void update(float deltaTime, GameWorld gameWorld) {
		if(!region.getRegion().contains(this.getPosition()))
			region.changed(this);
		
	}
	
	public void delete() {
		this.region.remove(this);
	}
	

	/**
	 * Gets the position of a given entity.
	 * @return
	 */
	public abstract Vector2 getPosition();

	public EntityRegion getRegion() {
		return region;
	}

	public void setRegion(EntityRegion region) {
		this.region = region;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}



}
