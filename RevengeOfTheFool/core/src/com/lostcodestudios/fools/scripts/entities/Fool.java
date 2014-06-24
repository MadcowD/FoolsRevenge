package com.lostcodestudios.fools.scripts.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.scripts.Script;

public class Fool extends Script {

	private static final float FOOL_SPEED = 5f;
	
	private static final float MAX_CAMERA_SPEED_MOUSE = 512f;
	private static final float MAX_SCROLL_CENTER_DISTANCE = 180f;
	
	private static final float CAMERA_BOUNDS_WIDTH = Config.SCREEN_WIDTH / 6;
	private static final float CAMERA_BOUNDS_HEIGHT = Config.SCREEN_HEIGHT / 4;
	
	@Override
	public void run(GameWorld gameWorld, ObjectMap<String, Object> args) {
		
		
		if (!gameWorld.cutsceneMode) {
		
			Human e = (Human)args.get("e");
			float delta = (Float)args.get("deltaTime");
			OrthographicCamera camera = gameWorld.camera;
			
			// move the Fool with WASD
			
			Vector2 velocity = new Vector2();
			
			if (gameWorld.input.isKeyHeld(Keys.A)) {
				velocity.x = -1;
			} else if (gameWorld.input.isKeyHeld(Keys.D)) {
				velocity.x = 1;
			}
	
			if (gameWorld.input.isKeyHeld(Keys.W)) {
				velocity.y = 1;
			} else if (gameWorld.input.isKeyHeld(Keys.S)) {
				velocity.y = -1;
			}
	
			velocity.nor();
	
			velocity.scl(FOOL_SPEED);
	
			e.setVelocity(velocity);
			
			// drink potions with Q
			if (gameWorld.input.wasKeyPressed(Keys.Q)) {
				e.drinkHealthPotion();
			}
			
			// control the camera with the mouse
			
			Vector2 cameraAnchor = e.getPosition().cpy().scl(Config.PIXELS_PER_METER); //this will be the Fool's position eventually
			
			Rectangle cameraBounds = new Rectangle(
					cameraAnchor.x - CAMERA_BOUNDS_WIDTH / 2, cameraAnchor.y - CAMERA_BOUNDS_HEIGHT / 2, CAMERA_BOUNDS_WIDTH, CAMERA_BOUNDS_HEIGHT);
			
			Vector2 cameraMovement = new Vector2();
			
			Vector2 centerScreen = new Vector2(Config.SCREEN_WIDTH / 2, Config.SCREEN_HEIGHT / 2);
			Vector2 mousePos = new Vector2(Gdx.input.getX(), Config.SCREEN_HEIGHT - Gdx.input.getY());
			Vector2 mouseCenterOffset = mousePos.cpy().sub(centerScreen);
			
			float cameraSpeed = (mouseCenterOffset.len() / MAX_SCROLL_CENTER_DISTANCE) * MAX_CAMERA_SPEED_MOUSE;
			
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
	
	static{
		Script.Register(new Fool());
	}
}
