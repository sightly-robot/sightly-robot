package de.unihannover.swp2015.robots2.visual.core;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.controller.interfaces.IVisualizationController;
import de.unihannover.swp2015.robots2.controller.main.VisualizationMainController;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;

/**
 * @author Rico Schrage
 */
public class MqttHandler implements IVisualization, Runnable {

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
				this.visController.startMqtt("tcp://" + ip);
				this.attempts = 0;
			} 
			catch (Exception e) {
				
				this.attempts++;
				
				if (this.attempts >= MAX_ATTEMPTS)
					return;
				
				try {
					Thread.sleep(ATTEMPT_INTERVAL);
					this.run();
				} 
				catch (InterruptedException e1) {
					e1.printStackTrace();
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
