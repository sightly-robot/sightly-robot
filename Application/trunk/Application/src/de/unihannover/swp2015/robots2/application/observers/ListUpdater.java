package de.unihannover.swp2015.robots2.application.observers;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.wtk.TableView;

import de.unihannover.swp2015.robots2.application.models.TableElement;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

/**
 * Automatic event controlled updater for the robot list. 
 * @author Tim Ebbeke
 */
public class ListUpdater implements IModelObserver {
	private TableView table;
	private GuiMainController controller;
	private volatile boolean updated;
	List <TableElement> elements;
	
	public ListUpdater(TableView table, GuiMainController controller) {
		this.table = table;
		this.controller = controller;
		this.elements = new ArrayList <>();
		this.updated = false;
		
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
				entry.getValue().getName(),
				entry.getValue().getScore()
			);
			
			elements.add(element);
		}
		
		table.setTableData(elements);
	}

	/**
	 * Called through emitted events.
	 */
	@Override
	public void onModelUpdate(IEvent event) {
		if (event.getType() == IEvent.UpdateType.ROBOT_ADD)
		{
			((IRobot)event.getObject()).observe(this);
		}
		
		updated = true;
	}	
}