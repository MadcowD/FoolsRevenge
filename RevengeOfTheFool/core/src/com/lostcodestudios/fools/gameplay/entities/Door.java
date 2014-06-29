package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.SoundManager;
import com.lostcodestudios.fools.TextManager;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class Door extends Entity {

	private boolean open = false;
	public Body body;
	
	private TextureRegion closedRegion;
	private TextureRegion openRegion;
	private Vector2 origin;
	
	public boolean selected = false;
	
	public String key = "";
	
	public Door(GameWorld gameWorld, String spriteKey, Vector2 position) {
		super(2);
		
		Rectangle[] frames = Config.doorSpriteInfo.get(spriteKey);
		
		Rectangle closedFrame = frames[0];
		Rectangle openFrame = frames[1];
		
		closedRegion = new TextureRegion(gameWorld.doorSpriteSheet, (int)closedFrame.x, (int)closedFrame.y, (int)closedFrame.width, (int)closedFrame.height);
		openRegion = new TextureRegion(gameWorld.doorSpriteSheet, (int)openFrame.x, (int)openFrame.y, (int)openFrame.width, (int)openFrame.height);
		
		origin = new Vector2((float)closedRegion.getRegionWidth() * Config.SPRITE_SCALE / 2, (float)closedRegion.getRegionHeight() * Config.SPRITE_SCALE / 2);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(position);
		bodyDef.type = BodyType.StaticBody;
		
		body = gameWorld.world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		
		float hwidth = (float)closedFrame.getWidth() * 4f / Config.PIXELS_PER_METER;
		float hheight = (float)closedFrame.getHeight() * 4f / Config.PIXELS_PER_METER;
		
		if (hwidth < hheight) {
			hwidth = 1f / 4;
		} else {
			hheight = 1f / 4;
		}
		
		shape.setAsBox(hwidth, hheight);
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef);
		body.getFixtureList().get(0).setUserData(this);
		
		body.setUserData(this);
	}
	
	public boolean isOpen() {
		return open;
	}
	
	@Override
	public void update(float deltaTime, GameWorld gameWorld) {
		super.update(deltaTime, gameWorld);
		
		Human fool = (Human) gameWorld.specialEntities.get("Fool");
		
		float distance = fool.getPosition().sub(this.getPosition()).len();
		
		selected = body.getFixtureList().get(0).testPoint(gameWorld.worldCursorPosition())
				&& distance <= Config.INTERACT_DISTANCE;
		
		if (selected && gameWorld.input.wasKeyPressed(Config.ACCEPT_KEY) && (this.open || (!key.isEmpty() && fool.hasItem(key)) || key.isEmpty())) {
			this.toggleOpen(gameWorld);
		}
	}

	@Override
	public void render(float deltaTime, GameWorld gameWorld) {
		TextureRegion currentFrame = null;
		
		if (this.open) {
			currentFrame = openRegion;
		} else {
			currentFrame = closedRegion;
		}
		
		gameWorld.spriteBatch.draw(currentFrame, getPosition().x * Config.PIXELS_PER_METER - origin.x, getPosition().y * Config.PIXELS_PER_METER - origin.y, 
				origin.x, origin.y, currentFrame.getRegionWidth() * Config.SPRITE_SCALE, currentFrame.getRegionHeight() * Config.SPRITE_SCALE, 1f, 1f, 0f);
	}
	
	public void renderText(GameWorld gameWorld) {
		Human fool = (Human) gameWorld.specialEntities.get("Fool");
		
		SpriteBatch spriteBatch = gameWorld.spriteBatch;
		
		String text = "";
		
		if (!open) {
			if (key.isEmpty()) {
				text = Config.ACCEPT_TEXT + " Open Door";
			} else if (!fool.hasItem(key)) {
				text = "Locked";
			} else {
				text = Config.ACCEPT_TEXT + " Unlock Door";
			}
		} else {
			text = Config.ACCEPT_TEXT + " Close Door";
		}
		
		float width = TextManager.getFont("ui-white").getBounds(text).width;
		TextManager.draw(spriteBatch, "ui-white", text, getPosition().x * Config.PIXELS_PER_METER - width / 2, getPosition().y * Config.PIXELS_PER_METER + 96);
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public void toggleOpen(GameWorld world) {
		this.open = !this.open;
		
		Human fool = (Human) world.specialEntities.get("Fool");
		
		if (this.open) {
			body.getFixtureList().get(0).setSensor(true);
		} else {
			body.getFixtureList().get(0).setSensor(false);
		}
		
		SoundManager.playSound("snd_door", getPosition(), fool.getPosition());
	}

}
