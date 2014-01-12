package fi.sakusaisa.tiralabra.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.sakusaisa.tiralabra.core.GridCell;

public class GridCellTest {

	@Test
	public void testGridCell() {
		
		GridCell testCell = new GridCell(10, 11);
		
		assertEquals(10, testCell.getCellX());
		assertEquals(11, testCell.getCellY());
		assertEquals(1, testCell.getCellData());
		assertTrue(testCell.getDistanceFromStart() == -1);
		assertTrue(testCell.getDistanceToGoal() == -1);
		assertTrue(testCell.getMovementCost() == -1);
		assertEquals(null, testCell.getArrivedFrom());
		
		
	}

	@Test
	public void testSetAndGetArrivedFrom() {
		
		GridCell testCell1 = new GridCell(10, 11);
		GridCell testCell2 = new GridCell(33, 28);
		
		assertEquals(null, testCell2.getArrivedFrom());
		
		testCell2.setArrivedFrom(testCell1);
		assertEquals(testCell1, testCell2.getArrivedFrom());
		
	}

}
