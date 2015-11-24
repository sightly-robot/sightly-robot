package de.unihannover.swp2015.robots2.controller.main;

/**
 * Specifies methods to be used by FieldTimerTask. These timers are needed for
 * some field state changes. As their expiration sometimes will trigger mqtt
 * messages, the main controller has to care in these cases.
 * 
 * @author Michael Thies
 */
public interface IFieldTimerController {
	
	/**
	 * This method is called after the random wait when a conflict occurred.
	 * 
	 * The method will resend a MQTT "lock" message and reset the Field state to LOCK_WAIT. 
	 * 
	 * @param x x coordinate of the Field to try to lock again
	 * @param y y coordinate of the Field to try to lock again
	 */
	public void retryLockField(int x, int y);
	
	/**
	 * This method is called after the safety wait following a lock massage.
	 * 
	 * It will send a MQTT "occupy set" message and set the Field state to OURS. 
	 * 
	 * @param x x coordinate of the Field to occupy
	 * @param y y coordinate of the Field to occupy
	 */
	public void occupyField(int x, int y); 
}
