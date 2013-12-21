package fi.sakusaisa.tiralabra;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author Saku Säisä
 */
@SuppressWarnings("serial")
public class UiPanel extends JPanel implements ActionListener {
    
    TiraLabra tiraLabra;

    /**
     * wrapper method for TiraLabra's findPath()
     */
    public void findPath() {
        this.tiraLabra.findPath();
    }
    
    /**
     * wrapper method for TiraLabra's repaintGrid()
     */
    public void repaintGrid() {
        this.tiraLabra.gridRenderer.repaint();
    }

    /**
     * wrapper method for TiraLabra's resetPath()
     */    
    public void resetPath() {
        this.tiraLabra.resetPath();
    }

    /**
     * wrapper method for TiraLabra's resetGrid()
     */
    public void resetGrid() {
        this.tiraLabra.resetGrid();
    }
    
    public UiPanel(TiraLabra tiraLabra) {
    
        this.tiraLabra = tiraLabra;
        
        setMaximumSize(new java.awt.Dimension(50, tiraLabra.wantedWindowHeight));
        setBackground(Color.black);

        // buttons have their own jpanel(s)
        JPanel buttonMainPanel = new JPanel(new GridLayout(4,1));
        buttonMainPanel.setBackground(Color.black);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1,1));
        buttonPanel.setBackground(Color.black);
        
        JButton findPathButton = new JButton("Find Path");
        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                findPath();
                repaintGrid();
            }
        }); 
        buttonPanel.add(findPathButton);

        buttonMainPanel.add(buttonPanel);
        
        buttonPanel = new JPanel(new GridLayout(1,2));
        buttonPanel.setBackground(Color.black);

        JButton resetPathButton = new JButton("Reset Path");
        resetPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                resetPath();
                repaintGrid();
            }
        }); 
        buttonPanel.add(resetPathButton);        

        JButton resetGridButton = new JButton("Reset Grid");
        resetGridButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                resetGrid();
                repaintGrid();
            }
        }); 
        buttonPanel.add(resetGridButton);        
        buttonMainPanel.add(buttonPanel);
        
        buttonPanel = new JPanel(new GridLayout(1,1));
        buttonPanel.setBackground(Color.black);
        JCheckBox checkboxDiagonal = new JCheckBox("Diagonal Movement", false);
        checkboxDiagonal.setName("Diagonal Movement");
        checkboxDiagonal.addActionListener(this);
        checkboxDiagonal.setForeground(Color.white);
        buttonPanel.add(checkboxDiagonal);
        buttonMainPanel.add(buttonPanel);   

        buttonPanel = new JPanel(new GridLayout(1,1));
        buttonPanel.setBackground(Color.black);
        JCheckBox checkboxTiebreaker = new JCheckBox("Heuristics Tie-breaker", false);
        checkboxTiebreaker.setName("Heuristics Tie-breaker");
        checkboxTiebreaker.addActionListener(this);
        checkboxTiebreaker.setForeground(Color.white);
        buttonPanel.add(checkboxTiebreaker);
        buttonMainPanel.add(buttonPanel);   
        
        this.add(buttonMainPanel);     
        
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {

        JCheckBox source = (JCheckBox) e.getSource();
        boolean state = source.isSelected();

        if (state) {
            if (source.getName().equals("Diagonal Movement"))
                tiraLabra.diagonalMoveAllowed = true;
            if (source.getName().equals("Heuristics Tie-breaker"))
                tiraLabra.useTieBreaker = true;
        }
        
        else {
            if (source.getName().equals("Diagonal Movement"))
                tiraLabra.diagonalMoveAllowed = false;
            if (source.getName().equals("Heuristics Tie-breaker"))
                tiraLabra.useTieBreaker = false;            
        }

        if (tiraLabra.pathFindingRan) {
            tiraLabra.resetPath();
            tiraLabra.findPath();
            repaintGrid();
        }
        
    }

}
