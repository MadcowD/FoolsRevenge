package com.lostcodestudios.fools.gameplay;

public class EventFlagManager {
	
	private static final int FLAG_ROWS = 50;
	private static final int FLAG_COLS = 50;
	
	private int[][] flags = new int[FLAG_ROWS][FLAG_COLS];
	
	public int getFlag(int row, int col) {
		return flags[row][col];
	}
	
	public void setFlag(int row, int col, int value) {
		flags[row][col] = value;
	}
	
	public void changeFlag(int row, int col, int delta) {
		flags[row][col] += delta;
	}
	
	public void incFlag(int row, int col) {
		changeFlag(row, col, 1);
	}
	
}
