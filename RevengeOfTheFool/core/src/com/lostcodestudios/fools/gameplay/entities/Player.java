package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Player extends Human {

	public Player(GameWorld gameWorld, Vector2 position) {
		super(gameWorld, "Fool", position);
	}
	
	public void update(float deltaTime, GameWorld gameWorld) {
		super.update(deltaTime, gameWorld);
		
		Vector2 velocity = new Vector2();
		
		if (gameWorld.input.isKeyHeld(Keys.A)) {
		    velocity.x = -1;
		} else if (gameWorld.input.isKeyHeld(Keys.D)) {
		    velocity.x = 1;
		}
		
		if (gameWorld.input.isKeyHeld(Keys.W)) {
		    velocity.y = 1;
		} else if (gameWorld.input.isKeyHeld(Keys.S)) {
		    velocity.y = -1;
		}
		
		velocity.nor();
		
		velocity.scl(5);
		
		body.setLinearVelocity(velocity);
	}
	
	
}
