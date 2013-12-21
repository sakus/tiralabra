package fi.sakusaisa.tiralabra;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A Swing mouse listener for the visual grid.
 * Calls a handler function with information about which cell was clicked on.
 * @author Saku S??is??
 */
public class GridMouseListener extends MouseAdapter {
    
    GridRenderer gridRenderer;
    int mouseCellX, mouseCellY;
    
    public GridMouseListener(GridRenderer gridRenderer) {
        this.gridRenderer = gridRenderer;
    }
    
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        mouseCellX = (((mouseEvent.getX()-2) / this.gridRenderer.getCellSize() - 1));
        mouseCellY = (((mouseEvent.getY()-2) / this.gridRenderer.getCellSize() - 1));
        if (mouseCellOk()) gridRenderer.mouseClickOn(mouseCellX, mouseCellY, false);

    }
    
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {   
    }
    
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        
            mouseCellX = (((mouseEvent.getX()-2) / this.gridRenderer.getCellSize() - 1));
            mouseCellY = (((mouseEvent.getY()-2) / this.gridRenderer.getCellSize() - 1));
            if (mouseCellOk()) gridRenderer.mouseClickOn(mouseCellX, mouseCellY, true);
        
    }
    
    public boolean mouseCellOk() {
        return (mouseCellX >= 0 && mouseCellY >= 0 && mouseCellX <= gridRenderer.getCellsX() - 1 && mouseCellY <= gridRenderer.getCellsY() - 1);
    }
    
}
