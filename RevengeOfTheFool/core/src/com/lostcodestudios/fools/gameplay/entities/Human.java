package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.Config.AnimatedSpriteInfo;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.graphics.AnimatedSprite;
import com.lostcodestudios.fools.gameplay.graphics.AnimatedSprite.Direction;

public class Human extends Entity {

	public static final float RADIUS = 0.25f;
	
	private static final float DEFAULT_HEALTH = 5f;
	
	private AnimatedSprite sprite;
	private String updateScriptBody;
	private ObjectMap<String, Object> updateScriptArgs = new ObjectMap<String, Object>();
	private boolean runUpdateScript = false;
	private GameWorld gameWorld;
	
	public Body body;
	
	private float health = DEFAULT_HEALTH;
	private float maxHealth = DEFAULT_HEALTH;
	private String deathScript;
	
	public float knockbackTime = 0f; // humans don't control velocity while being knocked back
	
	public Weapon weapon;
	
	public Human(GameWorld gameWorld, String animatedSpriteKey, Vector2 position) {
		super(2);
		this.gameWorld = gameWorld;
		AnimatedSpriteInfo info = Config.spriteInfo.get(animatedSpriteKey);

		sprite = new AnimatedSprite(gameWorld.spriteSheet, info.frameX, info.frameY, info.frameWidth, info.frameHeight);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.fixedRotation = true;
		
		body = gameWorld.world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS);
		fixtureDef.shape = shape;
		
		body.createFixture(fixtureDef);
		body.setUserData(this);
	}
	
	public Human(GameWorld gameWorld, String animatedSpriteKey, Vector2 position, String updateScriptPath, ObjectMap<String, Object> updateScriptArgs) {
		this(gameWorld, animatedSpriteKey, position);
		
		this.updateScriptBody = updateScriptPath;
		if(updateScriptArgs != null)
			this.updateScriptArgs.putAll(updateScriptArgs);
		
		this.updateScriptArgs.put("e", this);
		
		runUpdateScript = true;
	}
	
	public void setHealth(float health) {
		this.health = health;
		this.maxHealth = health;
	}
	
	public void setDeathScript(String script) {
		this.deathScript = script;
	}
	
	public float healthFraction() {
		return health / maxHealth;
	}
	
	public void damage(float amount) {
		this.health -= amount;
		
		if (this.health <= 0) {
			//TODO die
			this.delete();
			
			if (this.deathScript != null && !this.deathScript.isEmpty()) {
				gameWorld.scripts.runScript(deathScript, updateScriptArgs);
			}
		}
	}
	
	public boolean isDead() {
		return this.health <= 0;
	}
	
	public void heal(float amount) {
		this.health += amount;
		
		if (this.health >= maxHealth) {
			this.health = maxHealth;
		}
	}
	
	public void update(float deltaTime, GameWorld gameWorld) {
		super.update(deltaTime, gameWorld);
		
		sprite.update(deltaTime);
		
		if (knockbackTime > 0) {
			knockbackTime -= deltaTime;
			if (knockbackTime < 0) knockbackTime = 0;
		}
		
		if (runUpdateScript) {
			updateScriptArgs.put("deltaTime", deltaTime);
			gameWorld.scripts.runScript(updateScriptBody, updateScriptArgs);
		}
	}
	
	@Override
	public void render(float deltaTime, GameWorld gameWorld) {
		if (knockbackTime == 0) {
			float angle = (float) Math.toDegrees(body.getAngle());
			
			if (angle <= 45 || angle >= 315) {
				sprite.setDirection(Direction.Right);
			} else if (angle > 45 && angle < 135) {
				sprite.setDirection(Direction.Up);
			} else if (angle >= 135 && angle <= 225) {
				sprite.setDirection(Direction.Left);
			} else {
				sprite.setDirection(Direction.Down);
			}
		
			Vector2 velocity = body.getLinearVelocity();
			float speed = velocity.len();
			sprite.setMovementSpeed(speed);
			
			if (speed > 0) {
				body.setTransform(body.getPosition(), (float) Math.toRadians(velocity.angle()));
			}
		} else {
			sprite.setMovementSpeed(0); // don't walk while being knocked back
		}
		
		sprite.render(gameWorld.spriteBatch, getPosition().cpy().scl(Config.PIXELS_PER_METER));
	}

	
	@Override
	public void delete()
	{
		super.delete();
		gameWorld.destroyBody(body);
	};
	
	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public void setVelocity(Vector2 velocity) {
		if (knockbackTime == 0) {
			body.setLinearVelocity(velocity);
		}
	}
	
	public Vector2 getVelocity() {
		return body.getLinearVelocity();
	}

}
