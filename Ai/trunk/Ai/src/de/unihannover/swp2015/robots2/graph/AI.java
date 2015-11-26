package de.unihannover.swp2015.robots2.graph;

import de.unihannover.swp2015.robots2.aiinterface.AbstractAi;
import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.interfaces.IField;

public class AI extends AbstractAi {
	
	private IField nextField;

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
	 * 
	 * @param field
	 * @return 
	 */
	public boolean occupy(IField field) {
		boolean isOccupied = true;
		
		return isOccupied;
	}
}