package com.lostcodestudios.fools.gameplay.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class PointMapObjectParser {

	public float unitScale = 1f;
	
	public void load(GameWorld world, MapLayer layer) {
		
		MapObjects objects = layer.getObjects();
		
		for (MapObject object : objects) {
			MapProperties properties = object.getProperties();
			
			String name = object.getName();
			String type = (String)properties.get("type");
			
			RectangleMapObject rectObj = (RectangleMapObject) object;
			
			Rectangle rect = rectObj.getRectangle();
			
			rect.x *= unitScale;
			rect.y *= unitScale;
			rect.width *= unitScale;
			rect.height *= unitScale;
			
			float x = rect.x + rect.width / 2;
			float y = rect.y + rect.height / 2;
			
			Vector2 position = new Vector2(x, y);
			
			if (type.equals("Room")) {
				world.rooms.put(name, rect);
			} else if (type.equals("Point")) {
				world.points.put(name, position);
			}
		}
		
	}
	
}
