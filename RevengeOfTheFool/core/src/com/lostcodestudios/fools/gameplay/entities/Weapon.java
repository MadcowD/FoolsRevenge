package com.lostcodestudios.fools.gameplay.entities;

import com.lostcodestudios.fools.gameplay.GameWorld;

public class Weapon extends Item {

	public Weapon(GameWorld gameWorld, Entity holder, String spriteKey) {
		super(gameWorld, holder, spriteKey);
	}
	
	public float meleeDamage;

}
