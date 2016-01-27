package de.unihannover.swp2015.robots2.visual.core.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.controller.interfaces.IVisualizationController;
import de.unihannover.swp2015.robots2.controller.interfaces.ProtocolException;
import de.unihannover.swp2015.robots2.controller.main.VisualizationMainController;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;

/**
 * Handles the controller provided by the protocol.
 * 
 * Also handles reconnecting when initial connect failed.<br>
 * It's intended to run this in a new {@link Thread}.
 * 
 * @author Rico Schrage
 */
public class MqttHandler implements Runnable {

	/** logger (log4j) */
	private static final Logger LOGGER = LogManager.getLogger();

	/** defines how often the application tries to reconnect */
	private static final int MAX_ATTEMPTS = 5;
	/** defines how long the application waits until it tries to reconnect */
	private static final int ATTEMPT_INTERVAL = 5000;
	/**
	 * defines with which value Thread.sleep should get called (lower -> more
	 * precise)
	 */
	private static final int SLEEP_TIME = 1000;

	/** controller from the protocol */
	private final IVisualizationController visController;

	/** current attempt */
	private int attempts = 0;
	/** IP of the broker */
	private String ip = null;
	/** object, which holds the lock on the {@link #ip} */
	private Object ipLock = new Object();
	/** true if the IP was changed recently */
	private boolean ipChanged = false;
	/** transport-protocol of the MQTT connection */
	private MqttProtocol protocol;

	/**
	 * Constructs a new MqttHandler with {@link MqttProtocol#TCP} as protocol.
	 * 
	 * @param ip
	 *            IP of the broker
	 * @param preferenceHandler
	 *            handler for incoming/outgoing preferences
	 */
	public MqttHandler(String ip, IVisualization preferenceHandler) {
		this(MqttProtocol.TCP, ip, preferenceHandler);
	}

	/**
	 * Constructs a new MqttHandler.
	 * 
	 * @param protocol
	 *            transport-protocol of the MQTT connection
	 * @param ip
	 *            IP of the broker
	 * @param preferenceHandler
	 *            handler for incoming/outgoing preferences
	 */
	public MqttHandler(MqttProtocol protocol, String ip,
			IVisualization preferenceHandler) {
		this.visController = new VisualizationMainController();
		this.visController.registerVisualization(preferenceHandler);
		this.ip = ip;
		this.protocol = protocol;
	}

	/**
	 * Sets the IP of the broker.
	 * 
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

				LOGGER.info("Trying again in {} seconds.",
						ATTEMPT_INTERVAL / 1000);

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
	 */
	public enum MqttProtocol {
		SSL("ssl"), TCP("tcp");

		/** prefix of the IP which also identifies the protocol */
		private String protocolPrefix;

		/**
		 * Constructs protocol constant.
		 * 
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
		 * Returns a MQTT protocol, which contains the given string.
		 * 
		 * @param proto
		 *            identifier of the protocol
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
