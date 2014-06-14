package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;

import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.InputManager;
import com.lostcodestudios.fools.gameplay.entities.EntityManager;
import com.lostcodestudios.fools.gameplay.map.Box2DLightsMapObjectParser;
import com.lostcodestudios.fools.gameplay.map.Box2DMapObjectParser;

public class GameWorld {
	
	public OrthographicCamera camera;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private World world;
	private RayHandler rayHandler;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	private SpriteBatch spriteBatch = new SpriteBatch();
	
	public EventFlagManager flags = new EventFlagManager();
	public ScriptManager scripts = new ScriptManager(this);
	public DialogManager dialog = new DialogManager(this);
	public EntityManager entities = new EntityManager(this, 3);
	public InputManager input;
	
	private boolean paused;
	
	private Rectangle bounds;
	
	public GameWorld(InputManager input) {
		this.input = input;
		
		camera = new OrthographicCamera(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
		
		TmxMapLoader loader = new TmxMapLoader();
		tileMap = loader.load("castle.tmx");
		
		MapProperties mapProp = tileMap.getProperties();
		int width = (Integer) mapProp.get("width");
		int height = (Integer) mapProp.get("height");
		
		bounds = new Rectangle(0, 0, width, height);
		
		mapRenderer = new OrthogonalTiledMapRenderer(tileMap, Config.UNIT_SCALE);
		
		world = new World(new Vector2(), true);
		rayHandler = new RayHandler(world);
		
		//load physics bodies from the tile map
		Box2DMapObjectParser mapObjectParser = new Box2DMapObjectParser();
		mapObjectParser.setUnitScale(Config.UNIT_SCALE);
		mapObjectParser.load(world, tileMap.getLayers().get("Physics")); //NOTE: this won't load circle objects!
		
		//load lighting effects from the tile map
		Box2DLightsMapObjectParser lightObjectParser = new Box2DLightsMapObjectParser();
		lightObjectParser.setUnitScale(Config.UNIT_SCALE);
		lightObjectParser.load(rayHandler, tileMap.getLayers().get("Lights"));
		
		paused = false;
		
		

        scripts.runScript("scripts/start.groovy");
	}
	
	public void dispose() {
		mapRenderer.dispose();
		tileMap.dispose();
		
		rayHandler.dispose();
		world.dispose();
		
		spriteBatch.dispose();
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
		float tileSize = Config.UNIT_SCALE * Config.UNIT_SCALE;
		
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
		
		//NOTE: spriteBatch must not use the camera's view. It keeps its own, with pure screen coordinates
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.updateAndRender();
		
		this.spriteBatch.setProjectionMatrix(camera.combined);
		
		dialog.render(spriteBatch, this.spriteBatch, delta);
		
		if (Config.debug && !paused) { 
			debugRenderer.render(world, camera.combined);
		}
	}

	private void update(float delta) {
		if (input.wasKeyPressed(Keys.ESCAPE)) {
			dialog.showDialogWindow("Paused");
		}
		
		float cameraBoundWidth = Config.SCREEN_WIDTH / 6;
		float cameraBoundHeight = Config.SCREEN_HEIGHT / 6;
		
		Vector2 cameraAnchor = new Vector2(50 * 64, 94 * 64); //this will be the Fool's position eventually
		
		Rectangle cameraBounds = new Rectangle(
				cameraAnchor.x - cameraBoundWidth / 2, cameraAnchor.y - cameraBoundHeight / 2, cameraBoundWidth, cameraBoundHeight);
		
		float maxCameraSpeed = 512f;
		float maxCenterDistance = 180f;
		
		Vector2 cameraMovement = new Vector2();
		
		Vector2 centerScreen = new Vector2(Config.SCREEN_WIDTH / 2, Config.SCREEN_HEIGHT / 2);
		Vector2 mousePos = new Vector2(Gdx.input.getX(), Config.SCREEN_HEIGHT - Gdx.input.getY());
		Vector2 mouseCenterOffset = mousePos.cpy().sub(centerScreen);
		
		float cameraSpeed = (mouseCenterOffset.len() / maxCenterDistance) * maxCameraSpeed;
		
		cameraMovement = mouseCenterOffset.cpy().nor().scl(cameraSpeed * delta);
		
		camera.translate(cameraMovement);
		
		if (camera.position.x < cameraBounds.x) {
			camera.position.x = cameraBounds.x;
		} else if (camera.position.x > cameraBounds.x + cameraBounds.width) {
			camera.position.x = cameraBounds.x + cameraBounds.width;
		}
		
		if (camera.position.y < cameraBounds.y) {
			camera.position.y = cameraBounds.y;
		} else if (camera.position.y > cameraBounds.y + cameraBounds.height) {
			camera.position.y = cameraBounds.y + cameraBounds.height;
		}
		
		camera.update();
	}
	
}
