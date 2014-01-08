package fi.sakusaisa.tiralabra.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import fi.sakusaisa.tiralabra.core.TiraLabra;

/**
 * Builds the UI JPanel.
 * 
 * @author Saku Säisä
 */
@SuppressWarnings("serial")
public class UiPanel extends JPanel implements ActionListener {
    
    TiraLabra tiraLabra;
    
    public UiPanel(final TiraLabra tiraLabra) {
    
        this.tiraLabra = tiraLabra;
        
        setMaximumSize(new java.awt.Dimension(50, tiraLabra.getWantedWindowHeight()));

        // buttons have their own jpanel(s)
        JPanel buttonMainPanel = new JPanel(new GridLayout(5,1));
        JPanel buttonPanel = new JPanel(new GridLayout(1,1));
        
        // ui button for "find path"
        JButton findPathButton = new JButton("Find Path");
        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                tiraLabra.findPath();
                tiraLabra.getGridRenderer().repaint();
            }
        }); 
        buttonPanel.add(findPathButton);
        buttonMainPanel.add(buttonPanel);
        
        // ui button for "reset path"
        buttonPanel = new JPanel(new GridLayout(1,2));
        JButton resetPathButton = new JButton("Reset Path");
        resetPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	tiraLabra.resetPath();
            	tiraLabra.getGridRenderer().repaint();
            }
        }); 
        buttonPanel.add(resetPathButton);
        
        // ui button for "reset grid"
        JButton resetGridButton = new JButton("Reset Grid");
        resetGridButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                tiraLabra.resetGrid();
                tiraLabra.getGridRenderer().repaint();
            }
        }); 
        buttonPanel.add(resetGridButton);        
        buttonMainPanel.add(buttonPanel);
        
        // selection box for diagonal movement on/off
        buttonPanel = new JPanel(new GridLayout(1,1));
        JCheckBox checkboxDiagonal = new JCheckBox("Diagonal Movement", false);
        checkboxDiagonal.setName("Diagonal Movement");
        checkboxDiagonal.addActionListener(this);
        buttonPanel.add(checkboxDiagonal);
        buttonMainPanel.add(buttonPanel);   

        // selection box for a* on/off
        buttonPanel = new JPanel(new GridLayout(1,1));
        JCheckBox checkboxAStar = new JCheckBox("Use A* (otherwise Dijkstra)", false);
        checkboxAStar.setName("Use A*");
        checkboxAStar.addActionListener(this);
        buttonPanel.add(checkboxAStar);
        buttonMainPanel.add(buttonPanel);   
        
        // selection box for a* tiebreaker on/off
        buttonPanel = new JPanel(new GridLayout(1,1));
        JCheckBox checkboxTiebreaker = new JCheckBox("Heuristics Tie-breaker (A*)", false);
        checkboxTiebreaker.setName("Heuristics Tie-breaker");
        checkboxTiebreaker.addActionListener(this);
        buttonPanel.add(checkboxTiebreaker);
        buttonMainPanel.add(buttonPanel);   

        this.add(buttonMainPanel, java.awt.BorderLayout.NORTH);     
        
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {

        JCheckBox source = (JCheckBox) e.getSource();
        boolean state = source.isSelected();

        if (state) {
            if (source.getName().equals("Diagonal Movement"))
                tiraLabra.setDiagonalMoveAllowed(true);
            if (source.getName().equals("Heuristics Tie-breaker"))
                tiraLabra.setUseTieBreaker(true);
            if (source.getName().equals("Use A*"))
                tiraLabra.setUseAStar(true);
        }
        
        else {
            if (source.getName().equals("Diagonal Movement"))
                tiraLabra.setDiagonalMoveAllowed(false);
            if (source.getName().equals("Heuristics Tie-breaker"))
                tiraLabra.setUseTieBreaker(false);
            if (source.getName().equals("Use A*"))
                tiraLabra.setUseAStar(false);            
        }

        if (tiraLabra.isPathFindingRan()) {
            tiraLabra.resetPath();
            tiraLabra.findPath();
            tiraLabra.getGridRenderer().repaint();
        }
        
    }

}
