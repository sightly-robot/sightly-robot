package de.unihannover.swp2015.robots2.tests;

import java.util.List;

import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.implementation.Field;
import de.unihannover.swp2015.robots2.model.interfaces.IField;
import de.unihannover.swp2015.robots2.model.interfaces.IPosition;
import de.unihannover.swp2015.robots2.model.interfaces.IStage;

public class FakeStage implements IStage {

	private IField[][] fields;
	private int width;
	private int height;

	public FakeStage(int dimX, int dimY) {
		this.fields = new IField[dimX][dimY];
		this.width = dimX;
		this.height = dimY;
	}

	public FakeStage() {
		this.fields = new IField[3][3];
		this.width = 3;
		this.height = 3;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Field fld = new Field(i, j);
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
		return null;
	}

}
