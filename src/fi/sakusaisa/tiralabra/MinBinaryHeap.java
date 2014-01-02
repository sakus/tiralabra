package fi.sakusaisa.tiralabra;

/**
 * 
 * A min binary heap implementation that holds GridCell instances only.
 * @author Saku Säisä
 *
 */
public class MinBinaryHeap {

	// TODO: this class has not yet been used or tested in any way and surely doesn't work 100% as is
	
	private GridCell[] heapArray;
	private int heapSize;
	
	public MinBinaryHeap() {
		heapArray = new GridCell[100];
		heapSize = 0;
	}

	/**
	 * Returns an object's parent in the heap.
	 * @param index The index of the object whose parent you want.
	 * @return The parent object.
	 */
	private int getParent(int index) {
		return (index+1)/2;
	}
	
	/**
	 * Returns the right child of the object.
	 * @param index The index of the object whose child you want.
	 * @return The index of the child.
	 */
	private int getRightChild(int index) {
		return 2 * (index+1);
	}
	
	/**
	 * Returns the left child of the object.
	 * @param index The index of the object whose child you want.
	 * @return The index of the child.
	 */
	private int getLeftChild(int index) {
		return 2 * (index+1) + 1;
	}
	
	/**
	 * Makes sure the heap is in working order.
	 * @param index The index in the array to start running heapify from.
	 */
	private void heapify(int index) {
	
		int smallest;
		int left = getLeftChild(index);
		int right = getRightChild(index);
		GridCell tempCell;
	
		// check if the right child exists
		if (right <= heapSize) {
			
			// compare the values of the children for the smaller value
			if (heapArray[left].getMovementCost() < heapArray[right].getMovementCost())
				smallest = left;
			else
				smallest = right;
			
			/* 
			 * if the value in the current index is larger than the smallest child,
			 * switch them and then call heapify again
			 */
			if (heapArray[index].getMovementCost() > heapArray[smallest].getMovementCost()) {
				tempCell = heapArray[index];
				heapArray[index] = heapArray[smallest];
				heapArray[smallest] = tempCell;
				heapify(smallest);
			}
				
		}
		
		/* if there is no right child and the value of the left child is smaller, 
		 * switch the values (no need to heapify in this case)
		 */
		else if (left == heapSize && heapArray[index].getMovementCost() > heapArray[left].getMovementCost()) {
			tempCell = heapArray[index];
			heapArray[index] = heapArray[left];
			heapArray[left] = tempCell;
		}
		
	}
	
	/**
	 * Clears the heap of all data making it empty.
	 */
	public void clear() {
		heapArray = new GridCell[100];
		heapSize = 0;
	}
	
	/**
	 * Inserts a new GridCell instance into the heap.
	 * @param gridCell the GridCell to be added
	 */
	public void insert(GridCell gridCell) {
	
		// TODO have to increase the size of the array when needed
		
		// make the heap one slot bigger
		heapSize++;
		
		// traverse the heap "upwards" until the correct position is found
		int i = heapSize;
		while (i > 0 && heapArray[getParent(i)].getMovementCost() > gridCell.getMovementCost()) {
			heapArray[i] = heapArray[getParent(i)];
			i = getParent(i);
		}
		heapArray[i] = gridCell;
		
	}
	
	/**
	 * Removes the GridCell from the top of the heap and returns it.
	 * @return the deleted GridCell
	 */
	public GridCell delMin() {
		
		// grab the cell from the top of the heap
		GridCell min = heapArray[0];
		
		// move the last cell into the top, mark the heap as 1 cell smaller and heapify
		heapArray[0] = heapArray[heapSize];
		heapSize--;
		heapify(0);
		
		// finally return the old top of the heap
		return min;
	}
	
	/**
	 * Finds the current minimum without removing it from the heap.
	 * @return The GridCell currently on top of the heap.
	 */
	public GridCell findMin() {
		return heapArray[0];
	}
	
}
