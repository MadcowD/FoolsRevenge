package com.lostcodestudios.fools.gameplay.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class RoomMapObjectParser {

	public float unitScale = 1f;
	
	public void load(GameWorld world, MapLayer layer) {
		
		MapObjects objects = layer.getObjects();
		
		for (MapObject object : objects) {
			String name = object.getName();
			
			RectangleMapObject rectObj = (RectangleMapObject) object;
			
			Rectangle rect = rectObj.getRectangle();
			
			rect.x *= unitScale;
			rect.y *= unitScale;
			rect.width *= unitScale;
			rect.height *= unitScale;
			
			world.rooms.put(name, rect);
		}
		
	}
	
}
