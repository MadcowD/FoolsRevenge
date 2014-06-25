package com.lostcodestudios.fools.gameplay.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Door;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.gameplay.entities.Item;
import com.lostcodestudios.fools.gameplay.entities.Switch;
import com.lostcodestudios.fools.gameplay.entities.Weapon;

public class EntityMapObjectParser {

	private float unitScale = 1f;
	
	public void setUnitScale(float unitScale) {
		this.unitScale = unitScale;
	}
	
	/**
	 * SPAGHETTTI!
	 */
	ObjectMap<String, ObjectMap<String, Object>> paths = new ObjectMap<String, ObjectMap<String, Object>>();
	
	/**
	 * Parses default AI paths.
	 * @param layer 
	 */
	public void loadPaths(MapLayer layer){
		for(MapObject obj : layer.getObjects()){
			if(obj instanceof PolylineMapObject){
				String ent = (String)obj.getProperties().get("entity");
				String type = (String) obj.getProperties().get("type");
				PolylineMapObject pl = ((PolylineMapObject)obj);
				if(!paths.containsKey(ent)){
					ObjectMap<String, Object> entPaths = new ObjectMap<String, Object>();
					Polyline pla = pl.getPolyline();
					pla.setScale(unitScale, unitScale);
					pla.setPosition(pla.getX()*unitScale, pla.getY()*unitScale);
					entPaths.put(type, pl); 
					paths.put(ent,entPaths);
				}
				else
					paths.get(ent).put(type,pl);
			}
		}
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
			
			
			ObjectMap<String, Object> args = new ObjectMap<String, Object>();
			
			if(name != null)
				if(paths.containsKey(name))
					args.put("paths", paths.get(name));
			
			if (type.equals("Fool")) {
				e = new Human(world, "Fool", position, "entities.Fool", args);
				((Human) e).group = "Fool";
			}
			
			else if (type.equals("King")) {
				e = new Human(world, "King", position, "entities.King", args);
				((Human) e).group = "King";
			}
			
			else if (type.equals("Guard")) {
				e = new Human(world, "Guard", position, "entities.Guard", args);
				((Human) e).group = "King";
				
				Weapon sword = new Weapon(world, e, "Sword");
				
				world.entities.add(sword);
			}
			
			else if (type.equals("Door")) {
				String doorType = (String) entityProperties.get("doorType");
				
				float width = rectObj.getRectangle().width;
				float height = rectObj.getRectangle().height;
				
				float size = 0f;
				if (width > height) {
					size = width * unitScale;
				} else {
					size = height * unitScale;
				}
				
				doorType = (size == 2f ? "Average " : "Large ") + doorType;
				
				doorType += (width > height ? " Horizontal" : " Vertical");
				
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
			
			else if (type.equals("Weapon")) {
				String weaponType = (String)entityProperties.get("weaponType");
				e = new Weapon(world, position, weaponType);
			}
			
			else if (type.equals("Switch")) {
				String script = (String)entityProperties.get("triggerScript");
				e = new Switch(world, position, script);
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
				
				if (e instanceof Human) {
					((Human)e).tag = name;
				}
			}
		}
	}
	
}
