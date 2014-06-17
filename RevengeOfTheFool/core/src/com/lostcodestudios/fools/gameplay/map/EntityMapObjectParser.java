package com.lostcodestudios.fools.gameplay.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class EntityMapObjectParser {

	private float unitScale = 1f;
	
	public void setUnitScale(float unitScale) {
		this.unitScale = unitScale;
	}
	
	public void load(GameWorld world, MapLayer layer) {
		MapObjects entityObjects = layer.getObjects();
		
		for (MapObject entityObject : entityObjects) {
			MapProperties entityProperties = entityObject.getProperties();
			
			String type = (String) entityProperties.get("type");
			String name = entityObject.getName();
			
			RectangleMapObject rectObj = (RectangleMapObject) entityObject;
			
			Rectangle rect = rectObj.getRectangle();
			
			float x = rect.x + rect.width / 2;
			float y = rect.y + rect.height / 2;
			
			Vector2 position = new Vector2(x, y).scl(unitScale);
			
			Entity e = null;
			
			if (type.equals("Fool")) {
				e = new Human(world, "Fool", position, "com.lostcodestudios.fools.scripts.Fool", null);
			}
			
			// TODO load other types of entities
			
			world.entities.add(e);
			
			if (name != null && !name.isEmpty()) {
				world.specialEntities.put(name, e); // save the special ones for later
			}
		}
	}
	
}
