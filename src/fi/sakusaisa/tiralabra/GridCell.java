package fi.sakusaisa.tiralabra;

/**
 * Class representing a single cell / node in the grid
 * 
 * @author Saku Säisä
 */
public class GridCell {


     // the X location of this cell on the grid
    private int cellX;
    
    // the Y location of this cell on the grid
    private int cellY;
    
    /*
     * Data about what the cell actually contains.
     * 
     * 0 = un-passable obstacle
     * 1 = passable
     * 2 = final path
     * 3 = was considered but not used for pathing
     * 4 = start
     * 5 = goal
     * 
     */
    private int cellData;
    
    /*
     * The distance from start to this cell.
     * -1 = not set
     */
    private float distanceFromStart;
    
   /*
    * The distance to goal from this cell.
    * -1 = not set
    */
    private float distanceToGoal;
    
    /*
     * This is the (stupidly named) value that is used to determine the path
     * (distanceFromStart + distanceToGoal)
     */
    private float movementCost;
            

     // The cell this cell was discovered from
    private GridCell arrivedFrom;
        
    /**
     * The constructor initializes the cell with data 1 (passable/clear),
     * and distances -1.
     * 
     * @param x the x on the grid for this cell
     * @param y the y on the grid for this cell
     */
    public GridCell(int x, int y) {
        
        this.cellX = x;
        this.cellY = y;
        
        this.cellData = 1;
        this.distanceFromStart = -1;
        this.distanceToGoal = -1;
        this.movementCost = -1;
        this.arrivedFrom = null;
        
    }
    
    public int getCellData() {
        return this.cellData;
    }
    
    public void setCellData(int newData) {
        this.cellData = newData;
    }
    
    public float getDistanceFromStart() {
        return this.distanceFromStart;
    }
    
    public void setDistanceFromStart(float dist) {
        this.distanceFromStart = dist;
    }
    
    public float getDistanceToGoal() {
        return this.distanceToGoal;
    }
    
    public void setDistanceToGoal(float dist) {
        this.distanceToGoal = dist;
    }
 
    public void setArrivedFrom(GridCell arrivedFrom) {
        this.arrivedFrom = arrivedFrom;
    }
    
    public GridCell getArrivedFrom() {
        return this.arrivedFrom;
    }
        
    public void setMovementCost(float movementCost) {
        this.movementCost = movementCost;
    }
    
    public float getMovementCost() {
        return this.movementCost;
    }
    
    public void setCellX(int x) {
        this.cellX = x;
    }
    
    public int getCellX() {
        return this.cellX;
    }
    
    public void setCellY(int y) {
        this.cellY = y;
    }
    
    public int getCellY() {
        return this.cellY;
    }
}
