package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.Config.AnimatedSpriteInfo;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.graphics.AnimatedSprite;

public class Human extends Entity {

	private AnimatedSprite sprite;
	private String updateScriptPath;
	private ObjectMap<String, Object> updateScriptArgs = new ObjectMap<String, Object>();
	
	public Body body;
	
	public Human(GameWorld gameWorld, String animatedSpriteKey, Vector2 position, String updateScriptPath, ObjectMap<String, Object> updateScriptArgs) {
		super(1);
		AnimatedSpriteInfo info = Config.spriteInfo.get(animatedSpriteKey);
		
		sprite = new AnimatedSprite(gameWorld.spriteSheet, info.frameX, info.frameY, info.frameWidth, info.frameHeight);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		
		body = gameWorld.world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.25f);
		fixtureDef.shape = shape;
		
		body.createFixture(fixtureDef);
		
		this.updateScriptPath = updateScriptPath;
		
		for (Entry<String, Object> arg : updateScriptArgs) {
			this.updateScriptArgs.put(arg.key, arg.value);
		}
		
		this.updateScriptArgs.put("e", this);
	}
	
	public void update(float deltaTime, GameWorld gameWorld) {
		super.update(deltaTime, gameWorld);
		
		gameWorld.scripts.runScript(updateScriptPath, updateScriptArgs);
	}
	
	@Override
	public void render(float deltaTime, GameWorld gameWorld) {
		sprite.render(gameWorld.spriteBatch, getPosition().cpy().scl(GameWorld.PIXELS_PER_METER));
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

}
