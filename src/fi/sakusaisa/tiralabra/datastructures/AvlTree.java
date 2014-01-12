package fi.sakusaisa.tiralabra.datastructures;

import fi.sakusaisa.tiralabra.core.GridCell;

public class AvlTree {

	private AvlTreeNode root;
	
	/**
	 * Constructor.
	 */
	public AvlTree() {
		
		root = null;
	}

	/**
	 * Do a "rotate right" on a subtree to maintain balance.
	 * 
	 * @param node1 The root node on the subtree to rotate.
	 * @return The new root of this subtree after rotating.
	 */
	public AvlTreeNode rightRotate(AvlTreeNode node1) {
		
		AvlTreeNode node2;
		
		node2 = node1.left;
		node2.parent = node1.parent;
		node1.parent = node2;
		node1.left = node2.right;
		node2.right = node1;
		
		if (node1.left != null)
			node1.left.parent = node1;
		
		node1.height = Math.max(node1.left.height, node1.right.height) + 1;
		node2.height = Math.max(node2.left.height, node2.right.height) + 1;
		
		return node2;
		
	}
	
	/**
	 * Do a "rotate left" on a subtree to maintain balance.
	 * 
	 * @param node1 The root node on the subtree to rotate.
	 * @return The new root of this subtree after rotating.
	 */	
	public AvlTreeNode leftRotate(AvlTreeNode node1) {
	
		AvlTreeNode node2;
		
		node2 = node1.right;
		node2.parent = node1.parent;
		node1.parent = node2;
		node1.right = node2.left;
		node2.left = node1;
		
		if (node1.right != null)
			node1.right.parent = node1;
		
		node1.height = Math.max(node1.left.height, node1.right.height) + 1;
		node2.height = Math.max(node2.left.height, node2.right.height) + 1;
		
		return node2;
		
	}
	
	/**
	 * Do a "rotate rightleft" on a subtree to maintain balance.
	 * 
	 * @param node1 The root of the subtree to rotate.
	 * @return The root node of the rotated subtree.
	 */
	public AvlTreeNode rightLeftRotate(AvlTreeNode node1) {
		
		AvlTreeNode node2 = node1.right;
		node1.right = rightRotate(node2);
		return leftRotate(node1);
		
	}

	/**
	 * Do a "rotate leftright" on a subtree to maintain balance.
	 * 
	 * @param node1 The root of the subtree to rotate.
	 * @return The root node of the rotated subtree.
	 */	
	public AvlTreeNode leftRightRotate(AvlTreeNode node1) {
	
		AvlTreeNode node2 = node1.left;
		node1.left = leftRotate(node2);
		return rightRotate(node1);
		
	}
	
	private AvlTreeNode insert(AvlTreeNode newNode) {
		
		if (this.root == null) {
			this.root = newNode;
			return newNode;
		}
		
		AvlTreeNode x = this.root, p = x;

		while (x != null) {
			
			p = x;
			
			if (newNode.keyValue < x.keyValue) x = x.left;
			else x = x.right;
			
		}
		
		newNode.parent = p;
		if (newNode.keyValue < p.keyValue) p.left = newNode;
		else p.right = newNode;
			
		return newNode;
		
	}
	
	/**
	 * Adds a new GridCell into the tree.
	 * 
	 * @param nodeToAdd A GridCell to add into the tree.
	 */
	public void avlInsert(GridCell nodeToAdd) {
		
		AvlTreeNode newNode = new AvlTreeNode(nodeToAdd);
		insert(newNode);
		
		AvlTreeNode p = newNode.parent;
		
		while (p != null) {
			
			if (p.left.height == (p.right.height + 2)) {
				
				AvlTreeNode vanhempi = p.parent;
				AvlTreeNode alipuu;
				
				if (p.left.left.height > p.left.right.height)
					alipuu = rightRotate(p);
				else
					alipuu = leftRightRotate(p);
				
				if (vanhempi == null)
					this.root = alipuu;
				else if (vanhempi.left == p)
					vanhempi.left = alipuu;
				else
					vanhempi.right = alipuu;
				
				if (vanhempi != null)
					vanhempi.height = Math.max(vanhempi.left.height, vanhempi.right.height) + 1;
				
				return;
			
			}
			
			if (p.right.height == (p.left.height + 2)) {

				AvlTreeNode vanhempi = p.parent;
				AvlTreeNode alipuu;

				if (p.right.right.height > p.right.left.height)
					alipuu = leftRotate(p);
				else
					alipuu = rightLeftRotate(p);
				
				if (vanhempi == null)
					this.root = alipuu;
				else if (vanhempi.left == p)
					vanhempi.left = alipuu;
				else
					vanhempi.right = alipuu;
				
				if (vanhempi != null)
					vanhempi.height = Math.max(vanhempi.left.height, vanhempi.right.height) + 1;
				
				return;
				
			}
			
			p.height = Math.max(p.left.height, p.right.height) + 1;
			p = p.parent;
			
		}
		
	}
	
}
