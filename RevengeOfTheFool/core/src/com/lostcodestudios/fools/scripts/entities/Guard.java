package com.lostcodestudios.fools.scripts.entities;


import com.lostcodestudios.fools.scripts.Script;
import com.lostcodestudios.fools.scripts.ai.AI;
import com.lostcodestudios.fools.scripts.ai.PathState;

public class Guard extends AI {
	
	private static final float SPEED = 2f;
	
	public Guard () {
		super(new PathState("Patrol", SPEED, true));
		// TODO Auto-generated constructor stub
	}
	
	static {
		//Script.Register(new Guard());
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
