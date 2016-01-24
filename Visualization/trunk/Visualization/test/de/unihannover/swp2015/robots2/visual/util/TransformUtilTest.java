package de.unihannover.swp2015.robots2.visual.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransformUtilTest {
	
	@Test
	public void calcRotationTest() {
		assertEquals(360f, TransformUtil.calculateShortestRotation(270, 0), 1f);
		assertEquals(0f, TransformUtil.calculateShortestRotation(90, 0), 1f);
		assertEquals(-90f, TransformUtil.calculateShortestRotation(0, -90), 1f);
		assertEquals(270f, TransformUtil.calculateShortestRotation(180, -90), 1f);
		assertEquals(-180f, TransformUtil.calculateShortestRotation(-90, 180), 1f);
		assertEquals(-0f, TransformUtil.calculateShortestRotation(-90, 0), 1f);
		assertEquals(90f, TransformUtil.calculateShortestRotation(180, 90), 1f);
		assertEquals(-0f, TransformUtil.calculateShortestRotation(-90, 0), 1f);
		assertEquals(90f, TransformUtil.calculateShortestRotation(0, 90), 1f);
		assertEquals(180f, TransformUtil.calculateShortestRotation(90, 180), 1f);
	}
	
}
