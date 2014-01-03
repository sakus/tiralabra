package fi.sakusaisa.tiralabra;

/**
 * 
 * A min binary heap implementation that holds GridCell instances only.
 * @author Saku Säisä
 *
 */
public class MinBinaryHeap {
	
	private GridCell[] heapArray;
	private int heapSize;
	
	/**
	 * Constructor.
	 * 
	 * @param initialSize The size of the array holding the heap. Will be increased as needed. Minimum size is 2.
	 */
	public MinBinaryHeap(int initialSize) {
		if (initialSize < 2) heapArray = new GridCell[2];
		else heapArray = new GridCell[initialSize];
		heapSize = 0;
	}

	/**
	 * Returns an object's parent in the heap.
	 * @param index The index of the object whose parent you want.
	 * @return The parent object.
	 */
	protected int getParent(int index) {
		return index / 2;
	}
	
	/**
	 * Returns the right child of the object.
	 * @param index The index of the object whose child you want.
	 * @return The index of the child.
	 */
	protected int getRightChild(int index) {
		return 2 * index;
	}
	
	/**
	 * Returns the left child of the object.
	 * @param index The index of the object whose child you want.
	 * @return The index of the child.
	 */
	protected int getLeftChild(int index) {
		return 2 * index + 1;
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
		heapSize = 0;
		for (int i = 0; i < heapArray.length; i++)
			heapArray[i] = null;
	}
	
	/**
	 * Inserts a new GridCell instance into the heap.
	 * @param gridCell the GridCell to be added
	 */
	public void insert(GridCell gridCell) {
	
		// double the size of the array if it's full
		if (this.heapSize >= heapArray.length-2) {
			
			GridCell[] newHeapArray = new GridCell[heapArray.length * 2];
			
			for (int i = 0; i < heapArray.length; i++) {
				newHeapArray[i] = heapArray[i];
			}

			heapArray = newHeapArray;
			newHeapArray = null;
			
		}
		
		// make the heap one slot bigger
		heapSize++;
		
		// traverse the heap "upwards" until the correct position is found
		int i = heapSize;		
		while (i > 1 && heapArray[getParent(i)].getMovementCost() > gridCell.getMovementCost()) {
				heapArray[i] = heapArray[getParent(i)];
				i = getParent(i);
		}
		
		// insert into the heap
		heapArray[i] = gridCell;
		
	}
	
	/**
	 * Removes the GridCell from the top of the heap and returns it.
	 * @return the deleted GridCell
	 */
	public GridCell delMin() {
		
		if (heapSize > 0) {
			
			// grab the cell from the top of the heap
			GridCell min = heapArray[1];
			
			// move the last cell into the top, mark the heap as 1 cell smaller and heapify
			heapArray[1] = heapArray[heapSize];
			heapSize--;
			heapify(1);
			
			// finally return the old top of the heap
			return min;
			
		}
		
		else return null;
		
	}
	
	/**
	 * Finds the current minimum without removing it from the heap.
	 * @return The GridCell currently on top of the heap.
	 */
	public GridCell findMin() {
		return heapArray[1];
	}
	
	/**
	 * Get the size of the heap.
	 * @return The heap size in int.
	 */
	public int getHeapSize() {
		return this.heapSize;
	}
	
	/**
	 * Is the heap empty or not.
	 * 
	 * @return true is the heap is empty, false otherwise
	 */
	public boolean isEmpty() {
		return this.heapSize == 0;
	}
	
	/**
	 * Checks whether the heap contains a certain instance of gridcell
	 * 
	 * @param gridCell the instance we're looking for
	 * @return the index the cell was found at, -1 if not found
	 */
	public int contains(GridCell gridCell) {
	
		int found = -1;
		
		for (int i = 1; i <= heapSize; i++) {
			if (heapArray[i] == gridCell) found = i;
		}
		
		return found;
		
	}
	
	/**
	 * Removes a given node from anywhere in the heap. This is done by replacing
	 * the node to be deleted with the rightmost leaf, then by decresing the heap
	 * size by 1 and finally calling heapify starting from the deleted index.
	 * 
	 * @param index The index of the node to remove.
	 */
	public void removeAtIndex(int index) {
		heapArray[index] = heapArray[heapSize];
		heapSize--;
		heapify(index);
	}
	
}
