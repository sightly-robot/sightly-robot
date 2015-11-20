package de.unihannover.swp2015.robots2.visual.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import de.unihannover.swp2015.robots2.model.interfaces.IEvent;

/**
 * An entity used for the visualization of robots
 * 
 * @version 0.1
 * @author Daphne Schössow
 */
public class Robot extends Entity {
		private boolean isVirtual;
		
		public Robot(){
			this.texture = new Texture(Gdx.files.internal("robot.png")); //Dateinamen ggf noch anpassen
		}
		
		public void setPosition(int x, int y){
			changeDirection();
			this.renderX=x;
			this.renderY=y;
		}
		
		private void changeDirection(){
			//TODO
		}
		
		public void hide(){
			//TODO Optional
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
