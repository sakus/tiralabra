package fi.sakusaisa.tiralabra;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A small and simple standalone performance benchmarker comparing premade and custom made data structures.
 * 
 * @author Saku Säisä
 */
public class Benchmark {

	private GridCell[][] gridCells;
	
	private ClosedSet customClosedSet;
    private ArrayList<GridCell> prebuiltClosedSet;

    private MinBinaryHeap customOpenSet;
    private PriorityQueue<GridCell> prebuiltOpenSet;

    /**
     * Comparator.
     * @param x The grid size in x axis
     * @param y The gird size in y axis
     */
	public Benchmark(int x, int y) {

		gridCells = new GridCell[x][y];
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				gridCells[i][j] = new GridCell(i, j);
			}
		}
			
		customClosedSet = new ClosedSet(x, y);
		prebuiltClosedSet = new ArrayList<GridCell>();
		
		customOpenSet = new MinBinaryHeap(100);
		prebuiltOpenSet = new PriorityQueue<GridCell>(100, new Comparator<GridCell>() {
 			@Override
 			public int compare(GridCell gc1, GridCell gc2) {
 				float cost1 = gc1.getMovementCost();
 				float cost2 = gc2.getMovementCost();
 				if (cost1 < cost2) return -1;
 				else if (cost1 > cost2) return 1;
 				else return 0;
 			}
		});

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		long startTime, endTime, timeTaken, totalTime;
		int xSize, ySize, runTests;
		float averageTime;
		boolean verbose;
		
		xSize = 1000;
		ySize = 1000;
		
		runTests = 500;
		verbose = false;
		
		Benchmark benchmarker = new Benchmark(xSize, ySize);

		// closed set, adding into prebuilt arraylist
		System.out.println("*** \"closed set\" performance adding cells into a prebuilt structure ***");
		totalTime = 0;
		averageTime = 0;
		for (int k = 0; k < runTests; k++) {
			if (verbose) System.out.print(" [iteration " + (k+1) +"/" + runTests + "] Adding " + xSize + "*" + ySize + " GridCells into a prebuilt arraylist ..");
			startTime = System.currentTimeMillis();
			benchmarker.prebuiltClosedSet.clear();
			for (int i = 0; i < xSize; i++) {
				for (int j = 0; j < ySize; j++) {
					benchmarker.prebuiltClosedSet.add(benchmarker.gridCells[i][j]);		
				}
			}
			endTime = System.currentTimeMillis();
			timeTaken = endTime - startTime;
			totalTime += timeTaken;
			if (verbose) System.out.println(".. and it took " + timeTaken + "ms");
		}
		averageTime = totalTime / runTests;
		System.out.println("Average time taken over " + runTests + " iterations was " + averageTime + "ms.\n");
		
		// closet set, adding into custom boolean matrix
		System.out.println("*** \"closed set\" performance adding cells into a custom structure ***");
		totalTime = 0;
		averageTime = 0;
		for (int k = 0; k < runTests; k++) {
			if (verbose) System.out.print(" [iteration " + (k+1) +"/" + runTests + "] Adding " + xSize + "*" + ySize + " GridCells into a custom boolean matrix ..");
			startTime = System.currentTimeMillis();
			benchmarker.customClosedSet.clear();
			for (int i = 0; i < xSize; i++) {
				for (int j = 0; j < ySize; j++) {
					benchmarker.customClosedSet.insert(benchmarker.gridCells[i][j]);
				}
			}
			endTime = System.currentTimeMillis();
			timeTaken = endTime - startTime;
			totalTime += timeTaken;
			if (verbose) System.out.println(".. and it took " + timeTaken + "ms");
		}
		averageTime = totalTime / runTests;
		System.out.println("Average time taken over " + runTests + " iterations was " + averageTime + "ms.\n");

		// open set, adding into prebuilt arraylist
		System.out.println("*** \"open set\" performance adding cells into a prebuilt structure ***");
		totalTime = 0;
		averageTime = 0;
		for (int k = 0; k < runTests; k++) {
			if (verbose) System.out.print(" [iteration " + (k+1) +"/" + runTests + "] Adding " + xSize + "*" + ySize + " GridCells into a prebuilt priorityqueue ..");
			startTime = System.currentTimeMillis();
			benchmarker.prebuiltOpenSet.clear();
			for (int i = 0; i < xSize; i++) {
				for (int j = 0; j < ySize; j++) {
					benchmarker.prebuiltOpenSet.add(benchmarker.gridCells[i][j]);		
				}
			}
			endTime = System.currentTimeMillis();
			timeTaken = endTime - startTime;
			totalTime += timeTaken;
			if (verbose) System.out.println(".. and it took " + timeTaken + "ms");
		}
		averageTime = totalTime / runTests;
		System.out.println("Average time taken over " + runTests + " iterations was " + averageTime + "ms.\n");
		
		// open set, adding into custom binary heap
		System.out.println("*** \"open set\" performance adding cells into a custom binary heap ***");
		totalTime = 0;
		averageTime = 0;
		for (int k = 0; k < runTests; k++) {
			if (verbose) System.out.print(" [iteration " + (k+1) +"/" + runTests + "] Adding " + xSize + "*" + ySize + " GridCells into a custom binary heap ..");
			startTime = System.currentTimeMillis();
			benchmarker.customOpenSet.clear();
			for (int i = 0; i < xSize; i++) {
				for (int j = 0; j < ySize; j++) {
					benchmarker.customOpenSet.insert(benchmarker.gridCells[i][j]);		
				}
			}
			endTime = System.currentTimeMillis();
			timeTaken = endTime - startTime;
			totalTime += timeTaken;
			if (verbose) System.out.println(".. and it took " + timeTaken + "ms");
		}
		averageTime = totalTime / runTests;
		System.out.println("Average time taken over " + runTests + " iterations was " + averageTime + "ms.\n");
		
	}

}
