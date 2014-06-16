package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.Config.AnimatedSpriteInfo;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.graphics.AnimatedSprite;
import com.lostcodestudios.fools.gameplay.graphics.AnimatedSprite.Direction;

public class Human extends Entity {

	private AnimatedSprite sprite;
	private String updateScriptBody;
	private ObjectMap<String, Object> updateScriptArgs = new ObjectMap<String, Object>();
	private boolean runUpdateScript = false;
	private World world;
	
	public Body body;

	
	public Human(GameWorld gameWorld, String animatedSpriteKey, Vector2 position) {
		super(2);
		this.world = gameWorld.world;
		AnimatedSpriteInfo info = Config.spriteInfo.get(animatedSpriteKey);

		sprite = new AnimatedSprite(gameWorld.spriteSheet, info.frameX, info.frameY, info.frameWidth, info.frameHeight);
		
		position.scl(64);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		
		body = gameWorld.world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.25f*64);
		fixtureDef.shape = shape;
		
		body.createFixture(fixtureDef);

	}
	
	public Human(GameWorld gameWorld, String animatedSpriteKey, Vector2 position, String updateScriptPath, ObjectMap<String, Object> updateScriptArgs) {
		this(gameWorld, animatedSpriteKey, position);
		
		this.updateScriptBody = updateScriptPath;
		if(updateScriptArgs != null)
			this.updateScriptArgs.putAll(updateScriptArgs);
		
		this.updateScriptArgs.put("e", this);
		this.updateScriptArgs.put("body", this.body);
		
		runUpdateScript = true;
	}
	
	
	public void update(float deltaTime, GameWorld gameWorld) {
		super.update(deltaTime, gameWorld);
		
		sprite.update(deltaTime);
		
		if (runUpdateScript) {
			gameWorld.scripts.runScript(updateScriptBody, updateScriptArgs);
		}
	}
	
	@Override
	public void render(float deltaTime, GameWorld gameWorld) {
		Vector2 velocity = body.getLinearVelocity();
		
		float speed = velocity.len();
		
		if (speed > 0) {
			float angle = velocity.angle();
			
			if (angle < 45 || angle > 315) {
				sprite.setDirection(Direction.Right);
			} else if (angle >= 45 && angle < 135) {
				sprite.setDirection(Direction.Up);
			} else if (angle >= 135 && angle < 225) {
				sprite.setDirection(Direction.Left);
			} else {
				sprite.setDirection(Direction.Down);
			}
		}
		
		sprite.setMovementSpeed(speed);
		
		sprite.render(gameWorld.spriteBatch, getPosition().cpy());
	}

	
	@Override
	public void delete()
	{
		super.delete();
		world.destroyBody(body);
		
	};
	
	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

}
