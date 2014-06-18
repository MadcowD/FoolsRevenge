package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;

import box2dLight.RayHandler;

import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.InputManager;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.EntityManager;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.gameplay.map.Box2DLightsMapObjectParser;
import com.lostcodestudios.fools.gameplay.map.Box2DMapObjectParser;
import com.lostcodestudios.fools.gameplay.map.EntityMapObjectParser;

public class GameWorld {
	
	private static final float TIME_STEP = 1f / 60;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	
	public OrthographicCamera camera;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	public World world;
	private CollisionManager collisionManager;
	
	private RayHandler rayHandler;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public SpriteBatch spriteBatch = new SpriteBatch();
	
	public EventFlagManager flags = new EventFlagManager();
	public ScriptManager scripts = new ScriptManager(this);
	public DialogManager dialog = new DialogManager(this);
	public EntityManager entities;
	public InputManager input;
	public Texture spriteSheet;
	
	private float elapsedTime = 0f;
	
	private boolean paused;
	
	private Rectangle bounds;
	
	public ObjectMap<String, Entity> specialEntities = new ObjectMap<String, Entity>();
	public GameWorld(InputManager input) {
		this.input = input;
		
		camera = new OrthographicCamera(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
		
		TmxMapLoader loader = new TmxMapLoader();
		tileMap = loader.load("castle.tmx");
		
		MapProperties mapProp = tileMap.getProperties();
		int width = (Integer) mapProp.get("width");
		int height = (Integer) mapProp.get("height");
		
		bounds = new Rectangle(0, 0, width, height);
		
		mapRenderer = new OrthogonalTiledMapRenderer(tileMap, Config.SPRITE_SCALE);
		
		world = new World(new Vector2(), true);
		collisionManager = new CollisionManager(this, world);
		rayHandler = new RayHandler(world);
		
		//load physics bodies from the tile map
		Box2DMapObjectParser mapObjectParser = new Box2DMapObjectParser();
		mapObjectParser.setUnitScale(1f / Config.SPRITE_SCALE);
		mapObjectParser.load(world, tileMap.getLayers().get("Physics")); //NOTE: this won't load circle objects!
		
		//load lighting effects from the tile map
		Box2DLightsMapObjectParser lightObjectParser = new Box2DLightsMapObjectParser();
		lightObjectParser.setUnitScale(1f / Config.SPRITE_SCALE);
		lightObjectParser.load(rayHandler, tileMap.getLayers().get("Lights"));
		
		paused = false;
		
		entities = new EntityManager(this, 3);
        
        spriteSheet = new Texture("characters.png");
        
        // load entities from the tile map
        EntityMapObjectParser entityParser = new EntityMapObjectParser();
        entityParser.setUnitScale(1f / Config.SPRITE_SCALE);
        entityParser.load(this, tileMap.getLayers().get("Entities"));
        
        //TODO: MAKE A START SCRIPT
        scripts.runScript("com.lostcodestudios.fools.scripts.Start");
	}
	
	public void dispose() {
		mapRenderer.dispose();
		tileMap.dispose();
		
		rayHandler.dispose();
		world.dispose();
		
		dialog.dispose();
		
		spriteBatch.dispose();
		spriteSheet.dispose();
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	/**
	 * Sets the camera position to center on the given tile.
	 * @param x
	 * @param y
	 */
	public void setCameraPosition(float x, float y) {
		float tileSize = Config.PIXELS_PER_METER;
		camera.position.set(x * tileSize + tileSize / 2, (bounds.height - y) * tileSize - tileSize / 2, 0);
		camera.update();
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void render(SpriteBatch spriteBatch, float delta) {
		if (!paused) {
			update(delta);
		}
		
		//NOTE: the passed spriteBatch must not use the camera's view. It keeps its own, with pure screen coordinates
		
		Matrix4 meterView = camera.combined.cpy().scl(Config.PIXELS_PER_METER);
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		this.spriteBatch.setProjectionMatrix(camera.combined);
		
		Rectangle cameraBounds = getCameraBounds();
		
		entities.render(delta, cameraBounds);
		
		rayHandler.setCombinedMatrix(meterView);
		rayHandler.updateAndRender();
		
		dialog.render(spriteBatch, this.spriteBatch, delta);
		
		if (Config.debug) {
			debugRenderer.render(world, meterView);
		}
	}

	public Rectangle getCameraBounds() {
		Rectangle cameraBounds = new Rectangle(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2,
				camera.viewportWidth, camera.viewportHeight);
		
		cameraBounds.x /= Config.PIXELS_PER_METER;
		cameraBounds.y /= Config.PIXELS_PER_METER;
		cameraBounds.width /= Config.PIXELS_PER_METER;
		cameraBounds.height /= Config.PIXELS_PER_METER;
		
		return cameraBounds;
	}

	private void update(float delta) {
		if (input.wasKeyPressed(Keys.ESCAPE)) {
			dialog.showDialogWindow("Paused");
		}
		
		scripts.update(delta);
		
		updatePhysics(delta);
		
		entities.update(delta);
	}

	public void updatePhysics(float delta) {
		elapsedTime += delta;
		
		while (elapsedTime >= TIME_STEP) {
			world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			elapsedTime -= TIME_STEP;
		}
		
		world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		elapsedTime = 0f;
	}
	
}
