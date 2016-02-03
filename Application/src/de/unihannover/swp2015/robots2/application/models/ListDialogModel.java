package de.unihannover.swp2015.robots2.application.models;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;

import de.unihannover.swp2015.robots2.model.interfaces.IRobot;

public class ListDialogModel {
	private List <String> printables;
	private List <String> robotIds;
	
	public ListDialogModel(GeneralOptions options, java.util.List<IRobot> robots) {
		super();
		printables = new ArrayList <>();
		robotIds = new ArrayList <>();
		
		for (IRobot robot : robots) {
			if (!options.isShowIdNotName())
				printables.add(robot.getName());
			else
				printables.add(robot.getId());
			robotIds.add(robot.getId());
		}
	}

	public List<String> getPrintables() {
		return printables;
	}

	public List<String> getRobotIds() {
		return robotIds;
	}
}
