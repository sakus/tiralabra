package fi.sakusaisa.tiralabra.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.sakusaisa.tiralabra.core.GridCell;
import fi.sakusaisa.tiralabra.datastructures.AvlTree;

public class AvlTreeTest {

	@Test
	public void test() {
		
		AvlTree testTree = new AvlTree();
		
		GridCell testCell1 = new GridCell(10, 10);
		testCell1.setMovementCost(3);
		
		GridCell testCell2 = new GridCell(15, 15);
		testCell2.setMovementCost(1);
		
		GridCell testCell3 = new GridCell(20, 20);
		testCell3.setMovementCost(2);

		GridCell testCell4 = new GridCell(25, 25);
		testCell4.setMovementCost(0.5f);

		GridCell testCell5 = new GridCell(25, 25);
		testCell5.setMovementCost(2.5f);

		GridCell testCell6 = new GridCell(25, 25);
		testCell6.setMovementCost(3.5f);

		GridCell testCell7 = new GridCell(25, 25);
		testCell7.setMovementCost(1.5f);

		GridCell testCell8 = new GridCell(25, 25);
		testCell8.setMovementCost(0.1f);

		assertEquals(true, testTree.isEmpty());
		
		testTree.insert(testCell1);
		assertEquals(false, testTree.isEmpty());
		assertTrue(testTree.contains(testCell1) != null);
		assertEquals(3, testTree.getMin().getMovementCost(), 0.0001);
		
		testTree.insert(testCell2);
		assertEquals(1, testTree.getMin().getMovementCost(), 0.0001);
		
		testTree.insert(testCell3);
		assertEquals(1, testTree.getMin().getMovementCost(), 0.0001);

		assertTrue(testTree.contains(testCell2) != null);
		assertEquals(null, testTree.contains(testCell4));
		
		testTree.insert(testCell4);
		assertEquals(0.5f, testTree.getMin().getMovementCost(), 0.0001);
		
		testTree.insert(testCell5);
		testTree.insert(testCell6);
		testTree.insert(testCell7);
		
		testTree.insert(testCell8);
		assertEquals(0.1f, testTree.getMin().getMovementCost(), 0.0001);
	
		assertEquals(0.1f, testTree.delMin().getMovementCost(), 0.0001);
		assertEquals(0.5f, testTree.getMin().getMovementCost(), 0.0001);
		
		assertTrue(testTree.contains(testCell6) != null);
		testTree.remove(testCell6);
		assertTrue(testTree.contains(testCell6) == null);
			
	}

}
