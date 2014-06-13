package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.gameplay.EntityWorld;

public class DialogManager {
	
	private boolean wasAcceptKeyPressed = false;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private Array<Dialog> dialogsToShow = new Array<Dialog>();
    
    private EntityWorld world;

	public DialogManager(EntityWorld world) {
		this.world = world;
	}
	
	public void render(SpriteBatch spriteBatch, float delta) {
		boolean acceptKeyPressed = Gdx.input.isKeyPressed(Config.ACCEPT_KEY);
		
		if (acceptKeyPressed && !wasAcceptKeyPressed) {
			//handle a new press of the accept key
			if (dialogsToShow.size > 0) {
				dialogsToShow.removeIndex(0);
			}

            if (dialogsToShow.size == 0) {
                world.resume();
            }
		}
		
		wasAcceptKeyPressed = acceptKeyPressed;
		
		if (dialogsToShow.size > 0) {
			dialogsToShow.get(0).render(spriteBatch, shapeRenderer);
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
	
}
