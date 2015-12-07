package de.unihannover.swp2015.robots2.visual.core;

import org.eclipse.paho.client.mqttv3.MqttException;

import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualization;
import de.unihannover.swp2015.robots2.controller.main.VisualizationMainController;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;

/**
 * @author Rico Schrage
 */
public class MqttHandler implements IVisualization {

	private final static int MAX_ATTEMPTS = 5; 
	private final static int ATTEMPT_INTERVAL = 5000; 
	
	private final VisualizationMainController visController;
	
	private int attempts = 0;
	
	/**
	 * 
	 * @param visController
	 * @param resHandler
	 * @param prefs
	 */
	public MqttHandler() {
		this.visController = new VisualizationMainController();
		this.visController.registerVisualization(this);
	}
	
	public void startMqtt(final String ip) {
		
		try {
			this.visController.startMqtt("tcp://" + ip);
			this.attempts = 0;
		} 
		catch (MqttException e) {
			
			this.attempts++;
			
			if (this.attempts >= MAX_ATTEMPTS)
				return;
			
			try {
				Thread.sleep(ATTEMPT_INTERVAL);
				this.startMqtt(ip);
			} 
			catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	public IGame getGame() {
		return visController.getGame();
	}
	
	public int getCurrentAttempt() {
		return attempts;
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
