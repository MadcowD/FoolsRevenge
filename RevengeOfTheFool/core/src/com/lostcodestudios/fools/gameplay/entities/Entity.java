package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Entity {

	private int depth;
	private EntityRegion region;

	public Vector2 getPosition() {
		return null;
	}

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


	public void delete() {
		this.region.remove(this);
	}

	public void render(float deltaTime, GameWorld gameWorld) {
		// TODO Auto-generated method stub
		
	}

	public void update(float deltaTime, GameWorld gameWorld) {
		// TODO Auto-generated method stub
		
	}
}
