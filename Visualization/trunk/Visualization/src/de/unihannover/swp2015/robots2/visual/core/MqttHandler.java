package de.unihannover.swp2015.robots2.visual.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.controller.interfaces.IVisualizationController;
import de.unihannover.swp2015.robots2.controller.main.VisualizationMainController;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;

/**
 * @author Rico Schrage
 */
public class MqttHandler implements IVisualization, Runnable {

	private static final Logger log = LogManager.getLogger();
	
	private static final int MAX_ATTEMPTS = 5; 
	private static final int ATTEMPT_INTERVAL = 5000; 
	private static final int SLEEP_TIME = 1000; 
	
	private final IVisualizationController visController;
	
	private int attempts = 0;
	private String ip = null;
	private Object ipLock = new Object();
	private boolean ipChanged = false;
	
	/**
	 * 
	 * @param visController
	 * @param resHandler
	 * @param prefs
	 */
	public MqttHandler(final String ip) {
		this.visController = new VisualizationMainController();
		this.visController.registerVisualization(this);
		this.ip = ip;
	}
	
	public void setIp(final String ip) {
		synchronized(ipLock) {
			this.ip = ip;
			this.ipLock.notifyAll();
			this.ipChanged = true;
		}
	}
	
	public IGame getGame() {
		return visController.getGame();
	}
	
	public int getCurrentAttempt() {
		return attempts;
	}
	
	@Override
    public void run() {
		this.connect();
    }
	
	private void connect() {
		
		synchronized(ipLock) {
			
			try {
				log.info("Trying to connect to: {}", ip);
				this.visController.startMqtt("tcp://" + ip);
				this.attempts = 0;
				log.info("Connection established");
			} 
			catch (Exception e) {
				
				log.warn("Connection could'nt be established.");
				
				this.attempts++;
					
				if (this.attempts >= MAX_ATTEMPTS)
					return;
				
				log.info("Trying again in {} seconds.", ATTEMPT_INTERVAL/1000);
				
				try {
					
					long timePassed = 0;
					long startTime = System.currentTimeMillis();
					while (!ipChanged && timePassed < ATTEMPT_INTERVAL) {
						ipLock.wait(SLEEP_TIME);
						timePassed = (System.currentTimeMillis() - startTime);
					}
					ipChanged = false;
					
					this.connect();
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
