package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.graphics.ItemSprite;

public class Item extends Entity {

	public static final float RADIUS = 0.25f;
	
	public Entity holder;
	public Body body;
	
	public ItemSprite sprite;
	
	private GameWorld gameWorld;
	
	public Item(GameWorld gameWorld, Vector2 position, String spriteKey) {
		super(2);
		
		this.gameWorld = gameWorld;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.fixedRotation = true;
		
		body = gameWorld.world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS);
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		body.setUserData(this);
		
		sprite = new ItemSprite(gameWorld.itemSheet, spriteKey);
	}
	
	public Item(GameWorld gameWorld, Entity holder, String spriteKey) {
		this(gameWorld, holder.getPosition(), spriteKey);
		
		this.holder = holder;
	}

	@Override
	public void delete()
	{
		super.delete();
		gameWorld.destroyBody(body);
	};
	
	@Override
	public void update(float deltaTime, GameWorld gameWorld) {
		super.update(deltaTime, gameWorld);
		
		if (holder != null) {
			body.setTransform(holder.getPosition(), 0f);
		}
	}

	@Override
	public void render(float deltaTime, GameWorld gameWorld) {
		sprite.render(gameWorld.spriteBatch, getPosition().cpy().scl(Config.PIXELS_PER_METER), getScale());
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	private float getScale() {
		if (holder != null) return 4f; // half
		
		return 6f; // on ground
	}
	
	public void give(Human fool) {
		// TODO give regular items to fool's inventory
		this.holder = fool;
	}

}
