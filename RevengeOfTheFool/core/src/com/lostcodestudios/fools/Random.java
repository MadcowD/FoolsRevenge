package com.lostcodestudios.fools;

import java.util.LinkedList;

public final class Random {

	
	
	private static LinkedList<Double> randoms = new LinkedList<Double>();
	
	public static void init(int number){
		for(int i = 0; i < number; i++){
			randoms.addFirst(Math.random());
		}
	}
	
	public static double random() {
		double r = randoms.removeFirst();
		randoms.addLast(r);
		return r;
	}
	
}
