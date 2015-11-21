package de.unihannover.swp2015.robots2.visual.entity;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * An entity used for the visualization of 
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public class Field extends Entity {
	private boolean[] walls = new boolean[4];
	
	public Field(boolean[] walls){
		this.walls=walls;
		this.texture= = new Texture(Gdx.files.internal("tile"+walls[0]+walls[1]+walls[2]+walls[3]+".png")); //Dateinamen ggf noch anpassen
		setWalls();
	}
	
	public void setWalls(){
		//TODO
	}
	
	@Override
	public void setPosition(int x, int y) {
		this.renderX=x;
		this.renderY=y;
	}

	@Override
	public void hide() {
		// TODO Optional
	}
	
	@Override
	public void render() {
		RobotGameHandler.spriteBatch.begin();
		RobotGameHandler.spriteBatch.draw(texture, x, y);
		RobotGameHandler.spriteBatch.end();		
	}

	@Override
	public void onModelUpdate(IEvent event) {
		// TODO Auto-generated method stub
	}

}
