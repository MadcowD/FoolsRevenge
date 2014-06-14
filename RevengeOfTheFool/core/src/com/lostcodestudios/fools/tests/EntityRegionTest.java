package com.lostcodestudios.fools.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Rectangle;
import com.lostcodestudios.fools.gameplay.entities.EntityRegion;

public class EntityRegionTest {


	@Test
	public void testEntityRegion() {
		EntityRegion test = new EntityRegion(4, new Rectangle(0,0, 1024,1024));
		assertEquals(test.getRegion().getHeight(), 1024, 0);
		assertEquals(test.getSubRegions()[0].getRegion().getHeight(), 512, 0);
		assertEquals(test.getSubRegions()[3].getRegion().getY(), 512, 0);
		assertEquals(test.getSubRegions()[3].getRegion().getX(), 512, 0);
		assertTrue(test.getSuperRegion() == null);
		
		
	}

	@Test
	public void testRebalance() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAdd() {
		EntityRegion root = new EntityRegion(4, new Rectangle(0,0, 1024,1024));
	}

	@Test
	public void testRemove() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testUnsafeRemove() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testExecuteAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testExecute() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSelectRectangle() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSelectRectangleArrayOfEntity() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testContainsEntity() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testContainsVector2() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testContainsRectangle() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOverlaps() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetSubRegions() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSetSubRegions() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetSuperRegion() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetRegion() {
		fail("Not yet implemented"); // TODO
	}

}
