package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Fool extends Script {

	@Override
	public void run(GameWorld gameWorld, ObjectMap<String, Object> args) {
		Vector2 velocity = new Vector2();
		Body body = (Body)args.get("body");

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

		velocity.scl(5*64);

		body.setLinearVelocity(velocity);
	}
	
	static{
		Script.Register(new Fool());
	}
}
