package fi.sakusaisa.tiralabra;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * 
 * @author Saku S??is??
 * this class handles the rendering of the grid and everything related to it
 */
class GridRenderer extends JPanel {

    /**
     * a reference to the used TiraLabra instance
     */
    private TiraLabra tiraLabra;

    // these hold needed information about the grid / its cells
    private int cellsX, cellsY, cellSize, lastMouseX, lastMouseY;
    
    /**
     * Constructor.
     * @param tiraLabra a reference to the used TiraLabra instance
     */
    public GridRenderer(TiraLabra tiraLabra) {
        
        this.tiraLabra = tiraLabra;
        this.cellsX = tiraLabra.getGridCells().length;
        this.cellsY = tiraLabra.getGridCells()[0].length;
        this.cellSize = tiraLabra.getCellSize();
        
        GridMouseListener gridMouseListener = new GridMouseListener(this);
        
        this.addMouseListener(gridMouseListener);
        this.addMouseMotionListener(gridMouseListener);
        
        setMaximumSize(new java.awt.Dimension(tiraLabra.wantedWindowWidth - 50, tiraLabra.wantedWindowHeight));
        setMinimumSize(new java.awt.Dimension(tiraLabra.wantedWindowWidth - 50, tiraLabra.wantedWindowHeight));
        setPreferredSize(new java.awt.Dimension(tiraLabra.wantedWindowWidth - 50, tiraLabra.wantedWindowHeight));
        setBackground(Color.black);

        
    }
    
    private void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        
        // draw the grid itself
        g2d.setColor(Color.darkGray);
        for (int x = 10; x <= (cellsX+1) * cellSize; x += cellSize)
            g2d.drawLine(x, 10, x, (cellsY+1) * cellSize);
        for (int y = 10; y <= (cellsY+1) * cellSize; y += cellSize)
            g2d.drawLine(10, y, (cellsX+1) * cellSize, y);
        
        // visualize the cells in the grid
        for (int i = 0; i < cellsX; i++) {
            for (int j = 0; j < cellsY; j++) {
                
                // set the color based on cell data
                if (tiraLabra.getGridCells()[i][j].getCellData() == 0) // obstacle
                    g2d.setColor(Color.lightGray);
                else if (tiraLabra.getGridCells()[i][j].getCellData() == 1) // clear
                    g2d.setColor(Color.black);
                else if (tiraLabra.getGridCells()[i][j].getCellData() == 2) // path
                    g2d.setColor(Color.blue);
                else if (tiraLabra.getGridCells()[i][j].getCellData() == 3) // considered path
                    g2d.setColor(new Color(0.4f, 0.4f, 0.6f, 1f));
                else if (tiraLabra.getGridCells()[i][j].getCellData() == 4) // starting cell
                    g2d.setColor(Color.red);
                else if (tiraLabra.getGridCells()[i][j].getCellData() == 5) // goal cell
                    g2d.setColor(Color.pink);
                
                // draw the cells
                g2d.fillRect(10 + i * cellSize + 1, 10 + j * cellSize + 1, cellSize-1, cellSize-1);
                
            }            
        }

        
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        render(g);
    }
    
    public int getCellSize() {
        return this.cellSize;
    }
    
    public int getCellsX() {
        return this.cellsX;
    }
    
    public int getCellsY() {
        return this.cellsY;
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
        }
        
        if (x != lastMouseX || y != lastMouseY) {

            if (this.tiraLabra.getGridCells()[x][y].getCellData() == 1) {
                this.tiraLabra.getGridCells()[x][y].setCellData(0);
            }
            else if (this.tiraLabra.getGridCells()[x][y].getCellData() == 0) {
                this.tiraLabra.getGridCells()[x][y].setCellData(1);
            }

        }
        
        lastMouseX = x;
        lastMouseY = y;
        
        repaint();
        
    }
    
}
