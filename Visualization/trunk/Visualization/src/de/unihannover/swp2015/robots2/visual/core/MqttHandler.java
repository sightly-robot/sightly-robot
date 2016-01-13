package de.unihannover.swp2015.robots2.visual.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.controller.interfaces.IVisualizationController;
import de.unihannover.swp2015.robots2.controller.main.VisualizationMainController;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;

/**
 * Handles the controller provided by the protocol. Also handled reconnecting when initial connect failed.<br>
 * It's intended to run this in a new {@link Thread}.
 * 
 * @author Rico Schrage
 */
public class MqttHandler implements IVisualization, Runnable {

	/** Logger (log4j) */
	private static final Logger log = LogManager.getLogger();
	
	/** Defines how often the app tries to reconnect */
	private static final int MAX_ATTEMPTS = 5; 
	/** Defines how long the app waits until it tries to reconnect */
	private static final int ATTEMPT_INTERVAL = 5000; 
	/** Defines with which value Thread.sleep should get called (lower -> more precise) */
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
	
	/**
	 * Constructs a new MqttHandler.
	 * 
	 * @param ip IP of the broker
	 */
	public MqttHandler(String ip) {
		this.visController = new VisualizationMainController();
		this.visController.registerVisualization(this);
		this.ip = ip;
	}
	
	/**
	 * Set the IP of the broker. <br>
	 * This method is thread safe.
	 * @param ip new IP of the broker
	 */
	public void setIp(String ip) {
		synchronized(ipLock) {
			this.ip = ip;
			this.ipLock.notifyAll();
			this.ipChanged = true;
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
		
		synchronized(ipLock) {
			
			try {
				log.info("Trying to connect to: {}", ip);
				visController.startMqtt("tcp://" + ip);
				attempts = 0;
				log.info("Connection established");
			} 
			catch (Exception e) {
				log.warn("Connection could'nt be established.");
				
				attempts++;
					
				if (attempts >= MAX_ATTEMPTS)
					return;
				
				log.info("Trying again in {} seconds.", ATTEMPT_INTERVAL/1000);
				
				try {
					
					long timePassed = 0;
					long startTime = System.currentTimeMillis();
					while (!ipChanged && timePassed < ATTEMPT_INTERVAL) {
						ipLock.wait(SLEEP_TIME);
						timePassed = System.currentTimeMillis() - startTime;
					}
					ipChanged = false;
					
					connect();
				} 
				catch (InterruptedException e1) {
					log.error("ipLock.wait(...) has been interrupted!");
				}
			}
		}
	}
	
	@Override
	public void setSettings(String settings) {
		//TODO json->preferences
	}

	@Override
	public String getSettings() {
		//TODO preferences->json
		return null;
	}

}
