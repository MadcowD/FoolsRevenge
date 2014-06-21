package com.lostcodestudios.fools.scripts.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.scripts.Script;
import com.lostcodestudios.fools.scripts.ai.AI;
import com.lostcodestudios.fools.scripts.ai.NullState;
import com.lostcodestudios.fools.scripts.ai.PathState;

public class Guard extends AI {
	
	public Guard () {
		super(new PathState("Patrol", 2f, true));
		// TODO Auto-generated constructor stub
	}

	private static final float SPEED = 2f;
	
	static {
		Script.Register(new Guard());
	}
	
	
	//-------------------------
	//STATES
	//-------------------------
	
	public class PatrolState extends Script{

		@Override
		public void run (GameWorld world, ObjectMap<String, Object> args) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
