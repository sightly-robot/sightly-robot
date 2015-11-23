package de.unihannover.swp2015.robots2.hardwarerobot.automate;

public enum State {
	
	SHUTDOWN_STATE(-2),
	PAUSE_STATE(-1),
	LINE_FOLLOW_STATE(0),
	DRIVE_ON_CELL_STATE(1),
	WAIT_STATE(2),
	TURN_LEFT_1_STATE(3),
	TURN_LEFT_2_STATE(4),
	TURN_RIGHT_1_STATE(5),
	TURN_RIGHT_2_STATE(6),
	TURN_180_1_STATE(7),
	TURN_180_2_STATE(8),
	TURN_180_3_STATE(9),
	TURN_180_4_STATE(10);
	
	public int id = -1;
	
	private State(int id)
	{
		this.id = id;
	}

}
