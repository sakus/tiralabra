package fi.sakusaisa.tiralabra;

import static org.junit.Assert.*;

import org.junit.Test;

public class MinBinaryHeapTest {

	@Test
	public void testMinBinaryHeap() {
		
		MinBinaryHeap testHeap = new MinBinaryHeap(100);
		assertEquals("testHeap size should be 0", 0, testHeap.getHeapSize());
	}

	@Test
	public void testGetHeapSize() {
		
		MinBinaryHeap testHeap = new MinBinaryHeap(100);
		
		GridCell gridCell = new GridCell(0, 0);
		gridCell.setMovementCost(2);
		testHeap.insert(gridCell);
		assertEquals("size should be 1", 1, testHeap.getHeapSize());

		gridCell = new GridCell(0, 1);
		gridCell.setMovementCost(2);
		testHeap.insert(gridCell);
		assertEquals("size should be 2", 2, testHeap.getHeapSize());

	}

	@Test
	public void testClear() {

		MinBinaryHeap testHeap = new MinBinaryHeap(100);
		
		GridCell gridCell = new GridCell(0, 0);
		gridCell.setMovementCost(2);
		testHeap.insert(gridCell);

		gridCell = new GridCell(0, 1);
		gridCell.setMovementCost(2);
		testHeap.insert(gridCell);

		assertEquals("size should be 2", 2, testHeap.getHeapSize());
		testHeap.clear();
		assertEquals("size should be back to 0", 0, testHeap.getHeapSize());
	}

	@Test
	public void testInsert() {
		
		MinBinaryHeap testHeap = new MinBinaryHeap(100);
		
		GridCell gridCell = new GridCell(0, 0);
		gridCell.setMovementCost(2);
		testHeap.insert(gridCell);
		assertEquals("should return 2", 2f, testHeap.findMin().getMovementCost(), 0.0001f);
		
		gridCell = new GridCell(0, 1);
		gridCell.setMovementCost(3);
		testHeap.insert(gridCell);
		assertEquals("should still be 2", 2f, testHeap.findMin().getMovementCost(), 0.0001f);
		
		gridCell = new GridCell(0, 2);
		gridCell.setMovementCost(1);
		testHeap.insert(gridCell);
		assertEquals("now the min should be 1", 1f, testHeap.findMin().getMovementCost(), 0.0001f);

		gridCell = new GridCell(0, 3);
		gridCell.setMovementCost(9);
		testHeap.insert(gridCell);
		assertEquals("still 1", 1f, testHeap.findMin().getMovementCost(), 0.0001f);

		gridCell = new GridCell(0, 4);
		gridCell.setMovementCost(0.5f);
		testHeap.insert(gridCell);
		assertEquals("now it should be 0.5", 0.5f, testHeap.findMin().getMovementCost(), 0.0001f);
		assertEquals("and the size should be 5", 5, testHeap.getHeapSize());

	}

	@Test
	public void testDelMin() {

		MinBinaryHeap testHeap = new MinBinaryHeap(100);
		
		GridCell gridCell = new GridCell(0, 0);
		gridCell.setMovementCost(6);
		testHeap.insert(gridCell);

		gridCell = new GridCell(1, 0);
		gridCell.setMovementCost(2);
		testHeap.insert(gridCell);

		gridCell = new GridCell(2, 0);
		gridCell.setMovementCost(1.25f);
		testHeap.insert(gridCell);

		gridCell = new GridCell(3, 0);
		gridCell.setMovementCost(100);
		testHeap.insert(gridCell);

		assertEquals("first, heap size is 4", 4, testHeap.getHeapSize());
		
		GridCell returnedCell = testHeap.delMin();
		assertEquals("now it should be 3", 3, testHeap.getHeapSize());
		assertEquals("and the returned cell should have a vlaue of 1.25", 1.25f, returnedCell.getMovementCost(), 0.0001f);
		assertEquals("the top of the heap is now 2", 2f, testHeap.findMin().getMovementCost(), 0.0001f);
		
	}
	
}
