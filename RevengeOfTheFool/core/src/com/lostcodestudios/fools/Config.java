package com.lostcodestudios.fools;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

public final class Config {
	
	public static final boolean DEBUG = true;
	
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;
	
	public static final float UNIT_SCALE = 8f;
	
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
		
		TextManager.addFont("ui-black", "fonts/Oswald-Regular.ttf", 24, Color.BLACK);
		TextManager.addFont("ui-white", "fonts/Oswald-Regular.ttf", 24, Color.WHITE);
	}
	
}
