package de.unihannover.swp2015.robots2.model.interfaces;

/**
 * A position represents the (potential) position of a robot as coordinates on
 * the Stage, it's orientation and (depending on the available information) it's
 * driving progress to the next field. Position objects are used to encapsulate
 * the position properties of all robots and store the potential start positions
 * on the Stage.
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 * @author Michael Thies
 */
public interface IPosition {

	/**
	 * Contains the four cardinal points as the possible orientations in the
	 * labyrinth.
	 * 
	 * @version 0.2
	 * @author Patrick Kawczynski
	 * @author Michael Thies
	 */
	public enum Orientation {
		NORTH("n"), EAST("e"), SOUTH("s"), WEST("w");

		private String key;

		/**
		 * Private constructor which sets the given key for each orientation.
		 * 
		 * @param orientation
		 *            the orientation key
		 */
		private Orientation(String orientation) {
			this.key = orientation;
		}

		/**
		 * Returns an orientation by the given string.
		 * 
		 * @param text
		 *            the representing string of an orientation
		 * @return the orientation object or {@code null} if string was not a
		 *         valid representation of an orientation.
		 */
		public static Orientation getBy(String text) {
			if (text.length() > 0)
				return getBy(text.charAt(0));
			else
				return null;
		}

		/**
		 * Returns an orientation by the given key.
		 * 
		 * @param c
		 *            the representing key of an orientation.
		 * @return the orientation object
		 */
		public static Orientation getBy(char c) {
			if (c == 'n') {
				return NORTH;
			} else if (c == 'e') {
				return EAST;
			} else if (c == 's') {
				return SOUTH;
			} else if (c == 'w') {
				return WEST;
			} else {
				return null;
			}
		}

		@Override
		public String toString() {
			return this.key;
		}
	}

	/**
	 * Returns the x-coordinate of the position.
	 * 
	 * @return The x-coordinate
	 */
	public int getX();

	/**
	 * Returns the y-coordinate of the position.
	 * 
	 * @return The y-coordinate
	 */
	public int getY();

	/**
	 * Returns the orientation of the position.
	 * 
	 * @return The orientation
	 */
	public Orientation getOrientation();

	/**
	 * Returns the driving progress.
	 * 
	 * @return Progress to the next field in per mills: 0 = just started, 1000
	 *         reached next field
	 */
	public int getProgress();
}
