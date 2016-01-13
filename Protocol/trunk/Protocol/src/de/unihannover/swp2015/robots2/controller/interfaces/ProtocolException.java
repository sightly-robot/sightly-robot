package de.unihannover.swp2015.robots2.controller.interfaces;

/**
 * A general exception to be thrown when some part of the protocol fails.
 * 
 * If this Exception is caused by an MqttException, it should be included as cause.
 * 
 * @author Michael Thies
 */
public class ProtocolException extends Exception {
	public ProtocolException() {
		super();
	}

	public ProtocolException(String message) {
		super(message);
	}

	public ProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProtocolException(Throwable cause) {
		super(cause);
	}

}
