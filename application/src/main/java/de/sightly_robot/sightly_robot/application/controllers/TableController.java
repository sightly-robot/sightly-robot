package de.sightly_robot.sightly_robot.application.controllers;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.pivot.collections.Sequence;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.Mouse.Button;
import org.apache.pivot.wtk.Span;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TableViewSelectionListener;

import de.sightly_robot.sightly_robot.application.components.StrategicVisualization;
import de.sightly_robot.sightly_robot.application.models.GeneralOptions;
import de.sightly_robot.sightly_robot.application.models.TableData;
import de.sightly_robot.sightly_robot.application.models.TableElement;
import de.sightly_robot.sightly_robot.controller.main.GuiMainController;
import de.sightly_robot.sightly_robot.model.externalInterfaces.IModelObserver;
import de.sightly_robot.sightly_robot.model.interfaces.IEvent;
import de.sightly_robot.sightly_robot.model.interfaces.IPosition;
import de.sightly_robot.sightly_robot.model.interfaces.IRobot;

/**
 * Automatic event controlled updater for the robot list. 
 * @author Tim Ebbeke
 */
public class TableController implements IModelObserver {
	private TableView table;
	private GuiMainController controller;
	private volatile boolean updated;
	private volatile TableData elements;
	private GeneralOptions options;
	private StrategicVisualization visualization;
	
	public TableController(TableView table, 
						 GuiMainController controller, 
						 GeneralOptions options,
						 StrategicVisualization visualization) 
	{
		this.table = table;
		this.controller = controller;
		this.updated = true;
		this.options = options;
		this.visualization = visualization;
		
		elements = new TableData();
		elements.setRetainedIndex(-1);
		table.setSelectMode(TableView.SelectMode.SINGLE);	
		table.getTableViewSelectionListeners().add(selectionChangeListener);
		
		// this is a compromise. Unfortunately the selection event also gets fired
		// by computationally selected rows. This causes infinity blink events.
		table.getComponentMouseButtonListeners().add(tableClickListener);
		controller.getGame().observe(this);
	}
	
	/**
	 * Updates the roboter list if necessary.
	 */
	public void update() {
		if (!updated)
			return;
		
		Map<String, ? extends IRobot> robots = controller.getGame().getRobots();
		
		elements.clear();
		
		for (Entry<String, ? extends IRobot> entry : robots.entrySet()) {
			TableElement element = new TableElement(
				entry.getValue(),
				options.isShowIdNotName()
			);
			
			elements.add(element);
		}

		table.setTableData(elements.getData());
		if (elements.getData().getLength() > elements.getRetainedIndex())
			table.setSelectedIndex(elements.getRetainedIndex());	
		else 
			elements.setRetainedIndex(-1);
		updated = false;
	}

	private TableViewSelectionListener selectionChangeListener = new TableViewSelectionListener() {
		@Override
		public void selectedRangeAdded(TableView view, int rangeStart, int rangeEnd) {}

		@Override
		public void selectedRangeRemoved(TableView view, int rangeStart, int rangeEnd) {}

		@Override
		public void selectedRangesChanged(TableView view, Sequence<Span> previousSelectedRanges) {}

		@Override
		public void selectedRowChanged(TableView view, Object previousSelectedRow) {
			if (elements.getData().getLength() != 0) {
				visualization.setSelectedRobotId(getSelected().getId());
				visualization.update();
				visualization.repaint();
				elements.setRetainedIndex(view.getSelectedIndex());
			}
		}		
	};
	

	private ComponentMouseButtonListener tableClickListener = new ComponentMouseButtonListener() {

		@Override
		public boolean mouseClick(Component table, Button button, int x, int y, int count) {
			if (button.equals(Button.LEFT) ) {
				TableElement elem = getSelected();
				if (elem != null) {
					controller.letRobotBlink(elem.getId());
				}
			}
			return false;
		}

		@Override
		public boolean mouseDown(Component table, Button button, int x, int y) {
			return false; /* not used */ 
		}

		@Override
		public boolean mouseUp(Component table, Button button, int x, int y) {
			return false; /* not used */ 
		}
		
	};

	/**
	 * Called through emitted events.
	 */
	@Override
	public void onModelUpdate(IEvent event) {
		if (event.getType() == IEvent.UpdateType.ROBOT_ADD)
		{
			((IRobot)event.getObject()).observe(this);
		}
		// update on ANY robot event.
		updated = true;
	}	
	
	/** 
	 * Returns contained data, which is updated regularly.
	 * @return TableData.
	 */
	public TableData getData() {
		return elements;
	}
	
	/**
	 * Returns the selected table element.
	 * @return TableElement.
	 */
	public TableElement getSelected() {
		if (table.getSelectedIndex() == -1)
			return null;
		return elements.getByIndex(table.getSelectedIndex());
	}
	
	/**
	 * Selects the robot in the table with the given id.
	 * Does nothing if Id is not found.
	 * @param id The robot id.
	 */
	public void selectRobotWithId(String id) {
		int index = elements.getIndexById(id);
		if (index == -1)
			return; // do nothing
		else {
			table.setSelectedIndex(index);
			controller.letRobotBlink(getSelected().getId());
		}
	}
	
	/**
	 * Places the selected robot at the given position.
	 * @param position A position in the stage.
	 */
	public void placeSelectedRobotAt(IPosition position) {
		TableElement elem = getSelected();
		if (elem != null) {
			controller.setRobotPosition(position.getX(), position.getY(), position.getOrientation(), elem.getId());
		}
	}

	/**
	 * Sets the new options.
	 * @param generalOptions A new generalOptions object.
	 */
	public void setOptions(GeneralOptions generalOptions) {
		options = generalOptions;		
	}
}
