package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import box2dLight.RayHandler;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.InputManager;
import com.lostcodestudios.fools.TextManager;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.EntityManager;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.gameplay.map.Box2DLightsMapObjectParser;
import com.lostcodestudios.fools.gameplay.map.Box2DMapObjectParser;
import com.lostcodestudios.fools.gameplay.map.EntityMapObjectParser;
import com.lostcodestudios.fools.gameplay.map.RoomMapObjectParser;
import com.lostcodestudios.fools.scripts.ai.AStar;

public class GameWorld {
	
	private static final float TIME_STEP = 1f / 60;
	private static final int VELOCITY_ITERATIONS = 6;
	private static final int POSITION_ITERATIONS = 2;
	
	public static int ASTARWORLD = 100;
	public static int ASTARSIZE = 1;
	public static float ASTARRECIP = 1f/1f;
	
	
	public OrthographicCamera camera;
	
	public TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	public World world;
	private CollisionManager collisionManager;
	private Array<Body> bodiesToDestroy = new Array<Body>();
	
	public RayHandler rayHandler;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public SpriteBatch spriteBatch = new SpriteBatch();
	
	public ShapeRenderer screenShapeRenderer = new ShapeRenderer();
	public ShapeRenderer worldShapeRenderer = new ShapeRenderer();
	
	public EventFlagManager flags = new EventFlagManager();
	public ScriptManager scripts = new ScriptManager(this);
	public DialogManager dialog = new DialogManager(this);
	public EntityManager entities;
	public InputManager input;
	public Texture spriteSheet;
	public Texture itemSheet;
	public Texture doorSpriteSheet;
	
	private TextureRegion potionRegion;
	
	public ObjectMap<String, Rectangle> rooms = new ObjectMap<String, Rectangle>();
	
	private float elapsedTime = 0f;
	
	private boolean paused;
	
	private Rectangle bounds;
	
	public boolean cutsceneMode = false;
	
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
		
		
      //BUILD A*
      AStar.objectMap = new Object[ASTARWORLD*ASTARWORLD];
      for(int x = 0; x < ASTARWORLD; x++)
         for(int y = 0; y < ASTARWORLD; y++)
         {
       	  //TODO: REMOVE DEPENDENCE ON 100.
       	  world.QueryAABB(new QueryCallback(){

					private int x;
					private int y;

					@Override
					public boolean reportFixture (Fixture fixture) {
						AStar.objectMap[y*ASTARWORLD + x] = new Object();
						return  true;
					}
					
					public QueryCallback yield(int x, int y){
						this.x = x; this.y = y;
						return this;
					}
       		  
       	  }.yield(x,y), ASTARRECIP*x, ASTARRECIP*y, ASTARRECIP*x+ASTARRECIP, ASTARRECIP*y+ASTARRECIP);
         }
      
		
		//load lighting effects from the tile map
		Box2DLightsMapObjectParser lightObjectParser = new Box2DLightsMapObjectParser();
		lightObjectParser.setUnitScale(1f / Config.SPRITE_SCALE);
		lightObjectParser.load(rayHandler, tileMap.getLayers().get("Lights"));
		lightObjectParser.load(rayHandler, (TiledMapTileLayer) tileMap.getLayers().get("Objects"));
		
		// load points and rooms from the tile map
		RoomMapObjectParser roomParser = new RoomMapObjectParser();
		roomParser.unitScale = 1f / Config.SPRITE_SCALE;
		roomParser.load(this, tileMap.getLayers().get("Rooms"));
		
		paused = false;
		
		entities = new EntityManager(this, 3);
        
        spriteSheet = new Texture("characters.png");
        itemSheet = new Texture("items.png");
        doorSpriteSheet = new Texture("doors.png");
        
        Rectangle potionRect = Config.itemSpriteInfo.get("Health Potion");
        potionRegion = new TextureRegion(itemSheet, (int)potionRect.x, (int)potionRect.y, (int)potionRect.width, (int)potionRect.height);
        
