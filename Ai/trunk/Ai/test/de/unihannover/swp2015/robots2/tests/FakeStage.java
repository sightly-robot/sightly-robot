package de.unihannover.swp2015.robots2.tests;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.implementation.Field;
import de.unihannover.swp2015.robots2.model.implementation.Position;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition.Orientation;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class FakeStage implements IStage {

	private IField[][] fields;
	private List<IPosition> startPos;
	private int width;
	private int height;

	public FakeStage(int dimX, int dimY) {
		this.fields = new IField[dimX][dimY];
		this.width = dimX;
		this.height = dimY;

		this.startPos = new LinkedList<IPosition>();
		Random rand = new Random();
		this.startPos.add(new Position(rand.nextInt(dimX), rand.nextInt(dimY), Orientation.EAST));

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Field fld = new Field(i, j);
				/*
				 * Set walls
				 */
				if (i == 0) {
					fld.setWall(Orientation.WEST, true);
				}
				if (j == 0) {
					fld.setWall(Orientation.NORTH, true);
				}
				if (i == this.width - 1) {
					fld.setWall(Orientation.EAST, true);
				}
				if (j == this.height - 1) {
					fld.setWall(Orientation.SOUTH, true);
				}

				this.fields[i][j] = fld;
			}
		}
	}

	public FakeStage() {
		this.fields = new IField[3][3];
		this.width = 3;
		this.height = 3;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Field fld = new Field(i, j);
				/*
				 * Set walls
				 */
				if (i == 0) {
					fld.setWall(Orientation.WEST, true);
				}
				if (j == 0) {
					fld.setWall(Orientation.NORTH, true);
				}
				if (i == this.width - 1) {
					fld.setWall(Orientation.EAST, true);
				}
				if (j == this.height - 1) {
					fld.setWall(Orientation.SOUTH, true);
				}

				this.fields[i][j] = fld;
			}
		}

	}

	@Override
	public void observe(IModelObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	@Override
	public IField getField(int x, int y) {
		// TODO Auto-generated method stub
		return fields[x][y];
	}

	@Override
	public List<IPosition> getStartPositions() {
		// TODO Auto-generated method stub
		return this.startPos;
	}

}
