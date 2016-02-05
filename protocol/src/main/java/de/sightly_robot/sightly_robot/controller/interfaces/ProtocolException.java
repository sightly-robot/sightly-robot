package de.sightly_robot.sightly_robot.controller.interfaces;

/**
 * A general exception to be thrown when some part of the protocol fails.
 * 
 * If this Exception is caused by an MqttException, it should be included as
 * cause.
 * 
 * @author Michael Thies
 */
public class ProtocolException extends Exception {

	/**
	 * Default ProtocolException without message or cause.
	 */
	public ProtocolException() {
		super();
	}

	/**
	 * ProtocolException which holds a message.
	 * 
	 * @param message
	 *            the exception message
	 */
	public ProtocolException(String message) {
		super(message);
	}

	/**
	 * ProtocolException which holds a message and a cause (MqttException)
	 * 
	 * @param message
	 *            the exception message
	 * @param cause
	 *            the cause of the exception
	 */
	public ProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * ProtocolException which holds a cause.
	 * 
	 * @param cause
	 *            the cause of the exception
	 */
	public ProtocolException(Throwable cause) {
		super(cause);
	}

}
