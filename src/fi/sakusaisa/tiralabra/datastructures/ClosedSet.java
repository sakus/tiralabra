package fi.sakusaisa.tiralabra.datastructures;

import fi.sakusaisa.tiralabra.core.GridCell;

/**
 * A simple class to represent the closed set in a*
 * 
 * @author Saku Säisä
 *
 */
public class ClosedSet {

	private boolean[][] dataMatrix;
	
	/**
	 * Constructor that creates a new 2d-array to hold the data.
	 * The size should match the actual grid being used, as an index in this array matches
	 * the same index in the actual data grid 1:1.
	 * @param x The "x size" of the grid.
	 * @param y The "y size" of the grid.
	 */
	public ClosedSet(int x, int y) {
		dataMatrix = new boolean[x][y];
	}

	/**
	 * Clears the data structure.
	 */
	public void clear() {
		for (int i = 0; i < dataMatrix.length; i++) {
        	for (int j = 0; j < dataMatrix[0].length; j++) {
        		dataMatrix[i][j] = false;
        	}
        }
	}
	
	/**
	 * Inserts a GridCell into the data structure by marking
	 * the index of the said GridCell as true.
	 * 
	 * @param gridCell The GridCell to add.
	 */
	public void insert(GridCell gridCell) {
		dataMatrix[gridCell.getCellX()][gridCell.getCellY()] = true;
	}
	
	/**
	 * Checks whether or not a GridCell exists in the data structure.
	 * 
	 * @param gridCell the GridCell to check
	 * @return True if gound, false otherwise
	 */
	public boolean contains(GridCell gridCell) {

		int x = gridCell.getCellX();
		int y = gridCell.getCellY();
		
		if (x > -1 && y > -1 && x < dataMatrix.length && y < dataMatrix[0].length)
			return (dataMatrix[x][y] == true);
		else
			return false;
	}

	/**
	 * Provides a direct access to the underlying data array.
	 * 
	 * @return A reference to this instances data array.
	 */
	public boolean[][] getDataMatrix() {
		return this.dataMatrix;
	}
	
}
