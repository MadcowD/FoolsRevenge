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
import com.lostcodestudios.fools.scripts.ai.State;

public class SpecialGuard extends AI {

	public SpecialGuard() {
		super(new CutsceneState(new NullState()));
	}

	@Override
	public void run(GameWorld world, ObjectMap<String, Object> args) {
		// TODO Auto-generated method stub
		super.run(world, args);
	}

	@Override
	public void onSight(Entity e) {
		super.onSight(e);
		
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
					
					target = new Vector2(36, 99-18);
					target.y += Integer.parseInt(e.tag.replace("s","")) == 1 ? -1 : 1;
					
					parent.setState(new MovementState(e.getPosition(), target, 0, 3.5f, new MovementState(e.getPosition(), new Vector2(8, 99-59 - Integer.parseInt(e.tag.replace("s", "")) == 1 ? -1 : 1), 0, 3.5f, this)
					{
						@Override
						public void run(GameWorld world,
								ObjectMap<String, Object> args) {
							
							//check if Fool tried to escape
							final float ESCAPE_DISTANCE_2 = 6f * 6f;
							
							float dist2 = world.specialEntities.get("Fool").getPosition().cpy().sub(e.getPosition()).len2();
							
							if (dist2 > ESCAPE_DISTANCE_2) {
								world.flags.setFlag(0, 0, 1);
							}
							
							if (world.flags.getFlag(0, 0) == 1) {
								world.dialog.showVoiceBubble("Hey!", e, 3.2f);
								parent.setState(new ChaseState(e.getPosition(), world.specialEntities.get("Fool"), 0f, 5f, this)); // chase after the fool
							}

							super.run(world, args);

						}
						@Override
						protected void onEnd() {
							
							super.onEnd();
							world.flags.setFlag(1, 0, 5);
							
							// wait for Fool to enter jail
							
						}
					})
					{
						@Override
						public void run(GameWorld world,
								ObjectMap<String, Object> args) {
							
							//check if Fool tried to escape
							final float ESCAPE_DISTANCE_2 = 6f * 6f;
							
							float dist2 = world.specialEntities.get("Fool").getPosition().cpy().sub(e.getPosition()).len2();
							
							if (dist2 > ESCAPE_DISTANCE_2) {
								world.flags.setFlag(0, 0, 1);
							}
							
							if (world.flags.getFlag(0, 0) == 1) {
								world.dialog.showVoiceBubble("Hey!", e, 3.2f);
								parent.setState(new ChaseState(e.getPosition(), world.specialEntities.get("Fool"), 0f, 5f, this)); // chase after the fool
							}

							super.run(world, args);

						}
						@Override
						protected void onEnd() {
							
							super.onEnd();
							
						}
					});
					
					break;
			}
		}
		
	}
	
}
