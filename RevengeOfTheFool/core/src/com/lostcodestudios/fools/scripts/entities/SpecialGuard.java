package com.lostcodestudios.fools.scripts.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.Config;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Door;
import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.scripts.ai.AI;
import com.lostcodestudios.fools.scripts.ai.ChaseState;
import com.lostcodestudios.fools.scripts.ai.MovementState;
import com.lostcodestudios.fools.scripts.ai.NullState;
import com.lostcodestudios.fools.scripts.ai.PathState;
import com.lostcodestudios.fools.scripts.ai.State;

public class SpecialGuard extends AI {

	public State overState(Human e) {
		 int finalOffset = Integer.parseInt(e.tag.replace("s", "")) == 1 ? -1 : 1;
		
		 return new MovementState(e.getPosition(), new Vector2(58 - finalOffset, 99-73), 0f, 3.5f, new NullState());
	}
	
	public SpecialGuard() {
		
		super(new CutsceneState(new NullState()));
	}
	
	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		super.run(world, args);
		
		Entity e = (Entity)args.get("e");
		
		//check if Fool tried to escape
		final float ESCAPE_DISTANCE_2 = 7f * 7f;
		
		float dist2 = world.specialEntities.get("Fool").getPosition().cpy().sub(e.getPosition()).len2();
		
		if (world.flags.getFlag(0, 0) == 0 && dist2 > ESCAPE_DISTANCE_2) {
			world.flags.setFlag(0, 0, 1);
		}
		
		if (world.flags.getFlag(0, 0) > 0 && world.flags.getFlag(0, 0) < 3 && world.flags.getFlag(1, 1) != 1) {
			world.flags.incFlag(0, 0);
			
			world.dialog.showVoiceBubble("Hey!", e, 3.2f);
			this.setState(new ChaseState(e.getPosition(), (Human) world.specialEntities.get("Fool"), 6.7f, overState((Human)e))); // chase after the fool
		}
	}
	
}


 class CutsceneState extends State{

	public CutsceneState(State nextState) {
		super(nextState);
	}

	@Override
	public void run(final GameWorld world, ObjectMap<String, Object> args) {
		
		final Human e = (Human) args.get("e");
		final Human fool = (Human) world.specialEntities.get("Fool"); 
		
		if(world.flags.getFlag(1, 0) > 0) {
			switch (world.flags.getFlag(1, 0)) {
				case 1:
					Vector2 target = world.specialEntities.get("Fool").getPosition().cpy()
					.add(e.tag.equalsIgnoreCase("s1") ? -0.5f : 0.5f, 0);
					parent.setState(new MovementState(e.getPosition(), target, 0, 2.5f, this)
					{
						@Override
						protected void onEnd() {
							
							// TODO Auto-generated method stub
							super.onEnd();
							world.flags.setFlag(1, 0, 2);
						}
					});
					
					break;
				
				case 2:
					target = e.getPosition().cpy()
					.add(0, -7);
					parent.setState(new MovementState(e.getPosition(), target, 0, 0.6f, this)
					{
						@Override
						public void run(GameWorld world,
								ObjectMap<String, Object> args) {
							if (!world.cutsceneMode)return;
							
							// TODO Auto-generated method stub
							fool.body.setLinearVelocity(new Vector2(0, -0.6f));
							super.run(world, args);

						}
						@Override
						protected void onEnd() {
							
							super.onEnd();
							world.flags.setFlag(1, 0, 3);
							world.camera.position.set(
									Config.PIXELS_PER_METER * fool.getPosition().x, 
									Config.PIXELS_PER_METER * fool.getPosition().y, 0);
							world.camera.update();
							
							fool.body.setLinearVelocity(new Vector2());
							
							if (e.tag.equals("s1")) {
								((Door)world.specialEntities.get("KingDoor")).toggleOpen(world); // close the door
								
								world.dialog.showVoiceBubble("I'm not dragging this Fool another step.", e, 3f);
								
								world.scripts.delayScript("intro.Dialog5", 3f);
							}
						}
					});
					
					break;
					
				case 3:

					break;
					
				case 4:
					
					// path to the dungeon
					parent.setState(new PathState("DungeonPath", 3.5f, false, this) {

						@Override
						protected void onEnd() {
							world.flags.setFlag(1,0,5);
							
							Door door = (Door)world.specialEntities.get("CellDoor");
							
							if (!door.isOpen()) {
								door.toggleOpen(world);
							}
							
							super.onEnd();
						}
						
					});
					
					break;
					
				case 5:
					if (world.flags.getFlag(1, 1) == 1) {
						Door door = (Door)world.specialEntities.get("CellDoor");
						
						if (door.isOpen()) {
							door.toggleOpen(world);
							 world.flags.setFlag(0,0,1);
						}
						
						Guard guard = new Guard();
						guard.setState(((SpecialGuard)parent).overState(e));
						((Human)world.specialEntities.get(e.tag)).setUpdateScript(guard); // become a normal guard now
					}
					
					break;
			}
		}
		
	}
	
}
