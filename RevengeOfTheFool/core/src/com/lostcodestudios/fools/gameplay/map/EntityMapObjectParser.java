package com.lostcodestudios.fools.gameplay.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Door;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.gameplay.entities.Item;
import com.lostcodestudios.fools.gameplay.entities.Weapon;

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
				((Human) e).group = "Fool";
				((Human) e).tag = "Fool";
				
				
				Weapon sword = new Weapon(world, e, "Sword");
				sword.meleeDamage = 2.5f;
				
				((Human) e).weapon = sword;
				
				world.entities.add(sword);
			}
			
			else if (type.equals("King")) {
				e = new Human(world, "King", position, "com.lostcodestudios.fools.scripts.King", null);
				((Human) e).group = "King";
			}
			
			else if (type.equals("Guard")) {
				e = new Human(world, "Guard", position, "com.lostcodestudios.fools.scripts.Guard", null);
				((Human) e).group = "King";
				
				Weapon sword = new Weapon(world, e, "Sword");
				sword.meleeDamage = 2.5f;
				
				((Human) e).weapon = sword;
				
				world.entities.add(sword);
			}
			
			else if (type.equals("Door")) {
				String doorType = (String) entityProperties.get("doorType");
				e = new Door(world, doorType, position);
				
				if (entityProperties.get("key") != null) {
					String key = (String) entityProperties.get("key");
					((Door)e).key = key;
				}
			}
			
			else if (type.equals("Item")){
				String itemType = (String)entityProperties.get("itemType");
				e = new Item(world, position, itemType);
			}
			
			if (e instanceof Human) {
				// rotate the body for a human
				float radians = 0f;
				String rotation = entityProperties.get("rotation", String.class);
				if (rotation != null) {
					radians = (float) Math.toRadians(Float.parseFloat(rotation));
				}
				((Human) e).body.setTransform(((Human) e).body.getPosition(), radians);
				
				// then add an item if it has one
				String itemType = (String) entityProperties.get("item");
				if (itemType != null && !itemType.isEmpty()) {
					Item i = new Item(world, e, itemType);
					world.entities.add(i);
				}
			}
			
			world.entities.add(e);
			
			if (name != null && !name.isEmpty()) {
				world.specialEntities.put(name, e); // save the special ones for later
			}
		}
	}
	
}
