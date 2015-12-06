package de.unihannover.swp2015.robots2.application;

import de.unihannover.swp2015.robots2.application.components.StrategicVisualization;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * A thread that automatically repaints the visualization.
 * Repainting from an event is discouraged, because its expensive.
 * 
 * @author Tim
 */
public class VisualizationUpdater extends Thread implements IModelObserver {

	private boolean updated = false;
	private boolean running = false;
	private boolean enabled = true;
	private StrategicVisualization visualization;
	
	/**
	 * Construct with reference to visualization component.
	 * @param visualization A visualization component.
	 */
	public VisualizationUpdater(StrategicVisualization visualization) {
		super();
		this.visualization = visualization;
	}
	
	/**
	 * Periodically repaints the visualization.
	 */
	@Override
	public void run() {
		running = true;
		while (running) {
			// no updates?
			if (!updated || !enabled) {
				try {
					// sleep for a moment and wait for updates
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// thread got interrupted, stop everything
					running = false;
					return;
				}
				continue; // go on.
			}
			
			// updates!
			visualization.update();
			updated = false;
		}
	}
	
	/**
	 * Sets the flag and let the run() function return.
	 */
	public synchronized void gentleStop() {
		running = false;
	}
	
	/**
	 *	Disables the updating.  
	 */
	public synchronized void disable() {
		enabled = false;
	}
	
	/**
	 *	Enables the updating
	 */
	public synchronized void enable() {
		enabled = true;
	}

	/**
	 * Set update flag when an event is received, that is important for the visualization.
	 * 
	 * @param event A model update event emitted by the controller.
	 */
	@Override
	public synchronized void onModelUpdate(IEvent event) {
		// almost every type and those that aren't, are unnecessary to filter.
		updated = true;	
	}	
	
}
