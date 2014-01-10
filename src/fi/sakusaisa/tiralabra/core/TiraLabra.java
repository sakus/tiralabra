package fi.sakusaisa.tiralabra.core;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fi.sakusaisa.tiralabra.datastructures.ClosedSet;
import fi.sakusaisa.tiralabra.datastructures.MinBinaryHeap;
import fi.sakusaisa.tiralabra.ui.GridRenderer;
import fi.sakusaisa.tiralabra.ui.UiPanel;

/**
 * The main TiraLabra class.
 * 
 * @author Saku Säisä
 */
@SuppressWarnings("serial")
public class TiraLabra extends JFrame {


     // The data for all cells in the grid is held in this 2d-array.
    private GridCell[][] gridCells;
        
    // closed set as a simple boolean matrix
    private ClosedSet closedSet;

    // open set as a custom binary heap implementation
    private MinBinaryHeap openSet;
    
    // random variables..
    private GridRenderer gridRenderer;
    private int wantedWindowWidth;
	private int wantedWindowHeight;
    private int startCellX;
	private int startCellY;
	private int goalCellX;
	private int goalCellY;    
    private int cellSize = 10;
    private boolean pathFindingRan = false;  
    private boolean diagonalMoveAllowed = false;
    private boolean useTieBreaker = false;
    protected String statusMessage1, statusMessage2, statusMessage3;
    private boolean useAStar = false;
    protected int nodesChecked = 0;

    /**
     * Checks whether or not a cell is within the grid
     * @param x the x to check
     * @param y the y to check
     * @return true if the cell is OK, false otherwise
     */
    public boolean cellIsOkay(int x, int y) {
        return (x > -1 && x < gridCells.length && y > -1 && y < gridCells[0].length);
    }    
    
    /**
     * Initializes the grid
     */
    public void resetGrid() {
        
        // initialize the grid
        for (int i = 0; i < gridCells.length; i++) {
            for (int j = 0; j < gridCells[0].length; j++)
                gridCells[i][j] = new GridCell(i, j);
        }
        
        // throw in default start and goal cells
        setStartCellX(1); setStartCellY(1); setGoalCellX(gridCells.length - 2); setGoalCellY(gridCells[0].length - 2);
        gridCells[getStartCellX()][getStartCellY()].setCellData(4);
        gridCells[getGoalCellX()][getGoalCellY()].setCellData(5);
        
        resetStats();
        
    }

    /**
     * Resets the found path preserving the obstacles and current start/goal positions
     */
    public void resetPath() {
    
        for (int i = 0; i < gridCells.length; i++) {
            for (int j = 0; j < gridCells[0].length; j++) {

                // replace the consider path visualization with empty space
                if (gridCells[i][j].getCellData() == 3 || gridCells[i][j].getCellData() == 2)
                    gridCells[i][j].setCellData(1);
                
                // reset all the pathfinding data back to unset values
                gridCells[i][j].setArrivedFrom(null);
                gridCells[i][j].setDistanceFromStart(-1);
                gridCells[i][j].setDistanceToGoal(-1);
                gridCells[i][j].setMovementCost(-1);
                                
            }
                    
        }
        
        resetStats();
        
    }
    
    private void resetStats() {
        setPathFindingRan(false);
        statusMessage1 = null;
        statusMessage2 = null;
        statusMessage3 = null;
        nodesChecked = 0;	
    }
    
    /**
     * This is where the A* magic happens.
     */
    public void findPath() {
        
        // record the starting time for performance benchmarking
        long startTime = System.currentTimeMillis();

        // reset the path if needed
        if (isPathFindingRan())
            resetPath();        
        setPathFindingRan(true);

        // clear the sets
        closedSet.clear();
        openSet.clear(true);
        
        // grab the starting cell first
        GridCell currentCell = gridCells[getStartCellX()][getStartCellY()];
        currentCell.setDistanceFromStart(0);
        openSet.insert(currentCell);
 
        // loop until we arrive at the target or the open set becomes empty
        while (currentCell != gridCells[getGoalCellX()][getGoalCellY()] && !openSet.isEmpty()) {
        	
            // take the cell with the smallest movementCost and move it over to closedSet
        	currentCell = openSet.delMin();
            closedSet.insert(currentCell);
            
            // check adjacent cells
            findPathProcessAdjacentCells(currentCell);
            
        }
                
        // visualize the path following hte arrivedFrom flags starting from the goal 
        if (currentCell == gridCells[getGoalCellX()][getGoalCellY()]) {
        	visualizePath(currentCell);
        }

        // is this statusmessage hasn't been set, we know there was no path found
        if (statusMessage2 == null) statusMessage2 = "no path!";
        
        // check the time it took to find the path and visualize it
        long endTime = System.currentTimeMillis();
        statusMessage1 = "\nTime taken: " + (endTime - startTime) + "ms";
        statusMessage3 = "Nodes checked: " + nodesChecked;
        System.out.println(statusMessage1);
        System.out.println(statusMessage2);
        System.out.println(statusMessage3);

    } 

