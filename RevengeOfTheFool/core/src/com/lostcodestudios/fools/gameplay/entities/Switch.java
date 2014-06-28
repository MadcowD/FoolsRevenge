package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.TextManager;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Switch extends Entity {
	public static final float RADIUS = 0.25f;
	
	public Entity holder;
	public Body body;
	
	private TextureRegion normalFrame;
	private TextureRegion pulledFrame;
	
	public String name;
	
	private GameWorld gameWorld;
	
	public boolean selected = false;
	
	private boolean pulled = false;
	
	private String triggerScript;
	
	private Vector2 origin;
	
	public Switch(GameWorld gameWorld, Vector2 position, String triggerScript) {
		super(2);
		
		this.gameWorld = gameWorld;
		this.triggerScript = triggerScript;
		
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
		
		Rectangle frame1 = Config.switchSpriteFrame1;
		Rectangle frame2 = Config.switchSpriteFrame2;
		
		normalFrame = new TextureRegion(gameWorld.doorSpriteSheet, (int)frame1.x, (int)frame1.y, (int)frame1.width, (int)frame1.height);
		pulledFrame = new TextureRegion(gameWorld.doorSpriteSheet, (int)frame2.x, (int)frame2.y, (int)frame2.width, (int)frame2.height);
		
		origin = new Vector2(frame1.width * Config.SPRITE_SCALE / 2, frame1.height * Config.SPRITE_SCALE / 2);
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
		
		if (selected) {
			if (gameWorld.input.wasKeyPressed(Config.ACCEPT_KEY)) {
				trigger(gameWorld.specialEntities.get("Fool")); // the player did it
			}
		}
	}
	
	public void trigger(Entity e) {
		ObjectMap<String, Object> args = new ObjectMap<String, Object>();
		args.put("e", e);
		//gameWorld.scripts.runScript(triggerScript, args);
		pulled = !pulled;
	}

	@Override
	public void render(float deltaTime, GameWorld gameWorld) {
		TextureRegion currentFrame = null;
		
		if (this.pulled) {
			currentFrame = pulledFrame;
		} else {
			currentFrame = normalFrame;
		}
		
		gameWorld.spriteBatch.draw(currentFrame, getPosition().x * Config.PIXELS_PER_METER - origin.x, getPosition().y * Config.PIXELS_PER_METER - origin.y, 
				origin.x, origin.y, currentFrame.getRegionWidth() * Config.SPRITE_SCALE, currentFrame.getRegionHeight() * Config.SPRITE_SCALE, 1f, 1f, 0f);
	}
	
	public void renderText(GameWorld gameWorld) {
		SpriteBatch spriteBatch = gameWorld.spriteBatch;
		
		String text = Config.ACCEPT_TEXT + " Pull Switch";
		
		float width = TextManager.getFont("ui-white").getBounds(text).width;
		TextManager.draw(spriteBatch, "ui-white", text, getPosition().x * Config.PIXELS_PER_METER - width / 2, getPosition().y * Config.PIXELS_PER_METER + 96);
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

}
