package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;

import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.gameplay.map.Box2DLightsMapObjectParser;
import com.lostcodestudios.fools.gameplay.map.Box2DMapObjectParser;

public class EntityWorld {

	//world will contain event flags, tile map, map bodies, Box2dlights, entities, items, etc.
	//it will also load and run Groovy scripts, giving them access to its scope so they can access flags, manipulate entities, etc
	
	private OrthographicCamera camera;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private World world;
	private RayHandler rayHandler;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public EventFlagManager flags = new EventFlagManager();
	public ScriptManager scripts = new ScriptManager(this);
	public DialogManager dialog = new DialogManager(this);
	
	private boolean paused;
	
	public EntityWorld() {
		camera = new OrthographicCamera(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
		
		TmxMapLoader loader = new TmxMapLoader();
		tileMap = loader.load("testmap.tmx");
		
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
	}
	
	public void pause() {
		paused = true;
	}
	
	public void resume() {
		paused = false;
	}
	
	/**
	 * Sets the camera position to center on the given tile.
	 * @param x
	 * @param y
	 */
	public void setCameraPosition(float x, float y) {
		float tileSize = Config.UNIT_SCALE * Config.UNIT_SCALE;
		
		camera.position.set(x * tileSize + tileSize / 2, y * tileSize - tileSize / 2, 0);
		camera.update();
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
		
		dialog.render(spriteBatch, delta);
		
		if (Config.DEBUG && !paused) { 
			debugRenderer.render(world, camera.combined);
		}
	}

	private void update(float delta) {
		float cameraBoundWidth = Config.SCREEN_WIDTH / 6;
		float cameraBoundHeight = Config.SCREEN_HEIGHT / 6;
		
		Vector2 cameraAnchor = new Vector2(31 * 64, 12 * 64); //this will be the Fool's position eventually
		
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
