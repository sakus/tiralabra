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
    
    /**
     * The closed set of nodes for A*
     * uses ArrayList at first, will be replaced with something non-premade
     */
    private ArrayList<GridCell> closedSet = new ArrayList<GridCell>();
    
    /**
     * The open set of nodes for A*
     * PQ at first, will implement a binary heap of my own to use for this
     */
    private PriorityQueue<GridCell> openSet;

    // random variables..
    protected GridRenderer gridRenderer;
    protected int wantedWindowWidth, wantedWindowHeight;
    protected int startCellX, startCellY, goalCellX, goalCellY;    
    private int cellSize = 10;
    protected boolean pathFindingRan = false;
    private boolean autoRun;  
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
        
        // reset the stats
        pathFindingRan = false;
        statusMessage1 = null;
        statusMessage2 = null;
        statusMessage3 = null;
        nodesChecked = 0;        

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
        
        // reset the stats
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
        
        // helper variables so we don't have to do long method calls all the time
        int currentX;
        int currentY;

        // clear the open and closed set
        closedSet.clear();
        openSet.clear();
        
        // grab the starting cell as the current one
        GridCell currentCell = gridCells[startCellX][startCellY];
                
        // set the distances for the current cell
        currentCell.setDistanceFromStart(0);
        currentCell.setDistanceToGoal(Math.abs(startCellX - goalCellX) + Math.abs(startCellY - goalCellY));
        
        // add the starting cell into the open set
        openSet.add(currentCell);
 
        // loop until we arrive at the target or the open set becomes empty
        while (currentCell != gridCells[goalCellX][goalCellY] && !openSet.isEmpty()) {
            
            // take the cell with the smallest movementCost and move it over to closedSet
            currentCell = (GridCell)openSet.remove();
            closedSet.add(currentCell);
            
            // if we're at the goal, just go ahead and break the loop now
            if (currentCell == gridCells[goalCellX][goalCellY])
                break;
                        
            // update the helper variables
            currentX = currentCell.getCellX();
            currentY = currentCell.getCellY();
            
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
                
        // visualize the path following hte arrivedFrom flags starting from the goal 
        if (currentCell == gridCells[goalCellX][goalCellY]) {
        
            int pathLength = 1;
            
            while (currentCell.getArrivedFrom() != gridCells[startCellX][startCellY]) {
                currentCell.getArrivedFrom().setCellData(2);
                currentCell = currentCell.getArrivedFrom();
                pathLength++;
            }
            
            statusMessage2 = "Path length: " + pathLength;
        }
                
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

        // first check if the cell even exists - if not, don't do anything else
        if (!cellIsOkay(processX, processY))
            return;
        
        // grab a reference to the considered cell
        GridCell processCell = gridCells[processX][processY];

        if (processCell.getCellData() != 0 && !closedSet.contains(processCell)) {

            // if the cell isn't in the open set..
            if (!openSet.contains(processCell)) {
                
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
                	processCell.setDistanceToGoal(aStarHeuristic(processCell));
                else
                	processCell.setDistanceToGoal(0);
                
                // the combined value of path lenght + approx. length to goal used for evaluating the next step
                processCell.setMovementCost(processCell.getDistanceFromStart() + processCell.getDistanceToGoal());

                // add the cell to openSet; as it is a PriorityQeue / binaby min heap, the best option will be on top
                openSet.add(processCell);
                
            }

            // if the cell is in the open set already..
            else {
                
                // check if this way to the cell is shorter than the previously found one
                if ((currentCell.getDistanceFromStart() + 1) < processCell.getDistanceFromStart()) {
                
                	// if so, update the information - need to remove from openSet first, then re-add.. SLOW!
                	openSet.remove(processCell);
                	processCell.setArrivedFrom(currentCell);
                	processCell.setDistanceFromStart(currentCell.getDistanceFromStart() + 1);
                
                	// heuristics if A*
                	if (useAStar)
                		processCell.setDistanceToGoal(aStarHeuristic(processCell));
                	else
                		processCell.setDistanceToGoal(0);
                
                	processCell.setMovementCost(processCell.getDistanceFromStart() + processCell.getDistanceToGoal());
                	openSet.add(processCell);

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
    		return (1 * Math.max(dx, dy)) * tieBreaker;
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
        
        // openSet as PriorityQueue with a custom comparator
        openSet = new PriorityQueue<GridCell>(100, new Comparator<GridCell>() {
            @Override
            public int compare(GridCell gc1, GridCell gc2) {
                float cost1 = gc1.getMovementCost();
                float cost2 = gc2.getMovementCost();
                if (cost1 < cost2) return -1;
                else if (cost1 > cost2) return 1;
                else return 0;
                }
        });
        
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
