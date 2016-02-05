package de.sightly_robot.sightly_robot.application.observers;

import de.sightly_robot.sightly_robot.application.components.StrategicVisualization;
import de.sightly_robot.sightly_robot.controller.main.GuiMainController;
import de.sightly_robot.sightly_robot.model.externalInterfaces.IModelObserver;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot;

/**
 * A thread that automatically repaints the visualization.
 * Repainting from an event is discouraged, because its expensive.
 * 
 * @author Tim Ebbeke
 */
public class VisualizationUpdater implements IModelObserver {

	private volatile boolean updated = false;
	private boolean enabled = true;
	private StrategicVisualization visualization;
	private GuiMainController controller;
	
	/**
	 * Construct with reference to visualization component.
	 * @param visualization A visualization component.
	 */
	public VisualizationUpdater(StrategicVisualization visualization, GuiMainController controller) {
		super();
		this.visualization = visualization;
		this.controller = controller;
		
		// observe stage
		controller.getGame().getStage().observe(this);
		
		// observe game
		controller.getGame().observe(this);
	}
	
	/**
	 * Repaints the visualization, but only if necessary.
	 */
	public void update() {
		// no updates?
		if (!updated || !enabled) {
			return;
		}
			
		// updates!
		visualization.update();
		visualization.repaint();
		updated = false;
	}
	
	/**
	 *	Disables the updating.  
	 */
	public synchronized void disable() {
		enabled = false;
	}
	
	/**
	 *	Enables the updating.
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
		if (event.getType() == IEvent.UpdateType.STAGE_SIZE)
		{
			// observe to every field
			for (int x = 0; x != controller.getGame().getStage().getWidth(); ++x) {
				for (int y = 0; y != controller.getGame().getStage().getHeight(); ++y) {
					controller.getGame().getStage().getField(x, y).observe(this);;
				}
			}
		}
		
		if (event.getType() == IEvent.UpdateType.ROBOT_ADD)
		{
			((IRobot)event.getObject()).observe(this);
		}
		
		// ignored events
		switch(event.getType()) {
			case ROBOT_PROGRESS:
			case ROBOT_SCORE:
			case GAME_PARAMETER:
				return;
			default:
				break;
		}
		
		updated = true;	
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;		
	}	
	
}
