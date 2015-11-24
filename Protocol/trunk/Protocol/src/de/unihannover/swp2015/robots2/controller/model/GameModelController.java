package de.unihannover.swp2015.robots2.controller.model;

import de.unihannover.swp2015.robots2.model.writeableInterfaces.IGameWriteable;

public class GameModelController {

	private IGameWriteable game;
	
	public GameModelController(IGameWriteable game) {
		this.game = game;
	}
}
