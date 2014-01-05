package fi.sakusaisa.tiralabra;

/**
 * A simple class to represent the closed set in a*
 * 
 * @author Saku Säisä
 *
 */
public class ClosedSet {

	private boolean[][] dataMatrix;
	
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
	 * Inserts a GridCell into the data structure.
	 * @param gridCell The GridCell to add.
	 */
	public void insert(GridCell gridCell) {
		dataMatrix[gridCell.getCellX()][gridCell.getCellY()] = true;
	}
	
	/**
	 * Checks whether or not a GridCell exists in the data structure.
	 * @param gridCell the GridCell to check
	 * @return True if gound, false otherwise
	 */
	public boolean contains(GridCell gridCell) {
		return (dataMatrix[gridCell.getCellX()][gridCell.getCellY()] == true);
	}
}
