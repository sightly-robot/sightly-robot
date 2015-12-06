package de.unihannover.swp2015.robots2.application.exceptions;

/**
 * This exception is throw whenever the map file contains inconsistent
 * data or data that makes no sense.
 * This is exception is NOT thrown, when the json format is invalid.
 *
 * @author Tim
 */
public class InvalidMapFile extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidMapFile(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMapFile(String message) {
		super(message);
	}
}
