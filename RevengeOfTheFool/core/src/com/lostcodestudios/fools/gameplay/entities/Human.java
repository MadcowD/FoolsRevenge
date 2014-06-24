package com.lostcodestudios.fools.gameplay.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.Config.AnimatedSpriteInfo;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.graphics.AnimatedSprite;
import com.lostcodestudios.fools.gameplay.graphics.AnimatedSprite.Direction;

public class Human extends Entity {

	public static final float RADIUS = 0.25f;
	
	private static final float DEFAULT_HEALTH = 5f;
	private static final float HEALTH_POTION_VALUE = 5f;
	
	private static final int VIEW_VERTICES = 5;
	private static final float VIEW_FOV = 0.4f;
	private static final float VIEW_RANGE = 10f;
	
	private String spriteKey;
	private AnimatedSprite sprite;
	private String updateScriptBody;
	private ObjectMap<String, Object> updateScriptArgs = new ObjectMap<String, Object>();
	private boolean runUpdateScript = false;
	private GameWorld gameWorld;
	
	public String tag = "";
	public String group = "";
	
	public Body body;
	
	private float health = DEFAULT_HEALTH;
	private float maxHealth = DEFAULT_HEALTH;
	private String deathScript;
	
	public float knockbackTime = 0f; // humans don't control velocity while being knocked back
	
	public Weapon weapon;
	
	public Array<Item> inventory = new Array<Item>();
	
	public int healthPotions;
	
	public Human(GameWorld gameWorld, String animatedSpriteKey, Vector2 position) {
		super(2);
		this.gameWorld = gameWorld;
		
		this.spriteKey = animatedSpriteKey; // for the corpse... later :)
		AnimatedSpriteInfo info = Config.spriteInfo.get(animatedSpriteKey);

		sprite = new AnimatedSprite(gameWorld.spriteSheet, info.frameX, info.frameY, info.frameWidth, info.frameHeight);
		
		// create the main human body
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(position);
		bodyDef.fixedRotation = true;
		
		body = gameWorld.world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS);
		fixtureDef.shape = shape;
		
		Fixture bf = body.createFixture(fixtureDef);
		bf.setUserData(this);
		
		shape.dispose();
		
		// create the view sensor fixture
		
		createView(body);
		
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
	
	private void createView(Body body) {
		PolygonShape viewShape = new PolygonShape();
		
		Vector2[] verts = new Vector2[VIEW_VERTICES];
		verts[0] = new Vector2(0, 0);

		float radians = (float) (2f * Math.PI * VIEW_FOV);

		float start = radians / 2;

		for (int i = 1; i < VIEW_VERTICES; ++i) {
			float angle = start - ((float) i / (VIEW_VERTICES))
					* radians;

			verts[i] = new Vector2(VIEW_RANGE * (float) Math.cos(angle),
					VIEW_RANGE * (float) Math.sin(angle));
		}

		viewShape.set(verts);

		FixtureDef fd = new FixtureDef();
		fd.shape = viewShape;
		fd.isSensor = true;

		Fixture fixture = body.createFixture(fd); // Create the new fixture
		
		if (fixture.isSensor()) {
			System.out.println("Yeee");
		}
		
		fixture.setUserData(this); // Set the new fixture's user data
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
	
	public void damage(float amount, Human attacker) {
		this.health -= amount;
		
		if (this.health <= 0) {
			gameWorld.entities.add(new Corpse(gameWorld.spriteSheet, getPosition(), spriteKey)); // with a person on top!
			gameWorld.entities.add(new Corpse(gameWorld.spriteSheet, getPosition(), "Blood")); // bloodstain
			
			this.delete();
			
			if (this.deathScript != null && !this.deathScript.isEmpty()) {
				updateScriptArgs.put("killer", attacker);
				gameWorld.scripts.runScript(deathScript, updateScriptArgs);
			}
		}
	}
	
	public boolean isDead() {
		return this.health <= 0;
	}
	
	public boolean foolCanPickpocket() {
		return true; // TODO this should be conditional on AI state
	}
	
	public void heal(float amount) {
		this.health += amount;
		
		if (this.health >= maxHealth) {
			this.health = maxHealth;
		}
	}
	
	public void drinkHealthPotion() {
		if (this.healthPotions > 0) {
			heal(HEALTH_POTION_VALUE);
			--this.healthPotions;
		}
	}
	
	public void update(float deltaTime, GameWorld gameWorld) {
		super.update(deltaTime, gameWorld);
		
		sprite.update(deltaTime);
		
		if (knockbackTime > 0) {
			knockbackTime -= deltaTime;
			if (knockbackTime < 0) {
				knockbackTime = 0;
				setVelocity(new Vector2()); // stop being knocked back
			}
		}
		
		if (runUpdateScript) {
			updateScriptArgs.put("deltaTime", deltaTime);
			gameWorld.scripts.runScript(updateScriptBody, updateScriptArgs);
		}
		
		// slow for stair tiles
		TiledMapTileLayer floorLayer = (TiledMapTileLayer) gameWorld.tileMap.getLayers().get("Floor");
		
		Cell currentFloorCell = floorLayer.getCell((int)getPosition().x, (int)getPosition().y);
		
		if (currentFloorCell != null) {
			MapProperties currentFloorProperties = currentFloorCell.getTile().getProperties();
			
			if (currentFloorProperties.get("stairs") != null) {
				body.setLinearVelocity(body.getLinearVelocity().scl(Config.STAIR_SPEED));
			}
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
	
	public void renderHealthBar(GameWorld gameWorld) {
		if (!tag.equals("Fool")) {
			final float OFFSET_Y = 48;
			final float WIDTH = 48;
			final float HEIGHT = 4;
			
			// health bar
			ShapeRenderer shapeRenderer = gameWorld.worldShapeRenderer;
			
			shapeRenderer.setColor(Color.MAROON);
			shapeRenderer.rect(getPosition().x * Config.PIXELS_PER_METER - WIDTH / 2, getPosition().y * Config.PIXELS_PER_METER + OFFSET_Y,
					WIDTH, HEIGHT);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(getPosition().x * Config.PIXELS_PER_METER - WIDTH / 2, getPosition().y * Config.PIXELS_PER_METER + OFFSET_Y,
					WIDTH * healthFraction(), HEIGHT);
		}
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
	
	public boolean hasItem(String name) {
		for (Item item : inventory) {
			if (item.name.equals(name)) {
				return true;
			}
		}
		
		return false;
	}

}
