package com.lostcodestudios.fools.scripts;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;

public class End extends Script {

	static {
		Script.Register(new End());
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		// TODO handle the ending with dialogs
		
		if (world.flags.getFlag(1, 1) == 1) {
			// the Fool died
			
			// no ending text here
		} else {
			// yes ending! Handle the outcome
			
			final int BLOODY_COUNT = 3;
			
			if (world.flags.getFlag(0, 2) == 0) {
				// the fool didn't kill the King
				
				String dialog1 = "The Fool departed the castle, leaving his vengeance unfulfilled";
				
				if (world.flags.getFlag(0, 4) >= BLOODY_COUNT) {
					// kill 3 guards and get a bloody ending
					dialog1 += " and a trail of innocent blood in his wake.";
				} else {
					dialog1 += "."; // end of story. Kill just 2 people and you're a saint for all we care.
				}
				
				world.dialog.showDialogFull(dialog1);
			} else {
				String dialog1 = "";
				
				if (world.flags.getFlag(0, 3) == 1) {
					dialog1 += "With his prize in hand ";
				}
				
				if (world.flags.getFlag(0, 4) >= BLOODY_COUNT) {
					dialog1 += " and a trail of innocent blood in his wake";
				}
				
				dialog1 += ", the Fool took his exit, his vengeance complete.";
				
				world.dialog.showDialogFull(dialog1);
			}
			
			
		}
		
		world.dialog.showDialogFull("THE END");
		
		world.gameOver = true; // marks the world to return to the main menu
	}

}
