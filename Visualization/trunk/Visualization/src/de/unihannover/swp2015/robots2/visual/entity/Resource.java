package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * An entity used for the visualization of resources
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public class Resource extends Entity {

	private int stage = 10; //default? ggf noch ändern
	
	public Resource(){
		this.texture = new Texture(Gdx.files.internal("resource10.png")); //Dateinamen ggf noch anpassen
	}
	
	public void setPosition(int x, int y){ //oder gleich beim Erstellen die Position mitgeben?
		this.renderX=x;
		this.renderY=y;
	}
	
	public void hide(){
		//TODO Optional
	}
	
	public void updateStage(int stage){	
		this.stage=stage;
		this.texture = new Texture(Gdx.files.internal("resource"+stage+".png")); //Dateinamen ggf noch anpassen
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onModelUpdate(IEvent event) {
		// TODO Auto-generated method stub
		
	}
}
