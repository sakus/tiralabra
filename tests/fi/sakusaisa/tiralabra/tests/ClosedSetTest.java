package fi.sakusaisa.tiralabra.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.sakusaisa.tiralabra.core.GridCell;
import fi.sakusaisa.tiralabra.datastructures.ClosedSet;

public class ClosedSetTest {


	@Test
	public void testClosedSet() {
		ClosedSet testSet = new ClosedSet(50, 50);
		assertEquals("cell should be set as false (=initialized and empty)", false, testSet.getDataMatrix()[15][29]);
	}
	
	@Test
	public void testClear() {
		
		ClosedSet testSet = new ClosedSet(50, 50);
		
		testSet.getDataMatrix()[15][15] = true;
		testSet.getDataMatrix()[10][43] = true;
		testSet.getDataMatrix()[32][39] = true;
		testSet.getDataMatrix()[27][21] = true;
		testSet.getDataMatrix()[43][7] = true;
		
		assertEquals("cell should be set as true", true, testSet.getDataMatrix()[15][15]);
		assertEquals("cell should be set as true", true, testSet.getDataMatrix()[10][43]);
		assertEquals("cell should be set as true", true, testSet.getDataMatrix()[32][39]);
		assertEquals("cell should be set as true", true, testSet.getDataMatrix()[27][21]);
		assertEquals("cell should be set as true", true, testSet.getDataMatrix()[43][7]);
		
		testSet.clear();

		assertEquals("cell should be set as false", false, testSet.getDataMatrix()[15][15]);
		assertEquals("cell should be set as false", false, testSet.getDataMatrix()[10][43]);
		assertEquals("cell should be set as false", false, testSet.getDataMatrix()[32][39]);
		assertEquals("cell should be set as false", false, testSet.getDataMatrix()[27][21]);
		assertEquals("cell should be set as false", false, testSet.getDataMatrix()[43][7]);
		
	}
	
	@Test
	public void testInsert() {
		
		ClosedSet testSet = new ClosedSet(50, 50);
		GridCell testCell = new GridCell(25, 32);
		
		assertEquals("cell should be set as false", false, testSet.getDataMatrix()[25][32]);
		
		testSet.insert(testCell);
		
		assertEquals("cell should be set as true", true, testSet.getDataMatrix()[25][32]);
		
	}
	
	@Test
	public void testContains() {
	
		ClosedSet testSet = new ClosedSet(50, 50);
		
		GridCell testCell1 = new GridCell(25, 32);
		GridCell testCell2 = new GridCell(45, 15);
		
		testSet.insert(testCell1);
		
		assertEquals("cell should be set as true", true, testSet.getDataMatrix()[25][32]);
		assertEquals("should be true", true, testSet.contains(testCell1));
		
		assertEquals("cell should be set as false", false, testSet.getDataMatrix()[45][15]);
		assertEquals("should be false", false, testSet.contains(testCell2));
		
	}

	
	
}
