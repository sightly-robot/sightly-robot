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

	private static final Logger LOGGER = LogManager.getLogger(CalculationWorker.class.getName());

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
		this.pathCalculator = new PathCalculator();
	}

	@Override
	public void run() {
		try {
			LOGGER.info(
					"Ai Worker started. Entering calculation loop with delay {}ms.",
					CALCULATION_INTERVAL);

			// Enter main loop
			while (true) {
				long startTime = System.currentTimeMillis();

				this.calculate();
				
				LOGGER.trace("Ai Worker used {}ms for calculation.",
						System.currentTimeMillis() - startTime);

				// Wait rest of interval for next execution
				long sleepTime = CALCULATION_INTERVAL
						- (System.currentTimeMillis() - startTime);
				if (sleepTime < 0) {
					LOGGER.debug(
							"Ai Worker calculation Interval to short. Needed {} ms",
							CALCULATION_INTERVAL - sleepTime);
				} else {
					Thread.sleep(sleepTime);
				}
			}
		} catch (InterruptedException e) {
			LOGGER.info(
					"Ai Worker stopped by interrupt.");
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
		LOGGER.debug("New start position for Ai Worker: {}-{}", field.getX(),
				field.getY());
	}
	
	/**
	 * Do the actual calculation.
	 */
	private void calculate() {
		if (this.startPosition == null)
			return;
		
		try {
			// Get start node
			Node startNode = this.graph.getNode(
					this.startPosition.getX(),
					this.startPosition.getY());

			// Prevent concurrent modification of the graph.
			synchronized (this.graph) {
				// Weight calculation
				this.weightCalculator.calculate(startNode);

				// Next field calculation
				Node n = this.pathCalculator.calculate(startNode);
				this.nextField = n.getField();
			}

			LOGGER.trace(
					"New next field computed by Ai Worker: {}-{}",
					this.nextField.getX(), this.nextField.getY());

			// Inform AI about new calculated field
			this.ai.onNewFieldComputed();
		} catch (IndexOutOfBoundsException e) {
			LOGGER.info(
					"Path could not be calculated because start node is out of range",
					e);
		}
	}
}
