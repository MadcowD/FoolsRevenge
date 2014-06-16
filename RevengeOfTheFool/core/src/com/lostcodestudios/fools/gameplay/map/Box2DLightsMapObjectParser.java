package com.lostcodestudios.fools.gameplay.map;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Ellipse;

public class Box2DLightsMapObjectParser {
	
	private float unitScale = 1f;
	
	public void setUnitScale(float unitScale) {
		this.unitScale = unitScale;
	}
	
	public void load(RayHandler rayHandler, MapLayer mapLayer) {
		String ambientColorStr = mapLayer.getProperties().get("ambientColor", String.class);
		
		if (ambientColorStr != null) {
			rayHandler.setAmbientLight(parseColor(ambientColorStr));
		}
		
		MapObjects lightObjects = mapLayer.getObjects();
		
		for (MapObject lightObject : lightObjects) {
			Ellipse ellipse = ((EllipseMapObject) lightObject).getEllipse();
			MapProperties lightProperties = lightObject.getProperties();
			
			String type = lightProperties.get("type", String.class);
			
			if (type == null) continue;
			
			float x = unitScale * (ellipse.x + ellipse.width / 2);
			float y = unitScale * (ellipse.y + ellipse.height / 2);
			
			//lights defined by preset
			if (type.equals("Torch")) {
				new PointLight(rayHandler, 180, 
						new Color(255f / 255, 123f / 255, 0f / 255, 200f / 255), 64 * 5, x, y);
				
				continue;
			}
			
			
			//lights fully defined from map
			
			//average the ellipse's dimensions to find a "radius"
			float distance = (ellipse.width + ellipse.height) * unitScale / 4;
			
			int rays = Integer.parseInt(lightProperties.get("rays", String.class));
			
			Color color = parseColor(lightProperties.get("color", String.class));
			
			if (type.equals("Point")) {
				new PointLight(rayHandler, rays, color, distance, x, y);
			}
			
			if (type.equals("Cone")) {
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
	
}
