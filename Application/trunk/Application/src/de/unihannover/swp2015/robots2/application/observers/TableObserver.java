package de.unihannover.swp2015.robots2.application.observers;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.pivot.wtk.TableView;

import de.unihannover.swp2015.robots2.application.models.GeneralOptions;
import de.unihannover.swp2015.robots2.application.models.TableData;
import de.unihannover.swp2015.robots2.application.models.TableElement;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;

/**
 * Automatic event controlled updater for the robot list. 
 * @author Tim Ebbeke
 */
public class TableObserver implements IModelObserver {
	private TableView table;
	private GuiMainController controller;
	private volatile boolean updated;
	private volatile TableData elements;
	private GeneralOptions options;
	
	public TableObserver(TableView table, GuiMainController controller, GeneralOptions options) {
		this.table = table;
		this.controller = controller;
		this.updated = true;
		this.options = options;
		
		elements = new TableData();
		table.setSelectMode(TableView.SelectMode.SINGLE);
		
		controller.getGame().observe(this);
	}
	
	/**
	 * Updates the roboter list if necessary.
	 */
	public void update() {
		if (!updated)
			return;
		
		Map<String, ? extends IRobot> robots = controller.getGame().getRobots();
		
		int lastIndex = table.getSelectedIndex();
		
		elements.clear();
		
		for (Entry<String, ? extends IRobot> entry : robots.entrySet()) {
			TableElement element = new TableElement(
				entry.getValue(),
				options.isShowIdNotName()
			);
			
			elements.add(element);
		}
		
		Robot dummy = new Robot("12345678", true, false);
		dummy.setRobotState(RobotState.CONNECTED);
		elements.add(new TableElement(dummy, options.isShowIdNotName()));

		table.setTableData(elements.getData());
		if (elements.getData().getLength() > lastIndex)
			table.setSelectedIndex(lastIndex);	
		updated = false;
	}

	/**
	 * Called through emitted events.
	 */
	@Override
	public void onModelUpdate(IEvent event) {
		if (event.getType() == IEvent.UpdateType.ROBOT_ADD)
		{
			((IRobot)event.getObject()).observe(this);
			updated = true;
		}
		if (event.getType() == IEvent.UpdateType.ROBOT_DELETE)
			updated = true;
		if (event.getType() == IEvent.UpdateType.ROBOT_SCORE)
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
		else
			table.setSelectedIndex(index);
	}
}
