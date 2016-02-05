package de.sightly_robot.sightly_robot.visual.util.test;

import java.util.UUID;

import de.sightly_robot.sightly_robot.model.implementation.Field;
import de.sightly_robot.sightly_robot.model.implementation.Game;
import de.sightly_robot.sightly_robot.model.implementation.Robot;
import de.sightly_robot.sightly_robot.model.implementation.Stage;
import de.sightly_robot.sightly_robot.model.interfaces.IGame;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition.Orientation;
import de.sightly_robot.sightly_robot.model.writeableInterfaces.IGameWriteable;

/**
 * Contains everything what could help to test the visualization.
 * 
 * @author Rico Schrage
 */
public class TestUtil {

	private TestUtil() {
		// utility class
	}

	/**
	 * Creates a randomly configured {@link Game} for testing
	 * 
	 * @param numOfRobots
	 *            number of robots supposed to be in that game
	 * @param fieldWidth
	 *            width in fields
	 * @param fieldHeight
	 *            height in fields
	 * @return game created from those parameters with some additional test
	 *         structures
	 */
	public static IGame createRandomTestGame(final int numOfRobots,
			final int fieldWidth, final int fieldHeight) {
		final IGameWriteable game = new Game();
		for (int i = 0; i < numOfRobots; ++i) {
			final Robot rob = new Robot(UUID.randomUUID().toString(),
					i % 2 == 0, false);
			rob.addScore(i * 23);
			rob.setPosition(i % fieldWidth, (i + 3) % fieldHeight,
					Orientation.NORTH);
			game.addRobot(rob);
		}
		final Stage s = (Stage) game.getStage();
		s.changeSize(fieldWidth, fieldHeight);
		s.addStartPosition(0, 0, Orientation.SOUTH);
		final Field testField = (Field) s.getField(Math.abs(fieldWidth - 3),
				Math.abs(fieldHeight - 5));
		testField.setWall(Orientation.SOUTH, true);
		testField.setWall(Orientation.NORTH, true);
		testField.setWall(Orientation.WEST, true);
		testField.setWall(Orientation.EAST, true);
		return game;
	}

}
