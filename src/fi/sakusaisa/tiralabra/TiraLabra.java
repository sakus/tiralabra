package fi.sakusaisa.tiralabra;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The main TiraLabra class.
 * 
 * @author Saku Säisä
 */
@SuppressWarnings("serial")
public class TiraLabra extends JFrame {


     // The data for all cells in the grid is held in this 2d-array.
    private GridCell[][] gridCells = new GridCell[90][60];
        
    // closed set as a simple boolean matrix
    private boolean[][] customClosedSet = new boolean[gridCells.length][gridCells[0].length];

    // open set as a custom binary heap implementation
    private MinBinaryHeap customOpenSet;

    // random variables..
    protected GridRenderer gridRenderer;
    protected int wantedWindowWidth, wantedWindowHeight;
    protected int startCellX, startCellY, goalCellX, goalCellY;    
    private int cellSize = 10;
    protected boolean pathFindingRan = false;  
    protected boolean diagonalMoveAllowed = false;
    protected boolean useTieBreaker = false;
    protected String statusMessage1, statusMessage2, statusMessage3;
    protected boolean useAStar = false;
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
        startCellX = 1; startCellY = 1; goalCellX = gridCells.length - 2; goalCellY = gridCells[0].length - 2;
        gridCells[startCellX][startCellY].setCellData(4);
        gridCells[goalCellX][goalCellY].setCellData(5);
        
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
        pathFindingRan = false;
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
        if (pathFindingRan)
            resetPath();
        
        pathFindingRan = true;

        // clear the sets
        for (int i = 0; i < customClosedSet.length; i++) {
        	for (int j = 0; j < customClosedSet[0].length; j++) {
        		customClosedSet[i][j] = false;
        	}
        }
        customOpenSet.clear();
        
        // grab the starting cell first
        GridCell currentCell = gridCells[startCellX][startCellY];
        currentCell.setDistanceFromStart(0);
        customOpenSet.insert(currentCell);
 
        // loop until we arrive at the target or the open set becomes empty
        while (currentCell != gridCells[goalCellX][goalCellY] && !customOpenSet.isEmpty()) {
            // take the cell with the smallest movementCost and move it over to closedSet
        	currentCell = customOpenSet.delMin();
        	
            //closedSet.add(currentCell);
            customClosedSet[currentCell.getCellX()][currentCell.getCellY()] = true;
            
            // check adjacent cells
            findPathProcessAdjacentCells(currentCell);
            
        }
                
        // visualize the path following hte arrivedFrom flags starting from the goal 
        if (currentCell == gridCells[goalCellX][goalCellY]) {
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
        
        while (currentCell.getArrivedFrom() != gridCells[startCellX][startCellY]) {
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
                if (diagonalMoveAllowed) {
                    if (x != currentX || y != currentY) {
                        findPathProcessCell(x, y, currentCell);
                    }
                }
                
                /* if diagonal move is disabled, then only check the x and y where either
                 * X or Y (but not both) is the same as the processed cell.. this checks
                 * 4 of the adjacent cells: directly above, below, left and right
                 */
                else if (!diagonalMoveAllowed) {
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

        if (processCell.getCellData() != 0 && customClosedSet[processCell.getCellX()][processCell.getCellY()] == false) {
        	
            // if the cell isn't in the open set..
        	foundAtIndex = customOpenSet.contains(processCell);
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
                if (useAStar)
                	processCell.setDistanceToGoal(aStarHeuristic(processCell, currentCell));
                else
                	processCell.setDistanceToGoal(0);
                
                // the combined value of path lenght + approx. length to goal used for evaluating the next step
                processCell.setMovementCost(processCell.getDistanceFromStart() + processCell.getDistanceToGoal());

                // add the cell to openSet; as it is a binaby min heap, the best option will be on top
                customOpenSet.insert(processCell);
                
            }

            // if the cell is in the open set already..
            else {
                
                // check if this way to the cell is shorter than the previously found one
                if ((currentCell.getDistanceFromStart() + 1) < processCell.getDistanceFromStart()) {
                
                	// if so, update the information - need to remove from openSet first, then re-add.. SLOW!
                	customOpenSet.removeAtIndex(foundAtIndex);
                	processCell.setArrivedFrom(currentCell);
                	processCell.setDistanceFromStart(currentCell.getDistanceFromStart() + 1);
                
                	// heuristics if A*
                	if (useAStar)
                		processCell.setDistanceToGoal(aStarHeuristic(processCell, currentCell));
                	else
                		processCell.setDistanceToGoal(0);
                
                	processCell.setMovementCost(processCell.getDistanceFromStart() + processCell.getDistanceToGoal());
                	customOpenSet.insert(processCell);

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
    public float aStarHeuristic(GridCell processCell, GridCell currentCell) {    	

    	int dx = Math.abs(processCell.getCellX() - goalCellX);
    	int dy = Math.abs(processCell.getCellY() - goalCellY);
    	
    	float tieBreaker;
    	if (useTieBreaker) {
    		tieBreaker = 1.001f;
    	}
    	else {
    		tieBreaker = 1f;
    	}
    		
    	// manhattan distance when diagonal move disabled
    	if (!diagonalMoveAllowed)
    		return (1 * (dx + dy)) * tieBreaker;
    	
    	// Chebyshev distance when diagonal move enabled
    	else {
    		int cost = 1;
    		
    		/*
    		 * if it's a diagonal cell, make movement to it cost twice that of a non-diagonal cell,
    		 * so that diagonal move is allowed but the cost of going one step NE will be the same as going
    		 * first N and then E to end up in the same cell
    		 */
    		if (processCell.getCellX() != currentCell.getCellX() && processCell.getCellY() != currentCell.getCellY())
    			cost = 2;
    		return (cost * Math.max(dx, dy)) * tieBreaker;
    	}
    		    
    }
    
    public GridCell[][] getGridCells() {
        return this.gridCells;
    }
    
    public int getCellSize() {
        return this.cellSize;
    }
        
    public TiraLabra() {
        
        // define dimensions for the program window taking into account some padding + window decorations
        wantedWindowWidth = gridCells.length * cellSize + 70 + getInsets().left + getInsets().right;
        wantedWindowHeight = gridCells[0].length * cellSize + 20 + getInsets().top + getInsets().bottom;
        
        // program window properties
        setTitle("TiRaLabra");
        setSize(wantedWindowWidth, wantedWindowHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        // initialize data
        resetGrid();
                
        customOpenSet = new MinBinaryHeap();
        
        // set the JPanel for the grid renderer
        gridRenderer = new GridRenderer(this);
        getContentPane().add(gridRenderer, java.awt.BorderLayout.WEST);
        
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
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TiraLabra tiraLabra = new TiraLabra();
                tiraLabra.setVisible(true);
            }
        });
    }
    
}
