package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.RayHandler;

import com.lostcodestudios.fools.Revenge;
import com.lostcodestudios.fools.gameplay.map.Box2DLightsMapObjectParser;
import com.lostcodestudios.fools.gameplay.map.Box2DMapObjectParser;

public class EntityWorld {

	//world will contain event flags, tile map, map bodies, Box2dlights, entities, items, etc.
	//it will also load and run Groovy scripts, giving them access to its scope so they can access flags, manipulate entities, etc
	
	public static final float UNIT_SCALE = 8f;
	
	private OrthographicCamera camera;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private World world;
	private RayHandler rayHandler;
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public EventFlagManager flags = new EventFlagManager();
	public ScriptManager scripts = new ScriptManager(this);
	
	public EntityWorld() {
		camera = new OrthographicCamera(Revenge.SCREEN_WIDTH, Revenge.SCREEN_HEIGHT);
		
		TmxMapLoader loader = new TmxMapLoader();
		tileMap = loader.load("testmap.tmx");
		
		mapRenderer = new OrthogonalTiledMapRenderer(tileMap, UNIT_SCALE);
		
		world = new World(new Vector2(), true);
		rayHandler = new RayHandler(world);
		
		//load physics bodies from the tile map
		Box2DMapObjectParser mapObjectParser = new Box2DMapObjectParser();
		mapObjectParser.setUnitScale(UNIT_SCALE);
		mapObjectParser.load(world, tileMap.getLayers().get("Physics")); //NOTE: this won't load circle objects!
		
		//load lighting effects from the tile map
		Box2DLightsMapObjectParser lightObjectParser = new Box2DLightsMapObjectParser();
		lightObjectParser.setUnitScale(UNIT_SCALE);
		lightObjectParser.load(rayHandler, tileMap.getLayers().get("Lights"));
	}
	
	public void dispose() {
		mapRenderer.dispose();
		tileMap.dispose();
		
		rayHandler.dispose();
		world.dispose();
	}
	
	public void render(float delta) {
		float cameraSpeed = 512f;
		
		Vector2 cameraMovement = new Vector2();
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			cameraMovement.x = -1;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			cameraMovement.x = 1;
		}
		
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			cameraMovement.y = -1;
		} else if (Gdx.input.isKeyPressed(Keys.UP)) {
			cameraMovement.y = 1;
		}
		
		cameraMovement.nor();
		cameraMovement.scl(cameraSpeed * delta);
		camera.translate(cameraMovement);
		
		camera.update();
		
		mapRenderer.setView(camera);
		mapRenderer.render();
		
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.updateAndRender();
		
		debugRenderer.render(world, camera.combined);
	}
	
}
