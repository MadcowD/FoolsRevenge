package com.lostcodestudios.fools.scripts.entities;


import com.lostcodestudios.fools.scripts.Script;
import com.lostcodestudios.fools.scripts.ai.AI;
import com.lostcodestudios.fools.scripts.ai.NullState;
import com.lostcodestudios.fools.scripts.ai.PathState;

public class Guard extends AI {
	
	public Guard () {
		super(new PathState("Patrol",2f,true));
		// TODO Auto-generated constructor stub
	}

	private static final float SPEED = 2f;
	
	static {
		Script.Register(new Guard());
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
