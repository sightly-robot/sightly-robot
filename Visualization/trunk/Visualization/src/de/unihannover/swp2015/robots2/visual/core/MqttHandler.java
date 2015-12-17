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

	private final static Logger log = LogManager.getLogger();
	
	private final static int MAX_ATTEMPTS = 5; 
	private final static int ATTEMPT_INTERVAL = 5000; 
	
	private final IVisualizationController visController;
	
	private int attempts = 0;
	private String ip = null;
	private Object ipLock = new Object();
	
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
			this.ipLock.notify();
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
		
		synchronized(ipLock) {
			
			try {
				log.info("Trying to connect to: {}", ip);
				this.visController.startMqtt("tcp://" + ip);
				this.attempts = 0;
				log.info("Connection established");
			} 
			catch (Exception e) {
				
				log.warn("Connection could'nt be established");
				
				this.attempts++;
					
				if (this.attempts >= MAX_ATTEMPTS)
					return;
				
				try {
					ipLock.wait(ATTEMPT_INTERVAL);
					this.run();
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
