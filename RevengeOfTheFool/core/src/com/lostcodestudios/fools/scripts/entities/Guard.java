package com.lostcodestudios.fools.scripts.entities;


import com.lostcodestudios.fools.scripts.ai.AI;
import com.lostcodestudios.fools.scripts.ai.PatrolState;

public class Guard extends AI {
	
	private static final float SPEED = 3f;
	
	public Guard () {
		super(new PatrolState(SPEED, true));
		// TODO Auto-generated constructor stub
	}
	
}