package de.unihannover.swp2015.robots2.visual.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import de.unihannover.swp2015.robots2.model.implementation.Field;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;

/**
 * One-class swing app to test the reaction of the visualization when making changes to the IGame object.
 * @author rschr
 *
 */
public class TestApp extends JFrame implements ActionListener {

	
	private static final long serialVersionUID = 1L;
	
	IGame game;
	JButton changeRobotPos;
	JButton changeRobots;
	JButton changeWalls;
	JButton changeFood;
	
	public TestApp(IGame game) {
		this.game = game;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.changeRobotPos = new JButton("changeRobotPos");
		changeRobotPos.setBounds(0, 0, 200, 50);
		changeRobotPos.addActionListener(this);
		
		this.changeRobots = new JButton("changeRobots");
		changeRobots.setBounds(0, 50, 200, 50);
		changeRobots.addActionListener(this);
		
		this.changeWalls = new JButton("changeWalls");
		changeWalls.setBounds(0, 100, 200, 50);
		changeWalls.addActionListener(this);

		this.changeFood = new JButton("changeFood");
		changeFood.setBounds(0, 150, 200, 50);
		changeFood.addActionListener(this);

		this.add(changeRobotPos);
		this.add(changeRobots);
		this.add(changeWalls);
		this.add(changeFood);
		
		this.setSize(400, 400);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.changeFood) {
			final IStage stage = game.getStage();
			for (int x = 0; x < stage.getWidth(); ++x) {
				for (int y = 0; y < stage.getHeight(); ++y) {
					final Field f = (Field) stage.getField(x, y);
					int r = (int)(Math.random()*41);
					r -= 30;
					if (r < 0)
						r = 0;
					f.setFood(r);
				}
			}
		}
		else if (e.getSource() == this.changeWalls) {
			final IStage stage = game.getStage();
			for (int x = 0; x < stage.getWidth(); ++x) {
				for (int y = 0; y < stage.getHeight(); ++y) {
					final Field f = (Field) stage.getField(x, y);
					f.setWall(Orientation.NORTH, rb());
					f.setWall(Orientation.SOUTH, rb());
					f.setWall(Orientation.EAST, rb());
					f.setWall(Orientation.WEST, rb());
				}
			}
		}
	}
	
	private boolean rb() {
		final int des = (int)(Math.random()*10);
		if (des == 9)
			return true;
		return false;
	}
	
	
	
}
