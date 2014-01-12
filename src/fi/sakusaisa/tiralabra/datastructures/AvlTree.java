package fi.sakusaisa.tiralabra.datastructures;

import fi.sakusaisa.tiralabra.core.GridCell;

/**
 * An AVL tree implementation.
 * 
 * @author Saku Säisä
 *
 */
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

		int vasKork1, oikKork1, vasKork2, oikKork2;
		
		if (node1.left == null) vasKork1 = -1;
		else vasKork1 = node1.left.height;
		if (node1.right == null) oikKork1 = -1;
		else oikKork1 = node1.right.height;

		if (node2.left == null) vasKork2 = -1;
		else vasKork2 = node2.left.height;
		if (node2.right == null) oikKork2 = -1;
		else oikKork2 = node2.right.height;
		
		node1.height = Math.max(vasKork1, oikKork1) + 1;
		node2.height = Math.max(vasKork2, oikKork2) + 1;
		
		return node2;
		
	}

	/**
	 * Clears the tree.
	 * 
	 * @param compatibility true or false, this is only for compatibility..
	 */
	public void clear(boolean compatibility) {
		this.root = null;
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
		
		int vasKork1, oikKork1, vasKork2, oikKork2;
		
		if (node1.left == null) vasKork1 = -1;
		else vasKork1 = node1.left.height;
		if (node1.right == null) oikKork1 = -1;
		else oikKork1 = node1.right.height;

		if (node2.left == null) vasKork2 = -1;
		else vasKork2 = node2.left.height;
		if (node2.right == null) oikKork2 = -1;
		else oikKork2 = node2.right.height;
		
		node1.height = Math.max(vasKork1, oikKork1) + 1;
		node2.height = Math.max(vasKork2, oikKork2) + 1;
		
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
	
	private AvlTreeNode internalInsert(AvlTreeNode newNode) {
		
		if (this.root == null) {
			this.root = newNode;
			return newNode;
		}
		
		AvlTreeNode x = this.root, p = null;

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
	public void insert(GridCell nodeToAdd) {
		
		AvlTreeNode newNode = new AvlTreeNode(nodeToAdd);
		internalInsert(newNode);
		
		AvlTreeNode p = newNode.parent;
		
		int vanhKorkVas = -1, vanhKorkOik = -1;
		
		while (p != null) {
			
			int vasKork, oikKork;
			int vasVasKork = -1, vasOikKork = -1, oikOikKork = -1, oikVasKork = -1;
			
			if (p.left == null)
				vasKork = -1;
			else {
				vasKork = p.left.height;
				if (p.left.left == null)
					vasVasKork = -1;
				else
					vasVasKork = p.left.left.height;
				
				if (p.left.right == null)
					vasOikKork = -1;
				else
					vasOikKork = p.left.right.height;				
			}
			
			if (p.right == null)
				oikKork = -1;
			else {
				oikKork = p.right.height;
				if (p.right.left == null)
					oikVasKork = -1;
				else
					oikVasKork = p.right.left.height;
				
				if (p.right.right == null)
					oikOikKork = -1;
				else
					oikOikKork = p.right.right.height;								
			}
						
			if (vasKork == (oikKork + 2)) {
				
				AvlTreeNode vanhempi = p.parent;
				AvlTreeNode alipuu;
				
				if (vanhempi != null) {
					if (vanhempi.left != null)
						vanhKorkVas = vanhempi.left.height;
					if (vanhempi.right != null)
						vanhKorkOik = vanhempi.right.height;
				}

				if (vasVasKork > vasOikKork)
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
					vanhempi.height = Math.max(vanhKorkVas, vanhKorkOik) + 1;
				
				return;
			
			}
			
			if (oikKork == (vasKork + 2)) {

				AvlTreeNode vanhempi = p.parent;
				AvlTreeNode alipuu;

				if (vanhempi != null) {
					if (vanhempi.left != null)
						vanhKorkVas = vanhempi.left.height;
					if (vanhempi.right != null)
						vanhKorkOik = vanhempi.right.height;
				}
				
				if (oikOikKork > oikVasKork)
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
					vanhempi.height = Math.max(vanhKorkVas, vanhKorkOik) + 1;
				
				return;
				
			}
			
			p.height = Math.max(vasKork, oikKork) + 1;
			p = p.parent;
			
		}
		
	}
	
	private AvlTreeNode findMin(AvlTreeNode node) {	
	
		if (node == null)
			return null;
		
		while (node.left != null)
			node = node.left;
		
		return node;
		
	}
	
	/**
	 * Finds the minimum vlaue in the tree without deleting it.
	 * 
	 * @return The GridCell found.
	 */
	public GridCell getMin() {
		
		AvlTreeNode min = findMin(this.root);

		if (min != null)
			return min.key;
		else
			return null;
		
	}
	
	/**
	 * Removes the minimum value key form the tree.
	 * 
	 * @return The deleted GridCell.
	 */
	public GridCell delMin() {
		
		AvlTreeNode nodeToDelete = findMin(this.root);
		
		if (nodeToDelete.parent == null)
			this.root = null;
		else
			nodeToDelete.parent.left = null;
		
		return nodeToDelete.key;
		
	}

	/**
	 * Searches the tree for a specific GriDcell starting from the root.
	 * 
	 * @param cellToFind The GridCell of which existance in the tree to check.
	 * @return Returns the AvlTreeNode instance of the GridCell if found, null if not foun.
	 */
	public AvlTreeNode contains(GridCell cellToFind) {
	
		// starting from the root, keep going until the node to be checked is null
		AvlTreeNode node = this.root;
		while (node != null) {
			
			// move on to the left or the right child based on if the value is smaller or bigger
			if (cellToFind.getMovementCost() < node.keyValue)
				node = node.left;
			else if (cellToFind.getMovementCost() > node.keyValue)
				node = node.right;
			
			// if the value matches, take a better look if it's actually the object we're looking for
			else {
				
				if (node.key == cellToFind)
					return node;
				else
					return null;
				
			}
			
		}
		
		return null;
		
	}

	/**
	 * Checks whether or not the tree is empty.
	 * 
	 * @return True if the tree is empty, false if it's not.
	 */
	public boolean isEmpty() {
		return (this.root == null);
	}

	private AvlTreeNode internalRemove(AvlTreeNode pois) {
		
		AvlTreeNode vanh, lapsi, seur;
		
		if (pois.left == null && pois.right == null) {
			
			vanh = pois.parent;
			
			if (vanh == null) {
				this.root = null;
				return pois;
			}
			
			if (pois == vanh.left) {
				vanh.left = null;
			}
			
			else
				vanh.right = null;
		
			return pois;
			
		}
		
		else if (pois.left == null || pois.right == null) {
			
			if (pois.left != null)
				lapsi = pois.left;
			else 
				lapsi = pois.right;
			
			vanh = pois.parent;
			lapsi.parent = vanh;
			
			if (vanh == null) {
				this.root = lapsi;
				return pois;
			}
			
			if (pois == vanh.left)
				vanh.left = lapsi;
			else
				vanh.right = lapsi;
			
			return pois;
			
		}
		
		else if (pois.left != null && pois.right != null) {
			
			seur = findMin(pois);
			pois.key = seur.key;
			lapsi = seur.right;
			vanh = seur.parent;
			
			if (vanh.left == seur)
				vanh.left = lapsi;
			else
				vanh.right = lapsi;
			
			if (lapsi != null)
				lapsi.parent = vanh;
			
			return seur;
			
		}
		
		else return null;
		
	}
	
	/**
	 * Removes a specifid GridCell from the tree if it exists.
	 * 
	 * @param deleteNode the GridCell to delete.
	 */
	public void remove(GridCell deleteNode) {
		
		AvlTreeNode foundNode = contains(deleteNode);
		
		if (foundNode == null)
			return;
		
		AvlTreeNode pois = internalRemove(foundNode);
		AvlTreeNode p = pois.parent;
		
		AvlTreeNode vanhempi = p.parent;
		AvlTreeNode alipuu = null;

		int vasKork, oikKork;
		int vasVasKork = -1, vasOikKork = -1, oikOikKork = -1, oikVasKork = -1;
		
		if (p.left == null)
			vasKork = -1;
		else {
			vasKork = p.left.height;
			if (p.left.left == null)
				vasVasKork = -1;
			else
				vasVasKork = p.left.left.height;
			
			if (p.left.right == null)
				vasOikKork = -1;
			else
				vasOikKork = p.left.right.height;				
		}
		
		if (p.right == null)
			oikKork = -1;
		else {
			oikKork = p.right.height;
			if (p.right.left == null)
				oikVasKork = -1;
			else
				oikVasKork = p.right.left.height;
			
			if (p.right.right == null)
				oikOikKork = -1;
			else
				oikOikKork = p.right.right.height;								
		}

		while (p != null) {
			
			if (p.height <= -2 || p.height >= 2) {
				
				if (p.height <= -2) {
									
					if (vasVasKork > vasOikKork)
						alipuu = rightRotate(p);
					else
						alipuu = leftRightRotate(p);
					
				}
				
				else if (p.height >= 2) {
					
					if (oikOikKork > oikVasKork)
						alipuu = leftRotate(p);
					else
						alipuu = rightLeftRotate(p);
					
				}
				
				if (p == this.root) {
					this.root = alipuu;
					return;
				}
				
				if (alipuu.keyValue < vanhempi.keyValue)
					alipuu = vanhempi.left;
				else
					alipuu = vanhempi.right;
				
				p = vanhempi;
			
			}
			
			else {
				p.height = Math.max(vasKork, oikKork) + 1;
				p = p.parent;
			}
			
		}
		
	}
	
}
