package com.lostcodestudios.fools.gameplay;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.gameplay.entities.Entity;

public class DialogManager {
		//comment
	private ShapeRenderer screenShapeRenderer;
	private ShapeRenderer worldShapeRenderer;
	
	private Array<Dialog> dialogsToShow = new Array<Dialog>();
    private Array<VoiceBubble> voiceBubbles = new Array<VoiceBubble>();
	
    private GameWorld world;

	public DialogManager(GameWorld world) {
		this.world = world;
		
		this.screenShapeRenderer = world.screenShapeRenderer;
		this.worldShapeRenderer = world.worldShapeRenderer;
	}
	
	public void dispose() {
	}
	
	public void render(SpriteBatch spriteBatch, SpriteBatch worldSpriteBatch, float delta) {
		//render voice bubbles
		Iterator<VoiceBubble> it = voiceBubbles.iterator();
		
		while (it.hasNext()) {
			VoiceBubble bubble = it.next();
			
			bubble.render(worldSpriteBatch, worldShapeRenderer);
			
			if (!world.isPaused()) {
				if (bubble.update(delta)) {
					it.remove();
				}
			}
		}
		
		if (world.input.wasKeyPressed(Config.ACCEPT_KEY)) {
			//handle a new press of the accept key
			if (dialogsToShow.size > 0) {
				dialogsToShow.removeIndex(0);
			}

            if (dialogsToShow.size == 0) {
                world.resume();
            }
		}
		
		if (dialogsToShow.size > 0) {
			dialogsToShow.get(0).render(spriteBatch, screenShapeRenderer);
		}
	}
	
	public void showDialog(Dialog dialog) {
        world.pause();

		dialogsToShow.add(dialog);
	}
	
	public void showDialogWindow(String text) {
        //His booming voice sent tremors down the Fool's spine.
        Dialog dialog = new Dialog("dialog-black", "ui-black", text, Color.GRAY, Config.DIALOG_BORDER_COLOR,
                false, true, HAlignment.LEFT);

        dialog.bgColor1 = Config.DIALOG_WINDOW_COLOR_1;
        dialog.bgColor2 = Config.DIALOG_WINDOW_COLOR_2;
        dialog.bgColor3 = Config.DIALOG_WINDOW_COLOR_3;
        dialog.bgColor4 = Config.DIALOG_WINDOW_COLOR_4;

		showDialog(dialog);
	}
	
	public void showDialogFull(String text) {
		showDialog(new Dialog("dialog-white", "ui-white", 
				text, //"The King's rage shook the castle like thunder.", 
				Color.BLACK, Color.BLACK, true, false, 
				HAlignment.CENTER));
	}
	
	public void showVoiceBubble(String text, Entity speaker, float time) {
		voiceBubbles.add(new VoiceBubble(text, speaker, time));
	}
	
}
