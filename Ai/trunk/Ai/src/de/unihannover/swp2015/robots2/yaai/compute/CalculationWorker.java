package de.unihannover.swp2015.robots2.yaai.compute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.unihannover.swp2015.robots2.controller.interfaces.IRobotController;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.yaai.YetAnotherAi;
import de.unihannover.swp2015.robots2.yaai.model.Graph;
import de.unihannover.swp2015.robots2.yaai.model.Node;

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

			while (true) {
				long startTime = System.currentTimeMillis();

				if (this.startPosition != null) {
					Node startNode = this.graph.getNode(
							this.startPosition.getX(),
							this.startPosition.getY());

					// Execute! (if startPosition is valid)
					this.weightCalculator.calculate(startNode);
					Node n = this.pathCalculator.calculate(startNode);
					this.nextField = n.getField();

					log.trace("New next field computed by Ai Worker: {}-{}",
							this.nextField.getX(), this.nextField.getY());
					this.ai.onNewFieldComputed();
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

	public IField getNextField() {
		return this.nextField;
	}

	public void setCurrentPosition(IField field) {
		this.startPosition = field;
		log.debug("New start position for Ai Worker: {}-{}", field.getX(),
				field.getY());
	}
}
