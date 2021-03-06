package com.lostcodestudios.fools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;

public final class SoundManager {

	private static final String SOUND_PREFS_KEY = "TheFoolsRevenge_SOUNDPREFS";

	private static final float HEARING_RADIUS = 30f; // hear faintly from 20 tiles away
	private static final float HEARING_COEFFICIENT = 1f/(float)Math.sqrt(HEARING_RADIUS);
	private static ObjectMap<String, Sound> sounds = new ObjectMap<String, Sound>();

	private static ObjectMap<String, Array<Sound>> soundGroups = new ObjectMap<String, Array<Sound>>();

	private static ObjectMap<String, Music> music = new ObjectMap<String, Music>();

	private static float soundVolume;
	private static float musicVolume;

	public static void init() {
		Preferences prefs = Gdx.app.getPreferences(SOUND_PREFS_KEY);

		setSoundVolume(prefs.getFloat("soundVolume", 1f));
		setMusicVolume(prefs.getFloat("musicVolume", 1f));
	}

	public static float getSoundVolume() {
		return soundVolume;
	}

	public static float getMusicVolume() {
		return musicVolume;
	}

	public static void setSoundVolume(float volume) {
		soundVolume = volume;

		Preferences prefs = Gdx.app.getPreferences(SOUND_PREFS_KEY);
		prefs.putFloat("soundVolume", volume);
		prefs.flush();
	}

	public static void setMusicVolume(float volume) {
		musicVolume = volume;

		Preferences prefs = Gdx.app.getPreferences(SOUND_PREFS_KEY);
		prefs.putFloat("musicVolume", volume);
		prefs.flush();

		for (Music song : music.values()) {
			song.setVolume(volume); // update the volume of all music currently playing, but preserve its ratio to the old volume
		}
	}

	public static void loadSound(String key, String path) {
		sounds.put(key, Gdx.audio.newSound(Gdx.files.internal(path)));
	}

	public static void loadGroupSound(String key, String path) {
		Array<Sound> group = null;
		if (soundGroups.containsKey(key)) {
			group = soundGroups.get(key);
		}

		else {
			group = new Array<Sound>();
			soundGroups.put(key, group);
		}

		group.add(Gdx.audio.newSound(Gdx.files.internal(path)));
	}

	public static void loadMusic(String key, String path) {
		music.put(key, Gdx.audio.newMusic(Gdx.files.internal(path)));
	}

	public static void playSound(String key) {
		sounds.get(key).play(soundVolume);
	}

	public static void playSound(String key, Vector2 soundPos, Vector2 playerPos) {
		// handle panning and sound volume

		float dist = soundPos.cpy().sub(playerPos).len2();

		float vol = 1/(dist*HEARING_COEFFICIENT + 1);

		float xOffset = soundPos.x - playerPos.x;
		float panning = Math.min(Math.max(xOffset / HEARING_RADIUS, -1), 1);

		sounds.get(key).play(soundVolume * vol, 1, panning);
	}

	public static void playSoundGroup(String key) {
		Array<Sound> group = soundGroups.get(key);

		int n = (int) (group.size * Random.random());
		
		group.get(n).play(soundVolume);
	}
	
	public static void playSoundGroup(String key, Vector2 position, Vector2 playerPos) {
		Array<Sound> group = soundGroups.get(key);

		int n = (int) (group.size * Random.random());

		float dist = position.cpy().sub(playerPos).len2();

		float vol = 1f/((dist*HEARING_COEFFICIENT + 1f));

		float xOffset = position.x - playerPos.x;
		float panning = Math.min(Math.max(xOffset / HEARING_RADIUS, -1), 1);

		group.get(n).play(soundVolume * vol, 1, panning);

	}

	public static void playFootstep(Vector2 position, Vector2 playerPos, GameWorld gameWorld) {
		TiledMap map = gameWorld.tileMap;
		TiledMapTileLayer floorLayer = (TiledMapTileLayer) map.getLayers().get("Floor");
		String floorType;
		try{
			floorType = (String)floorLayer.getCell((int)position.x, (int)position.y).getTile().getProperties().get("floorType");
			playSoundGroup("snd_step_" + floorType, position, playerPos);
		}
		catch(Exception e){
			//gameWorld.camera.position.set(new Vector3(position.cpy().scl(64), 0));
			
			//System.out.println("Asd");
		}

	}

	public static void playMusic(String key) {
		music.get(key).setVolume(musicVolume);
		music.get(key).setLooping(true);
		music.get(key).play();
	}

	public static void setMusicVolume(String key, float volScale) {
		music.get(key).setVolume(musicVolume * volScale);
	}

}
