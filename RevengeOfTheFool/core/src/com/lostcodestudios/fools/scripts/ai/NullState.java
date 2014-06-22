package com.lostcodestudios.fools.scripts.ai;

import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.GameWorld;
import com.lostcodestudios.fools.scripts.Script;

public class NullState extends State
{

	public NullState () {
		super(null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run (GameWorld world, ObjectMap<String, Object> args) {
		//NULLSTATE DOES NOTHING.
	}

}
