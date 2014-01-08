package fi.sakusaisa.tiralabra.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import fi.sakusaisa.tiralabra.core.TiraLabra;

/**
 * A Swing mouse listener for the visual grid.
 * 
 * @author Saku Säisä
 */
public class GridMouseListener extends MouseAdapter {
    
    GridRenderer gridRenderer;
    TiraLabra tiraLabra;
    int mouseCellX, mouseCellY;
    protected int lastMouseX, lastMouseY;
    protected boolean userMovingStart = false;
    protected boolean userMovingGoal = false;

    
    public GridMouseListener(GridRenderer gridRenderer, TiraLabra tiraLabra) {
        this.gridRenderer = gridRenderer;
        this.tiraLabra = tiraLabra;
    }
    
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        mouseCellX = (((mouseEvent.getX()-2) / this.gridRenderer.getCellSize() - 1));
        mouseCellY = (((mouseEvent.getY()-2) / this.gridRenderer.getCellSize() - 1));
        if (mouseCellOk()) mouseClickOn(mouseCellX, mouseCellY, false);

    }
    
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {   
    }
    
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        
            mouseCellX = (((mouseEvent.getX()-2) / this.gridRenderer.getCellSize() - 1));
            mouseCellY = (((mouseEvent.getY()-2) / this.gridRenderer.getCellSize() - 1));
            if (mouseCellOk()) mouseClickOn(mouseCellX, mouseCellY, true);
        
    }
    
    public boolean mouseCellOk() {
        return (mouseCellX >= 0 && mouseCellY >= 0 && mouseCellX <= gridRenderer.getCellsX() - 1 && mouseCellY <= gridRenderer.getCellsY() - 1);
    }
 
    /**
     * Manipulates the grid based on what was clicked on
     * @param x of the cell that was clicked on
     * @param y of the cell that was clicked on
     * @param drag is this a dragging event or not
     */
    public void mouseClickOn(int x, int y, boolean drag) {
        
        // if not dragging, set these as -1 so the mouse movement detection won't kick in
        if (!drag) {
            lastMouseX = -1;
            lastMouseY = -1;
                        
            // pick up the start point
            if (this.tiraLabra.getGridCells()[x][y].getCellData() == 4 && !userMovingGoal) {
            	userMovingStart = true;
            	this.tiraLabra.getGridCells()[x][y].setCellData(1);
            	this.tiraLabra.resetPath();
            }
            
            // pick up the goal point
            else if (this.tiraLabra.getGridCells()[x][y].getCellData() == 5 && !userMovingStart) {
            	userMovingGoal = true;
            	this.tiraLabra.getGridCells()[x][y].setCellData(1);
            	this.tiraLabra.resetPath();
            }            
            
            // put down the start point
            else if (userMovingStart && this.tiraLabra.getGridCells()[x][y].getCellData() == 1) {
            	userMovingStart = false;
            	this.tiraLabra.getGridCells()[x][y].setCellData(4);
            	this.tiraLabra.setStartCellX(x);
            	this.tiraLabra.setStartCellY(y);
            }

            // put down the start point
            else if (userMovingGoal && this.tiraLabra.getGridCells()[x][y].getCellData() == 1) {
            	userMovingGoal = false;
            	this.tiraLabra.getGridCells()[x][y].setCellData(5);
            	this.tiraLabra.setGoalCellX(x);
            	this.tiraLabra.setGoalCellY(y);
            }

            // just switch obstacle <-> clear
            else {

                if (this.tiraLabra.getGridCells()[x][y].getCellData() == 1) {
                    this.tiraLabra.getGridCells()[x][y].setCellData(0);
                }
                else if (this.tiraLabra.getGridCells()[x][y].getCellData() == 0) {
                    this.tiraLabra.getGridCells()[x][y].setCellData(1);
                }            	
            	
            }
            
        }
        
        // detect moving
        else if (x != lastMouseX || y != lastMouseY) {

            if (this.tiraLabra.getGridCells()[x][y].getCellData() == 1) {
                this.tiraLabra.getGridCells()[x][y].setCellData(0);
            }
            else if (this.tiraLabra.getGridCells()[x][y].getCellData() == 0) {
                this.tiraLabra.getGridCells()[x][y].setCellData(1);
            }

        }
        
        lastMouseX = x;
        lastMouseY = y;
        
        this.gridRenderer.repaint();
        
    }
    
}
