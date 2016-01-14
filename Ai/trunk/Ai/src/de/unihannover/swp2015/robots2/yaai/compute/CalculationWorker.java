package de.unihannover.swp2015.robots2.yaai.compute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.yaai.YetAnotherAi;
import de.unihannover.swp2015.robots2.yaai.model.Graph;
import de.unihannover.swp2015.robots2.yaai.model.Node;

/**
 * Calculation Worker of the YAAI - Yet another AI.
 * 
 * This runnable worker should by started as thread by the AI main class and
 * will periodically recalculate a next field to drive to after a fixed
 * interval. The current position is provided asynchronously by the AI man
 * class, just as the calculated next field can be fetched.
 * 
 * @author Michael Thies
 */
public class CalculationWorker implements Runnable {
	private final YetAnotherAi ai;
	private final WeightCalculator weightCalculator;
	private final PathCalculator pathCalculator;
	private final Graph graph;
	private volatile IField startPosition;
	private volatile IField nextField;

	private Logger log = LogManager.getLogger(this.getClass().getName());

	/** Calculate path each XX ms */
	private final int CALCULATION_INTERVAL = 300;

	/**
	 * Construct a new calculation worker, working on the model of a given main
	 * controller and reporting to a given AI main class.
	 * 
	 * @param controller
	 *            The main controller its model will be used.
	 * @param ai
	 *            AI main class to be informed about each new calculation
	 */
	public CalculationWorker(IRobotController controller, YetAnotherAi ai) {
		this.graph = new Graph(controller.getGame().getStage());
		this.ai = ai;

		this.weightCalculator = new WeightCalculator(graph, controller);
		this.pathCalculator = new PathCalculator(graph, controller);
	}

	@Override
	public void run() {
		try {
			log.info(
					"Ai Worker started. Entering calculation loop with delay {}ms.",
					CALCULATION_INTERVAL);

			// Enter main loop
			while (true) {
				long startTime = System.currentTimeMillis();

				if (this.startPosition != null) {
					try {
						// Get start node
						Node startNode = this.graph.getNode(
								this.startPosition.getX(),
								this.startPosition.getY());

						// Execute! (if startPosition is valid)

						// Weight calculation
						this.weightCalculator.calculate(startNode);

						// Next field calculation
						Node n = this.pathCalculator.calculate(startNode);
						this.nextField = n.getField();

						log.trace(
								"New next field computed by Ai Worker: {}-{}",
								this.nextField.getX(), this.nextField.getY());

						// Inform AI about new calculated field
						this.ai.onNewFieldComputed();
					} catch (IndexOutOfBoundsException e) {
						log.info(
								"Path could not be calculated because start node is out of range",
								e);
					}
				}

				log.trace("Ai Worker used {}ms for calculation.",
						System.currentTimeMillis() - startTime);

				// Wait rest of interval for next execution
				long sleepTime = CALCULATION_INTERVAL
						- (System.currentTimeMillis() - startTime);
				if (sleepTime < 0) {
					log.debug(
							"Ai Worker calculation Interval to short. Needed {} ms",
							CALCULATION_INTERVAL - sleepTime);
				} else {
					Thread.sleep(sleepTime);
				}
			}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Get the latest next field calculated by this worker thread.
	 * 
	 * @return Next targeted field, as computed by latest calculation
	 */
	public IField getNextField() {
		return this.nextField;
	}

	/**
	 * Set the current robot position to be used as start field for next path
	 * calculation.
	 * 
	 * @param field
	 *            The field the robot is currently placed on (or driving to)
	 */
	public void setCurrentPosition(IField field) {
		this.startPosition = field;
		log.debug("New start position for Ai Worker: {}-{}", field.getX(),
				field.getY());
	}
}
