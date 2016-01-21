package de.unihannover.swp2015.robots2.visual.util.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.badlogic.gdx.Gdx;

import de.unihannover.swp2015.robots2.model.implementation.Field;
import de.unihannover.swp2015.robots2.model.implementation.Game;
import de.unihannover.swp2015.robots2.model.implementation.Robot;
import de.unihannover.swp2015.robots2.model.implementation.Stage;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent.UpdateType;
import de.unihannover.swp2015.robots2.model.interfaces.IGame;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot;
import de.unihannover.swp2015.robots2.model.interfaces.IRobot.RobotState;

/**
 * One-class swing app to test the reaction of the visualization when making
 * changes to the IGame object.
 * 
 * @author Rico Schrage
 */
public class TestApp extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Random rand = new Random();

	private final IGame game;
	private final JButton changeRobotPos;
	private final JButton changeRobots;
	private final JButton changeWalls;
	private final JButton changeFood;
	private final JButton changeSize;
	private final JButton startstop;

	/**
	 * Constructs the test app.
	 * 
	 * @param debugGame
	 *            game instance
	 */
	public TestApp(IGame debugGame) {
		this.game = debugGame;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.changeRobotPos = new JButton("changeRobotPos");
		this.changeRobotPos.setBounds(0, 0, 200, 50);
		this.changeRobotPos.addActionListener(this);

		this.changeRobots = new JButton("addRobot");
		this.changeRobots.setBounds(0, 50, 200, 50);
		this.changeRobots.addActionListener(this);

		this.changeWalls = new JButton("changeWalls");
		this.changeWalls.setBounds(0, 100, 200, 50);
		this.changeWalls.addActionListener(this);

		this.changeFood = new JButton("changeFood");
		this.changeFood.setBounds(0, 150, 200, 50);
		this.changeFood.addActionListener(this);

		this.changeSize = new JButton("changeSize");
		this.changeSize.setBounds(0, 200, 200, 50);
		this.changeSize.addActionListener(this);

		this.startstop = new JButton("startstop");
		this.startstop.setBounds(0, 250, 200, 50);
		this.startstop.addActionListener(this);

		this.add(changeRobotPos);
		this.add(changeRobots);
		this.add(changeWalls);
		this.add(changeFood);
		this.add(changeSize);
		this.add(startstop);

		this.setSize(400, 400);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == changeFood) {
			changeFood();
		} else if (e.getSource() == changeWalls) {
			changeWalls();
		} else if (e.getSource() == changeRobotPos) {
			final Map<String, ? extends IRobot> r = game.getRobots();
			final Collection<? extends IRobot> s = r.values();
			for (IRobot ro : s) {
				final Robot rob = (Robot) ro;
				rob.setPosition(rand.nextInt(game.getStage().getWidth()),
						(int) (Math.random() * game.getStage().getHeight()), randomOrientation());
				rob.emitEvent(UpdateType.ROBOT_POSITION);
				rob.setRobotState(RobotState.ROBOTICS_ERROR);
				rob.emitEvent(UpdateType.ROBOT_STATE);
			}
		} else if (e.getSource() == changeRobots) {
			final Game g = (Game) game;
			final Robot robo = new Robot(UUID.randomUUID().toString(), true, true);
			if (!game.getStage().getStartPositions().isEmpty()) {
				final List<IPosition> startposition = game.getStage().getStartPositions();
				for (IPosition pos : startposition) {
					robo.setPosition(pos.getX(), pos.getY(), pos.getOrientation());
					break;
				}
			} else {
				robo.setPosition(1, 1, Orientation.EAST);
			}
			g.addRobot(robo);
			g.emitEvent(UpdateType.ROBOT_ADD, robo);
		} else if (e.getSource() == changeSize) {
			final Stage g = (Stage) game.getStage();
			g.changeSize(8, 6);
			g.addStartPosition(2, 2, Orientation.EAST);
			g.emitEvent(UpdateType.STAGE_SIZE);
		} else if (e.getSource() == startstop) {
			startStop();
		}
	}

	private void changeFood() {
		final IStage stage = game.getStage();
		for (int x = 0; x < stage.getWidth(); ++x) {
			for (int y = 0; y < stage.getHeight(); ++y) {
				final Field f = (Field) stage.getField(x, y);
				int r = rand.nextInt(42);
				r -= 32;
				if (r < 0)
					r = 0;
				f.setFood(r);
			}
		}
	}

	private void changeWalls() {
		final Stage stage = (Stage) game.getStage();
		for (int x = 0; x < stage.getWidth(); ++x) {
			for (int y = 0; y < stage.getHeight(); ++y) {
				final Field f = (Field) stage.getField(x, y);
				f.setWall(Orientation.NORTH, TestApp.rb());
				f.setWall(Orientation.SOUTH, TestApp.rb());
				f.setWall(Orientation.EAST, TestApp.rb());
				f.setWall(Orientation.WEST, TestApp.rb());
				f.emitEvent(UpdateType.FIELD_FOOD);
			}
		}
		stage.emitEvent(UpdateType.STAGE_WALL);
	}

	private void startStop() {
		final Game g = (Game) game;
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				g.setRunning(!g.isRunning());
				g.emitEvent(UpdateType.GAME_STATE);
			}

		});
	}

	private Orientation randomOrientation() {
		int rd = rand.nextInt(4);
		switch (rd) {
		case 1:
			return Orientation.NORTH;
		case 2:
			return Orientation.EAST;
		case 3:
			return Orientation.WEST;
		default:
			return Orientation.SOUTH;
		}
	}

	private static boolean rb() {
		final int des = rand.nextInt(10);
		if (des == 9)
			return true;
		return false;
	}

}
