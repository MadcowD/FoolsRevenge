package com.lostcodestudios.fools;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;

public final class Config {
	
    public static class AnimatedSpriteInfo {
        public int frameX, frameY, frameWidth, frameHeight;

        public AnimatedSpriteInfo(int frameX, int frameY, int frameWidth, int frameHeight) {
            this.frameX = frameX;
            this.frameY = frameY;
            this.frameWidth = frameWidth;
            this.frameHeight = frameHeight;
        }
    }
    
	public static boolean debug = true;
	
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;
	
	public static final float SPRITE_SCALE = 8f;
	public static final float PIXELS_PER_METER = 64f;
	
	public static final float STAIR_SPEED = 0.6f;
	
	public static final int ACCEPT_KEY = Keys.E;
	public static final String ACCEPT_TEXT = "[E]";
	
	public static final String UI_FONT_KEY = "ui-black";
	
    public static final float DIALOG_WINDOW_ALPHA = 0.75f;
	public static final Color DIALOG_WINDOW_COLOR_1 = new Color(0.55f, 0.55f, 0.55f, DIALOG_WINDOW_ALPHA);
    public static final Color DIALOG_WINDOW_COLOR_2 = new Color(0.71f, 0.71f, 0.71f, DIALOG_WINDOW_ALPHA);
    public static final Color DIALOG_WINDOW_COLOR_3 = new Color(0.77f, 0.77f, 0.77f, DIALOG_WINDOW_ALPHA);
    public static final Color DIALOG_WINDOW_COLOR_4 = new Color(0.61f, 0.61f, 0.61f, DIALOG_WINDOW_ALPHA);

    public static final Color DIALOG_BORDER_COLOR = Color.BLACK;
	
	public static void loadFonts() {
		TextManager.addFont("debug", "fonts/Oswald-Regular.ttf", 14, Color.WHITE);
		
		TextManager.addFont("dialog-black", "fonts/Philosopher-BoldItalic.ttf", 24, Color.BLACK);
		TextManager.addFont("dialog-white", "fonts/Philosopher-BoldItalic.ttf", 24, Color.WHITE);
		
		TextManager.addFont("voice", "fonts/Philosopher-Bold.ttf", 24, Color.BLACK);
		
		TextManager.addFont("ui-black", "fonts/Oswald-Regular.ttf", 24, Color.BLACK);
		TextManager.addFont("ui-white", "fonts/Oswald-Regular.ttf", 24, Color.WHITE);
		
		TextManager.addFont("menu-title", "fonts/Philosopher-BoldItalic.ttf", 72, Color.RED);
		TextManager.addFont("menu-entry", "fonts/Philosopher-Regular.ttf", 48, Color.WHITE);
		TextManager.addFont("menu-entry-selected", "fonts/Philosopher-Italic.ttf", 48, Color.RED);
		TextManager.addFont("menu-label", "fonts/Philosopher-Italic.ttf", 24, Color.WHITE);
	}
	
    public static ObjectMap<String, AnimatedSpriteInfo> spriteInfo = new ObjectMap<String, AnimatedSpriteInfo>();
    public static ObjectMap<String, Rectangle> corpseSpriteInfo = new ObjectMap<String, Rectangle>();

    public static void loadSpriteInfo() {
        spriteInfo.put("Fool", new AnimatedSpriteInfo(1, 1, 9, 11));
        spriteInfo.put("King", new AnimatedSpriteInfo(31, 1, 7, 8));
        spriteInfo.put("Guard", new AnimatedSpriteInfo(55, 1, 8, 8));
        
        corpseSpriteInfo.put("Blood", new Rectangle(119, 1, 8, 8));
        corpseSpriteInfo.put("Fool", new Rectangle(1, 49, 9, 6));
        corpseSpriteInfo.put("King", new Rectangle(31, 37, 8, 5));
        corpseSpriteInfo.put("Guard", new Rectangle(55, 37, 9, 7));
    }
    
    public static ObjectMap<String, Rectangle> itemSpriteInfo = new ObjectMap<String, Rectangle>();
    
    public static void loadItemSpriteInfo() {
    	itemSpriteInfo.put("Gold Key", new Rectangle(1, 1, 4, 8));
    	itemSpriteInfo.put("Silver Key", new Rectangle(6, 1, 4, 8));
    	itemSpriteInfo.put("Brass Key", new Rectangle(11, 1, 4, 8));
    	itemSpriteInfo.put("Sword", new Rectangle(16, 1, 8, 8));
    	itemSpriteInfo.put("Health Potion", new Rectangle(1, 10, 6, 6));
    }
    
    public static ObjectMap<String, Rectangle[]> doorSpriteInfo = new ObjectMap<String, Rectangle[]>();
    
    public static void loadDoorSpriteInfo() {
    	doorSpriteInfo.put("Large Wood Horizontal", 
    			new Rectangle[] { new Rectangle(0, 0, 24, 13), new Rectangle(25, 0, 24, 13) });
    	
    	doorSpriteInfo.put("Large Wood Vertical", 
    			new Rectangle[] { new Rectangle(51, 0, 12, 25), new Rectangle(64, 0, 12, 25) });
    	
    	doorSpriteInfo.put("Average Wood Horizontal",
    			new Rectangle[] { new Rectangle(0, 16, 16, 8), new Rectangle(17, 16, 16, 8) });
    	
    	doorSpriteInfo.put("Average Wood Vertical",
    			new Rectangle[] { new Rectangle(78, 0, 8, 17), new Rectangle(87, 0, 8, 17) });
    }
    
    public static final Rectangle switchSpriteFrame1 = new Rectangle(0, 29, 8, 8);
    public static final Rectangle switchSpriteFrame2 = new Rectangle(8, 29, 8, 8);
    
	public static void loadAll() {
		loadSounds();
		loadFonts();
		loadSpriteInfo();
		loadItemSpriteInfo();
		loadDoorSpriteInfo();
	}
	
	public static float INTERACT_DISTANCE = 3f;
    
	public static void loadSounds() {
		SoundManager.loadGroupSound("snd_step_stone", "sounds/snd_walkingconcrete1.wav");
		SoundManager.loadGroupSound("snd_step_stone", "sounds/snd_walkingconcrete2.wav");
		SoundManager.loadGroupSound("snd_step_stone", "sounds/snd_walkingconcrete3.wav");
		SoundManager.loadGroupSound("snd_step_grass", "sounds/snd_walkingdirt1.wav");
		SoundManager.loadGroupSound("snd_step_carpet", "sounds/snd_walkingrug1.wav");
	}
	
}
