package de.unihannover.swp2015.robots2.model.implementation;

import java.awt.Color;

import de.unihannover.swp2015.robots2.model.interfaces.*;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IPositionWritable;
import de.unihannover.swp2015.robots2.model.writeableInterfaces.IRobotWriteable;;

/**
 * 
 * @version 0.1
 * @author Patrick Kawczynski
 */
public class Robot extends AbstractModel implements IRobot, IRobotWriteable {

	private final String id;
	private String name;
	private final boolean hardwareRobot;
	private final IPositionWritable position;
	private int score;
	private final Object scoreLock;
	private volatile boolean setupState;
	private volatile boolean errorState;
	private final boolean myself;
	
	public Robot(String id, boolean hardwareRobot, boolean myself) {
		super();
		
		this.id = id;
		this.myself = myself;
		this.hardwareRobot = hardwareRobot;
		this.position = new Position(0, 0, Orientation.NORTH);
		this.scoreLock = new Object();
	}
	
	public Robot(String id, boolean hardwareRobot) {
		this(id,hardwareRobot,false);
	}

	@Override
	public void setPosition(int x, int y, IPosition.Orientation orientation) {
		this.position.setPosition(x, y);
		this.position.setOrientation(orientation);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setSetupState(boolean setupState) {
		this.setupState = setupState;
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
		synchronized (this.scoreLock) {
			return this.score;
		}
	}
	
	@Override
	public void setScore(int score) {
		synchronized (this.scoreLock) {
			this.score = score;
		}
	}

	@Override
	public int addScore(int score) {
		synchronized (this.scoreLock) {
			this.score += score;
			return this.score;
		}
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
		// TODO get color from ID hash.
		return Color.black;
	}

	@Override
	public boolean isErrorState() {
		return this.errorState;
	}

}
