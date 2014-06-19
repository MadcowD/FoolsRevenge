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
	}

    public static ObjectMap<String, AnimatedSpriteInfo> spriteInfo = new ObjectMap<String, AnimatedSpriteInfo>();

    public static void loadSpriteInfo() {
        spriteInfo.put("Fool", new AnimatedSpriteInfo(1, 1, 9, 11));
        spriteInfo.put("King", new AnimatedSpriteInfo(31, 1, 7, 8));
        spriteInfo.put("Guard", new AnimatedSpriteInfo(55, 1, 8, 8));
    }
    
    public static ObjectMap<String, Rectangle> itemSpriteInfo = new ObjectMap<String, Rectangle>();
    
    public static void loadItemSpriteInfo() {
    	itemSpriteInfo.put("Gold Key", new Rectangle(1, 1, 4, 8));
    	itemSpriteInfo.put("Silver Key", new Rectangle(6, 1, 4, 8));
    	itemSpriteInfo.put("Brass Key", new Rectangle(11, 1, 4, 8));
    	itemSpriteInfo.put("Sword", new Rectangle(16, 1, 8, 8));
    	itemSpriteInfo.put("Health Potion", new Rectangle(1, 10, 6, 6));
    }
}
