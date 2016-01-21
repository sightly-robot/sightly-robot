package de.unihannover.swp2015.robots2.visual.core.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.controller.interfaces.IVisualizationController;
import de.unihannover.swp2015.robots2.controller.interfaces.ProtocolException;
import de.unihannover.swp2015.robots2.controller.main.VisualizationMainController;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;

/**
 * Handles the controller provided by the protocol. Also handled reconnecting
 * when initial connect failed.<br>
 * It's intended to run this in a new {@link Thread}.
 * 
 * @author Rico Schrage
 */
public class MqttHandler implements Runnable {

	/** Logger (log4j) */
	private static final Logger LOGGER = LogManager.getLogger();

	/** Defines how often the app tries to reconnect */
	private static final int MAX_ATTEMPTS = 5;
	/** Defines how long the app waits until it tries to reconnect */
	private static final int ATTEMPT_INTERVAL = 5000;
	/**
	 * Defines with which value Thread.sleep should get called (lower -> more
	 * precise)
	 */
	private static final int SLEEP_TIME = 1000;

	/** Controller from the protocol */
	private final IVisualizationController visController;

	/** Current attempt */
	private int attempts = 0;
	/** IP of the broker */
	private String ip = null;
	/** Object, which holds the lock on the {@link #ip} */
	private Object ipLock = new Object();
	/** True if the was changed recently */
	private boolean ipChanged = false;
	/** Transport-protocol of the MQTT connection */
	private MqttProtocol protocol; 

	/**
	 * Constructs a new MqttHandler with {@link MqttProtocol#TCP} as protocol.
	 * 
	 * @param ip IP of the broker
	 * @param preferenceHandler handler for incoming/outgoing preferences
	 */
	public MqttHandler(String ip, IVisualization preferenceHandler) {
		this(MqttProtocol.TCP, ip, preferenceHandler);
	}
	
	/**
	 * Constructs a new MqttHandler.
	 * 
	 * @param ip
	 *            IP of the broker
	 */
	public MqttHandler(MqttProtocol protocol, String ip, IVisualization preferenceHandler) {
		this.visController = new VisualizationMainController();
		this.visController.registerVisualization(preferenceHandler);
		this.ip = ip;
		this.protocol = protocol;
	}

	/**
	 * Set the IP of the broker. <br>
	 * This method is thread safe.
	 * 
	 * @param ip
	 *            new IP of the broker
	 */
	public void setIp(String ip) {
		synchronized (ipLock) {
			this.ip = ip;
			this.ipLock.notifyAll();
			this.ipChanged = true;
			LOGGER.info("Broker IP has been changed to: {}", ip);
		}
	}

	/**
	 * @return game provided by the controller
	 */
	public IGame getGame() {
		return visController.getGame();
	}

	/**
	 * @return current attempt
	 */
	public int getCurrentAttempt() {
		return attempts;
	}

	@Override
	public void run() {
		connect();
	}

	/**
	 * Just to clarify the reconnection algorithm.
	 */
	private void connect() {

		synchronized (ipLock) {

			try {
				LOGGER.info("Trying to connect to: {}", ip);
				visController.startMqtt(protocol + "://" + ip);
				attempts = 0;
				LOGGER.info("Connection established");
			} catch (ProtocolException e) {
				LOGGER.warn("Connection could'nt be established.", e);

				attempts++;
				if (attempts >= MAX_ATTEMPTS) {
					return;
				}

				LOGGER.info("Trying again in {} seconds.", ATTEMPT_INTERVAL / 1000);

				try {
					long timePassed = 0;
					long startTime = System.currentTimeMillis();
					while (!ipChanged && timePassed < ATTEMPT_INTERVAL) {
						ipLock.wait(SLEEP_TIME);
						timePassed = System.currentTimeMillis() - startTime;
					}
					ipChanged = false;

					connect();
				} catch (InterruptedException e1) {
					LOGGER.error("ipLock.wait(...) has been interrupted!", e);
				}
			}
		}
	}
	
	/**
	 * Identifies all available protocol prefixes for the IP.
	 * 
	 * @author Rico Schrage
	 */
	public enum MqttProtocol {
		SSL("ssl"), 
		TCP("tcp");
		
		/** Prefix of the IP, which also identifies the protocol */
		private String protocolPrefix;
		
		/**
		 * Constructs protocol constant.
		 * @param id
		 */
		private MqttProtocol(String id) {
			this.protocolPrefix = id;
		}
		
		@Override
		public String toString() {
			return protocolPrefix;
		}
		
		/**
		 * Returns a MqttProtocol, which contains the given string.
		 * 
		 * @param proto Identifier of the protocol
		 * @return matching enum
		 */
		public static MqttProtocol searchMatching(String proto) {
			MqttProtocol[] values = MqttProtocol.values();
			for (MqttProtocol value : values) {
				if (value.toString().equals(proto)) {
					return value;
				}
			}
			return null;
		}
	}

}
