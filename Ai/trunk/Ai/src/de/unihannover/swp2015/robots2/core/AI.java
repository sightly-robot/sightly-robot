package de.unihannover.swp2015.robots2.core;

import de.unihannover.swp2015.robots2.aiinterface.AbstractAi;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.graph.AIGraph;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

public class AI extends AbstractAi {
	
	private IField nextField;
	private AIGraph graph;
	
	public AI(IRobotController controller) { // controller from data modell
		super(controller);
	}

	@Override
	public void setRelativeSpeed(double foreward, double sideward, double backward) {
		// TODO Auto-generated method stub

	}
	
	// Backwards not in increment 1
	public IField getRandomField() {
		return null;
	}
	/**
	 * Returns the next Orientation, the robot i supposed to move in.
	 * 
	 * @return Orientation, the robot is supposed to move in next.
	 */
	public Orientation getNextOrientation() {
		return this.graph.getRandomOrientation();
	}
	
	/**
	 * 
	 * @param field
	 * @return 
	 */
	public boolean occupy(IField field) {
		boolean isOccupied = true;
		
		return isOccupied;
	}
}