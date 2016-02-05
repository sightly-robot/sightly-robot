package de.sightly_robot.sightly_robot.visual.test.entity;

import static org.junit.Assert.*;

import org.junit.Test;

import de.sightly_robot.sightly_robot.model.implementation.Field;
import de.sightly_robot.sightly_robot.model.implementation.Stage;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.visual.util.StageUtil;

public class StageUtilTest {

	@Test
	public void checkDriveDirectionTest() {
		final Field f1 = new Field(0, 0);
		f1.setWall(Orientation.EAST, true);
		final Field f2 = new Field(1, 1);
		f2.setWall(Orientation.NORTH, true);
		final Field f3 = new Field(5, 5);
		f3.setWall(Orientation.SOUTH, true);
		final Field f4 = new Field(0, 5);
		f4.setWall(Orientation.WEST, true);

		assertTrue(StageUtil.checkDriveDirection(f1, Orientation.WEST, 6, 6));
		assertFalse(StageUtil.checkDriveDirection(f2, Orientation.WEST, 6, 6));
		assertFalse(StageUtil.checkDriveDirection(f3, Orientation.WEST, 6, 6));
		assertTrue(StageUtil.checkDriveDirection(f4, Orientation.WEST, 6, 6));
	}

	@Test
	public void checkDriveDirectionPNTest() {
		final Stage stage = new Stage();
		stage.changeSize(6, 6);
		final Field f1 = (Field) stage.getField(0, 0);
		f1.setWall(Orientation.EAST, true);
		final Field f2 = (Field) stage.getField(1, 1);
		f2.setWall(Orientation.NORTH, true);
		final Field f3 = (Field) stage.getField(5, 5);
		f3.setWall(Orientation.NORTH, true);
		final Field f4 = (Field) stage.getField(0, 5);
		f4.setWall(Orientation.EAST, true);
		final Field f5 = (Field) stage.getField(1, 5);
		f5.setWall(Orientation.WEST, true);
		final Field f6 = (Field) stage.getField(0, 1);
		f6.setWall(Orientation.WEST, true);

		assertTrue(StageUtil.checkDriveDirectionOrNeighbors(f1, stage, Orientation.WEST));
		assertFalse(StageUtil.checkDriveDirectionOrNeighbors(f2, stage, Orientation.WEST));
		assertFalse(StageUtil.checkDriveDirectionOrNeighbors(f3, stage, Orientation.WEST));
		assertTrue(StageUtil.checkDriveDirectionOrNeighbors(f4, stage, Orientation.WEST));
		assertTrue(StageUtil.checkDriveDirectionOrNeighbors(f4, stage, Orientation.EAST));
		assertTrue(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(1, 0), stage, Orientation.WEST));
		assertTrue(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(4, 5), stage, Orientation.SOUTH));
		assertTrue(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(1, 5), stage, Orientation.WEST));
		assertFalse(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(3, 3), stage, Orientation.SOUTH));
		assertFalse(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(3, 4), stage, Orientation.SOUTH));
		assertFalse(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(3, 3), stage, Orientation.WEST));
		assertFalse(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(3, 4), stage, Orientation.EAST));
		assertFalse(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(3, 3), stage, Orientation.EAST));
		assertFalse(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(3, 4), stage, Orientation.WEST));
		assertFalse(StageUtil.checkDriveDirectionOrNeighbors(stage.getField(4, 4), stage, Orientation.SOUTH));
	}

	@Test
	public void convertToIntTest() {
		boolean[] b0 = { true, false, true, false };
		boolean[] b1 = { false, false, true, false };
		boolean[] b2 = { true, true, true, false };
		boolean[] b3 = { true, false, false, true };

		assertEquals(StageUtil.convertToInt(b0, 10), 101);
		assertEquals(StageUtil.convertToInt(b1, 10), 100);
		assertEquals(StageUtil.convertToInt(b2, 10), 111);
		assertEquals(StageUtil.convertToInt(b3, 10), 1001);

		assertEquals(StageUtil.convertToInt(b0, 2), 5);
		assertEquals(StageUtil.convertToInt(b1, 2), 4);
		assertEquals(StageUtil.convertToInt(b2, 2), 7);
		assertEquals(StageUtil.convertToInt(b3, 2), 9);
	}

}
