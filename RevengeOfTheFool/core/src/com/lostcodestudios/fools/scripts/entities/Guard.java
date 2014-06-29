package com.lostcodestudios.fools.scripts.entities;


import com.lostcodestudios.fools.gameplay.entities.Entity;
import com.lostcodestudios.fools.scripts.ai.AI;
import com.lostcodestudios.fools.scripts.ai.PatrolState;

public class Guard extends AI {
	
	private static final float SPEED = 3f;
	
	public Guard () {
		super(new PatrolState(SPEED, true));
		// TODO Auto-generated constructor stub
	}
	
static {
		//Script.Register(new Guard());
	}
	
	@Override
	public void onSight (Entity e) {
		// TODO Auto-generated method stub
		super.onSight(e);
	}
	//-------------------------
	//STATES
	//-------------------------
	
//	public class PatrolState extends PathState{
//
//		@Override
//		public void run (GameWorld world, ObjectMap<String, Object> args) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
}
