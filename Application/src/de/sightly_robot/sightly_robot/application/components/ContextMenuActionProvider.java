package de.sightly_robot.sightly_robot.application.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.pivot.wtk.Action;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.MessageType;

import de.sightly_robot.sightly_robot.application.dialogs.DialogFactory;
import de.sightly_robot.sightly_robot.application.dialogs.ListDialog;
import de.sightly_robot.sightly_robot.application.models.GeneralOptions;
import de.sightly_robot.sightly_robot.controller.main.GuiMainController;
import de.sightly_robot.sightly_robot.model.interfaces.IGame;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot;

public class ContextMenuActionProvider {
	private int rx; // x click position for context menu.
	private int ry; // y click position for context menu.
	private IGame game;
	private GuiMainController controller;
	private StrategicVisualization strategicVisualization;
	private GeneralOptions options;
	
	/**
	 * Constructor for a new ContextMenu (provides Actions)
	 * @param rx X field position.
	 * @param ry Y field position.
	 * @param options Passed down GeneralOptions. 
	 * @param game Passed down game.
	 * @param controller GuiMainController reference.
	 * @param parent The strategic visualization this context menu belongs to.
	 */
	public ContextMenuActionProvider(int rx, int ry, GeneralOptions options, IGame game, GuiMainController controller, StrategicVisualization parent) {
		this.rx = rx;
		this.ry = ry;
		this.game = game;
		this.controller = controller;
		this.strategicVisualization = parent;
		this.options = options;
	}
	
	/**
	 * Context menu action that is called when someone wants to place a robot on a start position.
	 */
	public Action placeRobotHereAction = new Action() {
		@Override
		public void perform(Component source) {
			List <IPosition> startPositions = game.getStage().getStartPositions();
    		// does not allow if the selected position is no start position!
    		Boolean found = false;
    		IPosition startPos = null;
    		for (IPosition pos : startPositions) {
    			found = (pos.getX() == rx && pos.getY() == ry);
    			if (found) {
    				startPos = pos;
    				break;
    			}
    		}
    		
    		if (!found) {
				Alert.alert(MessageType.ERROR, "Cannot place robot on field, that is not a start position", strategicVisualization.getWindow());
    		} else {            			
    			// compile robot list
    			final List<IRobot> robots = new ArrayList<IRobot>();
    			
    			for (IRobot robot : game.getRobots().values()) {
    				robots.add(robot);
    			}
    			
    			final IPosition sp = startPos;
    			DialogFactory.createListDialog(strategicVisualization.getWindow(), new DialogCloseListener() {
					@Override
					public void dialogClosed(Dialog dialog, boolean modal) {								
						ListDialog listDialog = (ListDialog)dialog;
						
						// do nothing if there is no selection
						if (listDialog.getSelectedIndex() == -1)
							return;
						
            			controller.setRobotPosition(rx, ry, sp.getOrientation(), ((ListDialog)dialog).getSelectedElement());						
					}
    			}, options, robots);
    		}
		}		
	};
	
	/**
	 * Action that is called when a user wants to free a field.
	 */
	public Action freeFieldAction = new Action() {
		@Override
		public void perform(Component source) {
			// IField field = game.getStage().getField(rx, ry);
			// controller.getGame().getStage().getField(x, y)
			Alert.alert(MessageType.ERROR, "NOT IMPLEMENTED", strategicVisualization.getWindow());
		}		
	};
}
