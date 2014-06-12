package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.lostcodestudios.fools.Revenge;

public class EntityWorld {

	//world will contain event flags, tile map, map bodies, Box2dlights, entities, items, etc.
	//it will also load and run Groovy scripts, giving them access to its scope so they can access flags, manipulate entities, etc
	
	public static final float UNIT_SCALE = 8f;
	
	private OrthographicCamera camera;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	private World world;
	private RayHandler rayHandler;
	private Box2DDebugRenderer debugRenderer;
	
	public EntityWorld() {
		camera = new OrthographicCamera(Revenge.SCREEN_WIDTH, Revenge.SCREEN_HEIGHT);
		
		TmxMapLoader loader = new TmxMapLoader();
		tileMap = loader.load("testmap.tmx");
		
		mapRenderer = new OrthogonalTiledMapRenderer(tileMap, UNIT_SCALE);
		
		world = new World(new Vector2(), true);
		rayHandler = new RayHandler(world);
		debugRenderer = new Box2DDebugRenderer();
		
		loadPhysics();
		loadLights();
	}

	private void loadPhysics() {
		// TODO Auto-generated method stub
		
	}
	
	private void loadLights() {
		MapLayer lightsLayer = tileMap.getLayers().get("Lights");
		
		String ambientColorStr = lightsLayer.getProperties().get("ambientColor", String.class);
		
		if (ambientColorStr != null) {
			rayHandler.setAmbientLight(parseColor(ambientColorStr));
		}
		
		MapObjects lightObjects = lightsLayer.getObjects();
		
		for (MapObject lightObject : lightObjects) {
			MapProperties lightProperties = lightObject.getProperties();
			
			String type = lightProperties.get("type", String.class);
			
			if (type == null) continue;
			
			float x = UNIT_SCALE * lightProperties.get("x", Float.class);
			float y = UNIT_SCALE * lightProperties.get("y", Float.class);
			
			int rays = Integer.parseInt(lightProperties.get("rays", String.class));
			
			Color color = parseColor(lightProperties.get("color", String.class));
			
			float distance = Float.parseFloat(lightProperties.get("distance", String.class));
			
			if (type.equals("Point")) {
				new PointLight(rayHandler, rays, color, distance, x, y);
			}
			else if (type.equals("Cone")) {
				float rotation = Float.parseFloat(lightProperties.get("rotation", String.class));
				float coneDegrees = Float.parseFloat(lightProperties.get("coneDegrees", String.class));
				
				new ConeLight(rayHandler, rays, color, distance, x, y, rotation, coneDegrees);
			}
		}
	}
	
	private Color parseColor(String colorStr) {
		String[] components = colorStr.split(", ");
		float r = (float)Integer.parseInt(components[0]) / 255f;
		float g = (float)Integer.parseInt(components[1]) / 255f;
		float b = (float)Integer.parseInt(components[2]) / 255f;
		float a = (float)Integer.parseInt(components[3]) / 255f;
		
		return new Color(r, g, b, a);
	}
	
	public void dispose() {
		mapRenderer.dispose();
		tileMap.dispose();
		
		rayHandler.dispose();
		world.dispose();
	}
	
	public void render(float delta) {
		float cameraSpeed = 256f;
		
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
