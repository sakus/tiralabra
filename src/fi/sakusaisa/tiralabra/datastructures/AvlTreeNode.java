package fi.sakusaisa.tiralabra.datastructures;

import fi.sakusaisa.tiralabra.core.GridCell;

/**
 * A class to represent a node in an AVL tree.
 * 
 * @author Saku Säisä
 *
 */
public class AvlTreeNode {

	protected AvlTreeNode left, right, parent;
	protected int height;
	protected GridCell key;
	protected float keyValue;
	
	/**
	 * Constructor.
	 * 
	 * @param nodeKey A GridCell as the key of this node.
	 */
	public AvlTreeNode(GridCell nodeKey) {
		
		this.left = null;
		this.right = null;
		this.parent = null;
		
		this.height = 0;
		this.key = nodeKey;
		this.keyValue = nodeKey.getMovementCost();
		
	}

	/**
	 * Gets the actual key value from the GridCell that is referenced in the node.
	 * 
	 * @return The key value.
	 */
	public float getKeyValue() {
		this.keyValue = this.key.getMovementCost();
		return this.keyValue;
	}
	
}