    /**
     * Visualizes the found path by setting the correct content data on each cell along the path
     */
    public void visualizePath(GridCell currentCell) {
    	
        int pathLength = 0;
        
        while (currentCell.getArrivedFrom() != gridCells[getStartCellX()][getStartCellY()]) {
            currentCell.getArrivedFrom().setCellData(2);
            currentCell = currentCell.getArrivedFrom();
            pathLength++;
        }
        
        statusMessage2 = "Path length: " + pathLength;
    	
    }
    
    /**
     * Process the adjacent cells of the current cell
     */
    public void findPathProcessAdjacentCells(GridCell currentCell) {
    	
        // update the helper variables
        int currentX = currentCell.getCellX();
        int currentY = currentCell.getCellY();

        // process the adjacent cells (don't touch the current cell itself)
        for (int y = currentY -1; y <= currentY + 1; y++) {
            for (int x = currentX - 1; x <= currentX + 1; x++) {
                
            	/* if diagonal move is allowed, check cells -1 to +1 in x/y
            	 * that either have a different x or a different y than the processed cell..
            	 * this results in 8 cells being processed
            	 */
                if (isDiagonalMoveAllowed()) {
                    if (x != currentX || y != currentY) {
                        findPathProcessCell(x, y, currentCell);
                    }
                }
                
                /* if diagonal move is disabled, then only check the x and y where either
                 * X or Y (but not both) is the same as the processed cell.. this checks
                 * 4 of the adjacent cells: directly above, below, left and right
                 */
                else if (!isDiagonalMoveAllowed()) {
                    if (x == currentX || y == currentY && !(x == currentX && y == currentY)) {
                        findPathProcessCell(x, y, currentCell);
                    }
                }
                
            }
        }

    }
    
    /**
     * processes a cell as a part of the A* algorithm as long as the cell:
     * - actually exists on the grid,
     * - isn't marked as an obstacle and
     * - isn't already in the closed set
     * 
     * @param processX the X of the cell to be processed
     * @param processY the Y of the cell to be processed
     * @param currentCell a reference to the "current cell" of which this one is adjacent to
     */
    public void findPathProcessCell(int processX, int processY, GridCell currentCell) {

    	int foundAtIndex = -1;
    	
        // first check if the cell even exists - if not, don't do anything else
        if (!cellIsOkay(processX, processY))
            return;
        
        // grab a reference to the considered cell
        GridCell processCell = gridCells[processX][processY];

        if (processCell.getCellData() != 0 && !closedSet.contains(processCell)) {
        	
            // if the cell isn't in the open set..
        	foundAtIndex = openSet.contains(processCell);
        	if (foundAtIndex == -1) {
        		
                // mark the cell as checked, for visualization
                if (processCell.getCellData() == 1) {
                    processCell.setCellData(3);
                    nodesChecked++;
                }

                // set the parent and path length so far
                processCell.setArrivedFrom(currentCell);
                processCell.setDistanceFromStart(currentCell.getDistanceFromStart() + 1);
                
                // heuristics if A*
                if (isUseAStar())
                	processCell.setDistanceToGoal(aStarHeuristic(processCell));
                else
                	processCell.setDistanceToGoal(0);
                
                // the combined value of path lenght + approx. length to goal used for evaluating the next step
                processCell.setMovementCost(processCell.getDistanceFromStart() + processCell.getDistanceToGoal());

                // add the cell to openSet; as it is a binaby min heap, the best option will be on top
                openSet.insert(processCell);
                
            }

            // if the cell is in the open set already..
            else {
                
                // check if this way to the cell is shorter than the previously found one
                if ((currentCell.getDistanceFromStart() + 1) < processCell.getDistanceFromStart()) {
                
                	// if so, update the information - need to remove from openSet first, then re-add.. SLOW!
                	openSet.removeAtIndex(foundAtIndex);
                	processCell.setArrivedFrom(currentCell);
                	processCell.setDistanceFromStart(currentCell.getDistanceFromStart() + 1);
                
                	// heuristics if A*
                	if (isUseAStar())
                		processCell.setDistanceToGoal(aStarHeuristic(processCell));
                	else
                		processCell.setDistanceToGoal(0);
                
                	processCell.setMovementCost(processCell.getDistanceFromStart() + processCell.getDistanceToGoal());
                	openSet.insert(processCell);

                }

            }

        }

    }
    
