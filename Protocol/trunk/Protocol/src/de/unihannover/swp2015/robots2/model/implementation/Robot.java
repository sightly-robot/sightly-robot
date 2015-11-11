package de.unihannover.swp2015.robots2.model.implementation;

import java.awt.Color;

import de.unihannover.swp2015.robots2.external.implementation.Position.Orientation;
import de.unihannover.swp2015.robots2.external.interfaces.IModelObserver;
import de.unihannover.swp2015.robots2.external.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Robot extends AbstractModel implements IRobot, IRobotWriteable {

	private String id;
	private String name;
	private boolean hardwareRobot;
	private IPosition position;
	private int score;
	private boolean setupState;
	private boolean myself;
	private Color color;
	
	@Override
	public void observe(IModelObserver observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void emitEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPosition(int x, int y, Orientation orientation) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public void setSetupState(boolean setupState) {
		this.setupState = setupState;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isHardwareRobot() {
		return this.hardwareRobot;
	}

	@Override
	public IPosition getPosition() {
		return this.position;
	}

	@Override
	public int getScore() {
		return this.score;
	}

	@Override
	public boolean isSetupState() {
		return this.setupState;
	}

	@Override
	public boolean isMyself() {
		return this.myself;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

}
