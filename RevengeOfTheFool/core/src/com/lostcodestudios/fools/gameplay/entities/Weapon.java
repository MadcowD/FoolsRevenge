package com.lostcodestudios.fools.gameplay.entities;

import com.lostcodestudios.fools.gameplay.GameWorld;

public class Weapon extends Item {

	public Weapon(GameWorld gameWorld, Entity holder, String spriteKey) {
		super(gameWorld, holder, spriteKey);
	}
	
	public float meleeDamage;
	
	@Override
	public void update(float deltaTime, GameWorld gameWorld) {
		super.update(deltaTime, gameWorld);
	}

	@Override
	public void render(float deltaTime, GameWorld gameWorld) {
		if (holder == null)
			super.render(deltaTime, gameWorld);
	}
	
	@Override
	public void give(Human fool) {
		fool.weapon = this;
		this.holder = fool;
	}
	
}
