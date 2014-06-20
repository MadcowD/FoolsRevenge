package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Weapon extends Item {

	public Weapon(GameWorld gameWorld, Entity holder, String spriteKey) {
		super(gameWorld, holder, spriteKey);
		
		initWeapon(spriteKey);
	}
	
	public Weapon(GameWorld gameWorld, Vector2 position, String spriteKey) {
		super(gameWorld, position, spriteKey);
		
		initWeapon(spriteKey);
	}
	
	public float meleeDamage;

	private void initWeapon(String spriteKey) {
		if (spriteKey.equals("Sword")) {
			meleeDamage = 2.5f;
		}
		
		// TODO initialize different types of weapons here
	}
	
	@Override
	public void render(float deltaTime, GameWorld gameWorld) {
		if (holder == null)
			super.render(deltaTime, gameWorld);
	}
	
	@Override
	public void give(Human human) {
		if (human != null && ((Human)human).weapon != null) {
			((Human)human).weapon.holder = null;
		}
		
		human.weapon = this;
		this.holder = human;
		this.selected = false;
	}
	
}