        // load entities from the tile map
        EntityMapObjectParser entityParser = new EntityMapObjectParser();
        entityParser.setUnitScale(1f / Config.SPRITE_SCALE);
        entityParser.loadPaths(tileMap.getLayers().get("Paths"));
        entityParser.load(this, tileMap.getLayers().get("Entities"));
        
	}
	
	public boolean started = false;
	
	public void start() {
		//TODO: MAKE A START SCRIPT
        scripts.runScript("Start");
	}
	
	public void dispose() {
		mapRenderer.dispose();
		tileMap.dispose();
		
		rayHandler.dispose();
		world.dispose();
		
		dialog.dispose();
		
		spriteBatch.dispose();
		spriteSheet.dispose();
		itemSheet.dispose();
		doorSpriteSheet.dispose();
		
		screenShapeRenderer.dispose();
		worldShapeRenderer.dispose();
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
		worldShapeRenderer.setProjectionMatrix(camera.combined);
		
		
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
		
		entities.renderHUD(delta, cameraBounds);
		
		if (!cutsceneMode) {
		
			renderHUD((Human) specialEntities.get("Fool"), spriteBatch);
		
		}
			
		dialog.render(spriteBatch, this.spriteBatch, delta);
		
		if (Config.debug && !paused) {
			debugRenderer.render(world, meterView);
			
			spriteBatch.begin();
			Vector2 positions = (specialEntities.get("Fool").getPosition());
			TextManager.draw(spriteBatch, "debug", "object map HERE!: " + AStar.objectMap[(int)(positions.x*ASTARSIZE) + (int)(positions.y*ASTARSIZE)*ASTARWORLD] , 0, 70);
			TextManager.draw(spriteBatch, "debug", "Player is in King's room: " + rooms.get("KingRoom").contains(specialEntities.get("Fool").getPosition()), 0, 60);
			spriteBatch.end();
		}
	}
	
	private void renderHUD(Human fool, SpriteBatch spriteBatch) {
		final float WEAPON_SLOT_X = 16;
		final float WEAPON_SLOT_Y = 16;
		final float WEAPON_SLOT_BORDER = 8;
		final float WEAPON_SLOT_SIZE = 80;
		final float HEALTH_BAR_X = 120;
		final float HEALTH_BAR_Y = 16;
		final float HEALTH_BAR_WIDTH = 256;
		final float HEALTH_BAR_HEIGHT = 32;
		final float HEALTH_BAR_BORDER = 8;
		final float POTIONS_X = 396;
		final float POTIONS_Y = 16;
		
		screenShapeRenderer.begin(ShapeType.Filled);
		screenShapeRenderer.setColor(Color.BLACK);
		screenShapeRenderer.rect(WEAPON_SLOT_X, WEAPON_SLOT_Y, 
				WEAPON_SLOT_SIZE + 2 * WEAPON_SLOT_BORDER, WEAPON_SLOT_BORDER);
		screenShapeRenderer.rect(WEAPON_SLOT_X, WEAPON_SLOT_Y, 
				WEAPON_SLOT_BORDER, WEAPON_SLOT_SIZE + 2 * WEAPON_SLOT_BORDER);
		screenShapeRenderer.rect(WEAPON_SLOT_X, WEAPON_SLOT_Y + WEAPON_SLOT_SIZE + WEAPON_SLOT_BORDER,
				WEAPON_SLOT_SIZE + 2 * WEAPON_SLOT_BORDER, WEAPON_SLOT_BORDER);
		screenShapeRenderer.rect(WEAPON_SLOT_X + WEAPON_SLOT_SIZE + WEAPON_SLOT_BORDER,
				WEAPON_SLOT_Y, WEAPON_SLOT_BORDER, WEAPON_SLOT_SIZE + 2 * WEAPON_SLOT_BORDER);
		
		screenShapeRenderer.setColor(Color.DARK_GRAY);
		screenShapeRenderer.rect(WEAPON_SLOT_X + WEAPON_SLOT_BORDER,
				WEAPON_SLOT_Y + WEAPON_SLOT_BORDER, WEAPON_SLOT_SIZE, WEAPON_SLOT_SIZE);
		
		screenShapeRenderer.setColor(Color.BLACK);
		screenShapeRenderer.rect(HEALTH_BAR_X, HEALTH_BAR_Y,
				HEALTH_BAR_WIDTH + 2 * HEALTH_BAR_BORDER, HEALTH_BAR_BORDER);
		screenShapeRenderer.rect(HEALTH_BAR_X, HEALTH_BAR_Y, 
				HEALTH_BAR_BORDER, HEALTH_BAR_HEIGHT + 2 * HEALTH_BAR_BORDER);
		screenShapeRenderer.rect(HEALTH_BAR_X, 
				HEALTH_BAR_Y + HEALTH_BAR_BORDER + HEALTH_BAR_HEIGHT, 
				HEALTH_BAR_WIDTH + 2 * HEALTH_BAR_BORDER, HEALTH_BAR_BORDER);
		screenShapeRenderer.rect(HEALTH_BAR_X + HEALTH_BAR_BORDER + HEALTH_BAR_WIDTH, 
				HEALTH_BAR_Y, HEALTH_BAR_BORDER, HEALTH_BAR_HEIGHT + 2 * HEALTH_BAR_BORDER);
		
		screenShapeRenderer.setColor(Color.RED);
		screenShapeRenderer.rect(HEALTH_BAR_X + HEALTH_BAR_BORDER, 
				HEALTH_BAR_Y + HEALTH_BAR_BORDER, 
				HEALTH_BAR_WIDTH * fool.healthFraction(), HEALTH_BAR_HEIGHT);
		
		screenShapeRenderer.end();
		
		spriteBatch.begin();
		
		if (fool.weapon != null) {

			fool.weapon.sprite.render(spriteBatch, 
					new Vector2(WEAPON_SLOT_X + WEAPON_SLOT_BORDER + WEAPON_SLOT_SIZE / 2,
							WEAPON_SLOT_Y + WEAPON_SLOT_BORDER + WEAPON_SLOT_SIZE / 2), 8f);
		}
		
		spriteBatch.draw(potionRegion, POTIONS_X, POTIONS_Y, 48f, 48f);
		TextManager.draw(spriteBatch, "ui-white", "" + fool.healthPotions, POTIONS_X + 56, POTIONS_Y + 24);
		
		spriteBatch.end();
	}

	public Rectangle getCameraBounds() {
		Rectangle cameraBounds = new Rectangle(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2,
				camera.viewportWidth, camera.viewportHeight);
		
		cameraBounds.x /= Config.PIXELS_PER_METER;
		cameraBounds.y /= Config.PIXELS_PER_METER;
		cameraBounds.width /= Config.PIXELS_PER_METER;
		cameraBounds.height /= Config.PIXELS_PER_METER;
		
		// add margins for the entity world
		final float MARGIN = 3; // 3 meters should be enough
		cameraBounds.x -= MARGIN;
		cameraBounds.width += MARGIN * 2;
		cameraBounds.y -= MARGIN;
		cameraBounds.height += MARGIN * 2;
		
		return cameraBounds;
	}

	private void update(float delta) {		
		if (input.wasKeyPressed(Keys.R)) {
			dialog.showInventory();
			return;
		}
		
		scripts.update(delta);
		
		updatePhysics(delta);
		collisionManager.update(delta); // this handles view sightings OUTSIDE of the dangerous ContactListener event context
		
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
		
		// destroy all bodies queued
		for (Body body : bodiesToDestroy) {
			world.destroyBody(body);
		}
		bodiesToDestroy.clear();
	}
	
	public void destroyBody(Body body) {
		bodiesToDestroy.add(body);
	}
	
	public Vector2 worldCursorPosition() {
		Vector2 worldCursorPosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		worldCursorPosition.add(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2);
		worldCursorPosition.scl(1f / Config.PIXELS_PER_METER);
		
		return worldCursorPosition;
	}
	
}
