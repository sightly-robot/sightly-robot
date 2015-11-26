package de.unihannover.swp2015.robots2.abstractrobot;

import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public enum Direction {

	FOREWARD,
	RIGHT,
	BACKWARD,
	LEFT;
	
	public static Direction calcDirection(Orientation robotOrientation,Orientation orientation)
	{
		switch (orientation) {
		case NORTH:
			switch (robotOrientation) {
			case EAST:
				return LEFT;
			case SOUTH:
				return BACKWARD;
			case WEST:
				return RIGHT;
			case NORTH:
				return FOREWARD;
			}
			break;
		case EAST:
			switch (robotOrientation) {
			case NORTH:
				return RIGHT;
			case WEST:
				return BACKWARD;
			case SOUTH:
				return LEFT;
			case EAST:
				return FOREWARD;
			}
			break;
		case SOUTH:
			switch (robotOrientation) {
			case EAST:
				return RIGHT;
			case NORTH:
				return BACKWARD;
			case WEST:
				return LEFT;
			case SOUTH:
				return FOREWARD;
			}
			break;
		case WEST:
			switch (robotOrientation) {
			case SOUTH:
				return RIGHT;
			case EAST:
				return BACKWARD;
			case NORTH:
				return LEFT;
			case WEST:
				return FOREWARD;
			}
			break;
		}
		return FOREWARD;
	}
}
