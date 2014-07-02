package com.lostcodestudios.fools.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.Revenge;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
        // writing a comment for fun
        // this time writing another comment for fun
        // one last comment, hopefully!

		config.width = Config.SCREEN_WIDTH;
		config.height = Config.SCREEN_HEIGHT;
		config.resizable = false;
		
		new LwjglApplication(new Revenge(), config);
	}
}