    /**
     * The heuristics function for A*.
     * 
     * @param processCell the cell we're processing
     * @return the distance to the goal as determined by the heuristic function
     */
    public float aStarHeuristic(GridCell processCell) {    	

    	int dx = Math.abs(processCell.getCellX() - getGoalCellX());
    	int dy = Math.abs(processCell.getCellY() - getGoalCellY());
    	
    	float tieBreaker;
    	
    	if (isUseTieBreaker()) {
    		tieBreaker = 1.001f;
    	}
    	
    	else {
    		tieBreaker = 1f;
    	}
    		
    	// manhattan distance when diagonal move disabled
    	if (!isDiagonalMoveAllowed())
    		return (1 * (dx + dy)) * tieBreaker;
    	
    	// Chebyshev distance when diagonal move enabled
    	else {
    		//return (1 * Math.max(dx, dy)) * tieBreaker;
    		return (float) ((1 * (dx + dy) + (Math.sqrt(2)*1 - 2*1) * Math.min(dx, dy)) * tieBreaker);
    	}
    		    
    }
    
    public GridCell[][] getGridCells() {
        return this.gridCells;
    }
    
    public int getCellSize() {
        return this.cellSize;
    }
    
    /**
     * Constructor.
     * 
     * @param gridXSize The size of the grid in x axis.
     * @param gridYSize The size of the grid in y axis.
     */
    public TiraLabra(int gridXSize, int gridYSize) {
        
    	// create the grid
    	 gridCells = new GridCell[gridXSize][gridYSize];
    	 
        // define dimensions for the program window taking into account some padding + window decorations
        setWantedWindowWidth(gridCells.length * cellSize + 70 + getInsets().left + getInsets().right);
        setWantedWindowHeight(gridCells[0].length * cellSize + 20 + getInsets().top + getInsets().bottom);
        
        // program window properties
        setTitle("TiRaLabra");
        setSize(getWantedWindowWidth(), getWantedWindowHeight());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        // initialize data
        resetGrid();
        openSet = new MinBinaryHeap(100);
        closedSet = new ClosedSet(gridCells.length, gridCells[0].length);
                
        // set the JPanel for the grid renderer
        setGridRenderer(new GridRenderer(this));
        getContentPane().add(getGridRenderer(), java.awt.BorderLayout.WEST);
        
        // build the UI into another JPanel
        JPanel uiPanel = new UiPanel(this);
        getContentPane().add(uiPanel, java.awt.BorderLayout.EAST);

        // finalize the jframe
        pack();
        setVisible(true);
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    	final int x = Integer.parseInt(args[0]);
    	final int y = Integer.parseInt(args[1]);

    	//final int x = 80;
    	//final int y = 60;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TiraLabra tiraLabra = new TiraLabra(x, y);
                tiraLabra.setVisible(true);
            }
        });
    }

	public int getWantedWindowWidth() {
		return wantedWindowWidth;
	}

	public void setWantedWindowWidth(int wantedWindowWidth) {
		this.wantedWindowWidth = wantedWindowWidth;
	}

	public int getWantedWindowHeight() {
		return wantedWindowHeight;
	}

	public void setWantedWindowHeight(int wantedWindowHeight) {
		this.wantedWindowHeight = wantedWindowHeight;
	}

	public GridRenderer getGridRenderer() {
		return gridRenderer;
	}

	public void setGridRenderer(GridRenderer gridRenderer) {
		this.gridRenderer = gridRenderer;
	}

	public boolean isDiagonalMoveAllowed() {
		return diagonalMoveAllowed;
	}

	public void setDiagonalMoveAllowed(boolean diagonalMoveAllowed) {
		this.diagonalMoveAllowed = diagonalMoveAllowed;
	}

	public boolean isUseTieBreaker() {
		return useTieBreaker;
	}

	public void setUseTieBreaker(boolean useTieBreaker) {
		this.useTieBreaker = useTieBreaker;
	}

	public boolean isUseAStar() {
		return useAStar;
	}

	public void setUseAStar(boolean useAStar) {
		this.useAStar = useAStar;
	}

	public boolean isPathFindingRan() {
		return pathFindingRan;
	}

	public void setPathFindingRan(boolean pathFindingRan) {
		this.pathFindingRan = pathFindingRan;
	}

	public int getStartCellX() {
		return startCellX;
	}

	public void setStartCellX(int startCellX) {
		this.startCellX = startCellX;
	}

	public int getStartCellY() {
		return startCellY;
	}

	public void setStartCellY(int startCellY) {
		this.startCellY = startCellY;
	}

	public int getGoalCellX() {
		return goalCellX;
	}

	public void setGoalCellX(int goalCellX) {
		this.goalCellX = goalCellX;
	}

	public int getGoalCellY() {
		return goalCellY;
	}

	public void setGoalCellY(int goalCellY) {
		this.goalCellY = goalCellY;
	}
    
}
