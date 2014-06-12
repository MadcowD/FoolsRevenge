package com.lostcodestudios.fools.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lostcodestudios.fools.Revenge;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = Revenge.SCREEN_WIDTH;
		config.height = Revenge.SCREEN_HEIGHT;
		config.resizable = false;
		
		new LwjglApplication(new Revenge(), config);
	}
}
