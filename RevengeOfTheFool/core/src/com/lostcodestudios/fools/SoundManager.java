package com.lostcodestudios.fools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public final class SoundManager {
	
	private static final String SOUND_PREFS_KEY = "TheFoolsRevenge_SOUNDPREFS";
	
	private static final float HEARING_RADIUS = 20f; // hear faintly from 20 tiles away
	private static final float HEARING_COEFFICIENT = 1/(float)Math.sqrt(HEARING_RADIUS);
	private static ObjectMap<String, Sound> sounds = new ObjectMap<String, Sound>();
	private static ObjectMap<String, Music> music = new ObjectMap<String, Music>();
	
	private static float soundVolume;
	private static float musicVolume;
	
	public static void init() {
		Preferences prefs = Gdx.app.getPreferences(SOUND_PREFS_KEY);
		
		soundVolume = prefs.getFloat("soundVolume", 1f);
		musicVolume = prefs.getFloat("musicVolume", 1f);
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
			float fraction = song.getVolume() / musicVolume;
			
			song.setVolume(volume * fraction); // update the volume of all music currently playing, but preserve its ratio to the old volume
		}
	}
	
	public static void loadSound(String key, String path) {
		sounds.put(key, Gdx.audio.newSound(Gdx.files.internal(path)));
	}
	
	public static void loadMusic(String key, String path) {
		music.put(key, Gdx.audio.newMusic(Gdx.files.internal(path)));
	}
	
	public static void playSound(String key) {
		sounds.get(key).play(soundVolume);
	}
	
	public static void playSound(String key, Vector2 soundPos, Vector2 playerPos) {
		// handle panning and sound volume
		
		float dist = soundPos.cpy().sub(playerPos).len();
		
		float vol = 1/(dist*HEARING_COEFFICIENT + 1);
		
		float xOffset = soundPos.x - playerPos.x;
		float panning = Math.min(Math.max(xOffset / HEARING_RADIUS, -1), 1);
		
		sounds.get(key).play(soundVolume * vol, 1, panning);
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
