package Assignment1;

/*
 Author: Giorgio Miguel Torregrosa
 Title: Integer Two-Four-Tree Data Structure w/o duplicates
 Course: COP 3503 w/ Professor Gerber
 Date: 10/23/2024
*/

// begin 2-3-4 Tree structure
public class TwoFourTree {
	
	// 2-3-4 Node structure
	private class TwoFourTreeItem {
		
		// public variables
		int values = 1;
		int value1 = 0; 
		int value2 = 0; 
		int value3 = 0; 
		boolean isLeaf = true;
		TwoFourTreeItem parent = null; 
		TwoFourTreeItem leftChild = null; 
		TwoFourTreeItem rightChild = null;
		TwoFourTreeItem centerChild = null; 
		TwoFourTreeItem centerLeftChild = null; 
		TwoFourTreeItem centerRightChild = null; 

		// simple methods 
		public boolean isTwoNode() {

			if (this.values == 1) {
				return true;
			} // end if
			return false;
		}

		public boolean isThreeNode() {
			if (this.values == 2) {
				return true;
			} // end if
			return false;
		}

		public boolean isFourNode() {
			if (this.values == 3) {
				return true;
			} // end if
			return false;
		}

		public boolean isRoot() {
			if (parent == null) {
				return true;
			} // end if
			return false;
		}

		// constructors
		public TwoFourTreeItem(int value1) {
			this.value1 = value1;
		}

		public TwoFourTreeItem(int value1, int value2) {
			this.value1 = value1;
			this.value2 = value2;
			this.values = 2;
		}

		public TwoFourTreeItem(int value1, int value2, int value3) {
			this.value1 = value1;
			this.value2 = value2;
			this.value3 = value3;
			this.values = 3;
		}

		private void printIndents(int indent) {
			for (int i = 0; i < indent; i++)
				System.out.printf("  ");
		}

		public void printInOrder(int indent) {
			if (!isLeaf)
				leftChild.printInOrder(indent + 1);
			printIndents(indent);
			System.out.printf("%d\n", value1);
			if (isThreeNode()) {
				if (!isLeaf)
					centerChild.printInOrder(indent + 1);
				printIndents(indent);
				System.out.printf("%d\n", value2);
			} else if (isFourNode()) {
				if (!isLeaf)
					centerLeftChild.printInOrder(indent + 1);
				printIndents(indent);
				System.out.printf("%d\n", value2);
				if (!isLeaf)
					centerRightChild.printInOrder(indent + 1);
				printIndents(indent);
				System.out.printf("%d\n", value3);
			}
			if (!isLeaf)
				rightChild.printInOrder(indent + 1);
		}
	}// end item class

	// root reference
	TwoFourTreeItem root = null;

	// left and right references
	TwoFourTreeItem leftNode = null;
	TwoFourTreeItem rightNode = null;

	// delete reference
	TwoFourTreeItem delNode = null;

	// 7 helper methods for addValue(): splitRoot(), splitLeafRoot(), SplitNode(),
	// SplitLeafNode(), Evolve2Node(), Evolve3Node(), EvolveLeafNode()

	// takes in a 4node-leaf as an input parameter, splits the leaf-node into two
	// 2node-leaves and returns the middle value of the split 4node
	public int splitLeafNode(TwoFourTreeItem node) {

		// creating leftNode with v1
		leftNode = new TwoFourTreeItem(node.value1);
		leftNode.parent = node.parent;

		// creating RightNode with v3
		rightNode = new TwoFourTreeItem(node.value3);
		rightNode.parent = node.parent;

		// return middle value (v2) to be used by evolution methods
		return node.value2;
	}// end split leaf Node

	// takes in a 4node as an input parameter, splits the 4node into two 2nodes and
	// returns the middle value of the split 4node
	public int splitNode(TwoFourTreeItem node) {

		// creating leftNode and initializing pointers
		leftNode = new TwoFourTreeItem(node.value1);
		leftNode.isLeaf = false;
		leftNode.parent = node.parent;
		leftNode.leftChild = node.leftChild;
		leftNode.rightChild = node.centerLeftChild;

		// updating the children's parent
		node.leftChild.parent = leftNode;
		node.centerLeftChild.parent = leftNode;

		// creating rightNode and initializing pointers
		rightNode = new TwoFourTreeItem(node.value3);
		rightNode.isLeaf = false;
		rightNode.parent = node.parent;
		rightNode.leftChild = node.centerRightChild;
		rightNode.rightChild = node.rightChild;

		// updating the children's parent
		node.centerRightChild.parent = rightNode;
		node.rightChild.parent = rightNode;

		// return middle value to be used by evolution methods
		return node.value2;
	}// end split Node

	// takes in a 4node-root as an input parameter, splits the root into two 2nodes
	// and returns the middle value of the split root
	public TwoFourTreeItem splitRoot(TwoFourTreeItem oldRoot, int value) {

		// creating a new root with v2
		TwoFourTreeItem newRoot = new TwoFourTreeItem(oldRoot.value2);
		newRoot.isLeaf = false;

		// creating a leftNode for the split
		leftNode = new TwoFourTreeItem(oldRoot.value1);
		leftNode.parent = newRoot;
		leftNode.isLeaf = false;
		// creating a rightNode for the split
		rightNode = new TwoFourTreeItem(oldRoot.value3);
		rightNode.parent = newRoot;
		rightNode.isLeaf = false;

		// updating new root's children
		newRoot.leftChild = leftNode;
		newRoot.rightChild = rightNode;

		// updating leftNodes children and updating their children's parent to leftNode
		leftNode.leftChild = root.leftChild;
		leftNode.rightChild = root.centerLeftChild;
		root.leftChild.parent = leftNode;
		root.centerLeftChild.parent = leftNode;

		// updating rightNodes children and updating their children's parent to
		// rightNode
		rightNode.leftChild = root.centerRightChild;
		rightNode.rightChild = root.rightChild;
		root.centerRightChild.parent = rightNode;
		root.rightChild.parent = rightNode;
		root = newRoot;

		// returning the correct node to traverse to
		if (value > newRoot.value1) {
			return rightNode;
		} // traverse right
		else {
			return leftNode;
		} // traverse left
	}// end split non leaf root

	// used only by a leaf-root.
	public TwoFourTreeItem splitLeafRoot(TwoFourTreeItem oldRoot, int value) {

		// creating a new root, this root is not a leaf node
		TwoFourTreeItem newRoot = new TwoFourTreeItem(oldRoot.value2);
		newRoot.isLeaf = false;

		// creating LeftNode and RightNode reference
		leftNode = new TwoFourTreeItem(oldRoot.value1);
		rightNode = new TwoFourTreeItem(oldRoot.value3);

		// setting up pointers/references
		newRoot.leftChild = leftNode;
		newRoot.rightChild = rightNode;
		leftNode.parent = newRoot;
		rightNode.parent = newRoot;

		// after links have been made we move the reference to the new root to allow GC
		// to clean up old root
		root = newRoot;

		// figure out where cur reference should go next
		if (value < newRoot.value1) {
			return leftNode;
		} // traverse left after split
		else {
			return rightNode;
		} // traverse right after split
	}// end split leaf root

	// evolves a 2node into a 3node, returns the node that cur should traverse to next.
	public TwoFourTreeItem evolve2Node(TwoFourTreeItem node, int v2, int insertValue) {

		// Node now has two values
		node.values = 2;

		if (v2 > node.value1) {
			node.value2 = v2;
			// Child pointers
			// node.leftChild remains the same (values < value1)
			node.centerChild = leftNode; // From splitting child
			node.rightChild = rightNode; // From splitting child
		} else {
			node.value2 = node.value1;
			node.value1 = v2;
			node.leftChild = leftNode; // From splitting child
			node.centerChild = rightNode; // From splitting child
			// node.rightChild remains the same (values > value2)
		}

		// Update parent pointers
		leftNode.parent = node;
		rightNode.parent = node;

		// Decide where to traverse next
		if (insertValue < v2) {
			return leftNode;
		}  
		else {
			return rightNode;
		}
	}// end evovle2Node

	// evolves a 3node returns the node that cur should go to next in order to keep
	// traversing
	public TwoFourTreeItem evolve3Node(TwoFourTreeItem node, int v2, int insertValue) {

		// case 1: v2 > node.v2
		if (v2 > node.value2) {
			node.value3 = v2;
			node.values = 3;
			node.centerLeftChild = node.centerChild;
			node.centerRightChild = leftNode;
			node.rightChild = rightNode;
		} // end case 1

		// case 2: v2 < node.v2, but v2 > node.v1
		else if (v2 > node.value1 && v2 < node.value2) {
			node.value3 = node.value2;
			node.value2 = v2;
			node.values = 3;
			node.centerLeftChild = leftNode;
			node.centerRightChild = rightNode;
		} // end case 2

		// case 3: v2 < node.v1
		else if (v2 < node.value1) {
			node.value3 = node.value2;
			node.value2 = node.value1;
			node.value1 = v2;
			node.values = 3;
			node.leftChild = leftNode;
			node.centerLeftChild = rightNode;
			node.centerRightChild = node.centerChild;
		} // end case 3

		// updating newly created 2node's parent to the node that is currently evolving
		leftNode.parent = node;
		rightNode.parent = node;

		node.centerChild = null;

		// figure out where cur reference should go next
		if (insertValue > v2) {
			return rightNode;
		} // traverse right after split
		else {
			return leftNode;
		} // traverse left after split

	}// end evolve 3node

	// simple method to evolve a leaf node.
	public void evolveLeafNode(TwoFourTreeItem node, int item) {

		// 2node case
		if (node.isTwoNode()) {

			node.values = 2;

			// case 1 if item > v1
			if (item > node.value1) {
				node.value2 = item;
			} // end if
				// case 2 if item < v1
			else {
				node.value2 = node.value1;
				node.value1 = item;
			} // end else
		} // end 2node case

		// 3node case
		else if (node.isThreeNode()) {

			node.values = 3;

			// case 1
			if (item > node.value2) {
				node.value3 = item;
			} // end case 1
			// case 2
			else if (item > node.value1 && item < node.value2) {
				node.value3 = node.value2;
				node.value2 = item;
			} // end case 2
			// case 3
			else if (item < node.value1) {
				node.value3 = node.value2;
				node.value2 = node.value1;
				node.value1 = item;
			} // end case 3

		} // end 3node case
	}// end evolve leaf Node

	// CREATE INSERTION METHOD
	public boolean addValue(int value) {

		// null case, create a new node to begin the tree
		if (root == null) {
			root = new TwoFourTreeItem(value);
			return true;
		} // end null check

		// root is still a leaf and can take on more values
		else if (!root.isFourNode() && root.isLeaf) {
			evolveLeafNode(root, value);
			return true;
		} // end leaf case

		// root has some children, creating a reference to the head
		TwoFourTreeItem cur = root;

		// root is the first 4node to pop and its a leaf node
		if (root.isFourNode() && root.isLeaf) {
			cur = splitLeafRoot(root, value); // cur is either at left or right node after the split
		} // splitting a leaf node

		// while the reference isn't a leaf node, traverse the 2-3-4tree and "pop" any
		// 4nodes we encounter on the way down towards the proper leaf to insert
		while (!cur.isLeaf) {

			// 2node case
			if (cur.isTwoNode()) {
				if (value < cur.value1) {
					cur = cur.leftChild;
				} // traverse left
				else {
					cur = cur.rightChild;
				} // traverse left
			} // end 2node case

			// 3node case
			else if (cur.isThreeNode()) {
				if (value < cur.value1) {
					cur = cur.leftChild;
				} // traverse left
				else if (value < cur.value2) {
					cur = cur.centerChild;
				} // traverse mid
				else {
					cur = cur.rightChild;
				} // traverse right
			} // end if three node

			// 4node case
			else if (cur.isFourNode()) {

				// node to split is the root
				if (cur.isRoot()) {
					cur = splitRoot(root, value);
				} // end splitting non leaf root
					// if parent of node to split is a 2node
				else if (cur.parent.isTwoNode()) {
					int middleValue = splitNode(cur);
					cur = evolve2Node(cur.parent, middleValue, value);
				} // end 2node case
				else if (cur.parent.isThreeNode()) {
					int middleValue = splitNode(cur);
					cur = evolve3Node(cur.parent, middleValue, value);
				} // end 3node case

			} // end if 4node

		} // end while, cur should be at a leaf node

		// we are now at a leaf node
		if (!cur.isFourNode()) {
			evolveLeafNode(cur, value);
			return true;
		} // end if
		else {
			if (cur.parent.isTwoNode()) {
				int middleValue = splitLeafNode(cur);
				cur = evolve2Node(cur.parent, middleValue, value);
				evolveLeafNode(cur, value);
			} // end if
			else if (cur.parent.isThreeNode()) {
				int middleValue = splitLeafNode(cur);
				cur = evolve3Node(cur.parent, middleValue, value);
				evolveLeafNode(cur, value);
			} // end else if
		} // end else

		return false;

	}// end insert method

	// CREATE HAS VALUE METHOD
	public boolean hasValue(int value) {

		// creating a reference to move around
		TwoFourTreeItem cur = root;

		// while reference isn't null
		while (cur != null) {

			// 2node case
			if (cur.isTwoNode()) {
				if (value == cur.value1) {
					delNode = cur;
					return true;
				} // end if
				else {
					if (value < cur.value1) {
						cur = cur.leftChild;
					} // traverse left
					else {
						cur = cur.rightChild;
					} // traverse left
				} // end else
			} // end if two node

			// 3node case
			else if (cur.isThreeNode()) {
				if (value == cur.value1 || value == cur.value2) {
					delNode = cur;
					return true;
				} // end if
				else {
					if (value < cur.value1) {
						cur = cur.leftChild;
					} // traverse left
					else if (value < cur.value2) {
						cur = cur.centerChild;
					} // traverse mid
					else {
						cur = cur.rightChild;
					} // traverse right
				} // end else
			} // end if three node

			// 4node case
			else if (cur.isFourNode()) {
				if (value == cur.value1 || value == cur.value2 || value == cur.value3) {
					delNode = cur;
					return true;
				} // end if
				else {
					if (value < cur.value1) {
						cur = cur.leftChild;
					} // traverse left
					else if (value < cur.value2) {
						cur = cur.centerLeftChild;
					} // traverse leftMid
					else if (value < cur.value3) {
						cur = cur.centerRightChild;
					} // traverse rightMid
					else {
						cur = cur.rightChild;
					} // traverse right
				} // end else
			} // end else if
		} // end while

		delNode = null;
		return false;
	} // end has value

	// deletion helper methods----------------

	// This method is responsible for finding the predecessor or successor leafnode
	// of a given node
	public TwoFourTreeItem findSucPred(TwoFourTreeItem node, int value) {
		
		// set the reference at the node.
		TwoFourTreeItem cur = node;

		// 2node case: we can either go left or right then start traversal, right for
		// successor, left for predecessor
		if (cur.isTwoNode()) {

			// go right, then keep going left to find successor
			cur = node.rightChild;
			while (!cur.isLeaf) {
				cur = cur.leftChild;
			} // end traversal

			// we've found successor, only look for predecessor if successor is at a 2node
			if (!cur.isTwoNode()) {

				// we've found the successor and we can swap with the value we want to delete,
				// returning the reference to the node that contains the value to be swapped
				// with
				return cur;
			} // end find successor
			else {

				// we need to look at the predecessor and check if it is at a 2node or not
				cur = node.leftChild;

				// keep going right to find predecessor
				while (!cur.isLeaf) {
					cur = cur.rightChild;
				} // end traversal

				// predecessor is found, return regardless of node type
				return cur;

			} // end predecessor case
		} // end 2node case
		else if (cur.isThreeNode()) {

			// since this node has two keys, we need to know which key we are trying to find
			// the pred/succ of
			// two cases, we are either v1 or v2, if v1 go left or mid, if v2 go mid or
			// right

			// case v1
			if (value == cur.value1) {

				// go mid (right), check successor
				cur = node.centerChild;

				while (!cur.isLeaf) {
					cur = cur.leftChild;
				} // end traversal

				if (!cur.isTwoNode()) {
					return cur;
				} // only look for predecessor if successor is at a 2node
				else {

					// go left, check predecessor
					cur = node.leftChild;

					while (!cur.isLeaf) {
						cur = cur.rightChild;
					} // end traversal

					// return regardless of type
					return cur;
				} // end predecessor
			} // end case 1
			else if (value == cur.value2) {

				// go right
				cur = node.rightChild;

				while (!cur.isLeaf) {
					cur = cur.leftChild;
				} // end traversal
				if (!cur.isTwoNode()) {

					// cur isnt at a 2node
					return cur;
				}
				// else its at a 2node and we should check predecessor
				else {

					// go mid (left)
					cur = node.centerChild;

					while (!cur.isLeaf) {
						cur = cur.rightChild;
					} // end traversal

					// return regardless
					return cur;
				} // end predecessor

			} // end case 2
		} // end 3node case
		else if (cur.isFourNode()) {

			if (value == cur.value1) {

				// go centerLeft (right)
				cur = node.centerLeftChild;

				while (!cur.isLeaf) {
					cur = cur.leftChild;
				} // end traversal

				if (!cur.isTwoNode()) {
					return cur;
				} // check predecessor
				else {
					// go left
					cur = node.leftChild;

					while (!cur.isLeaf) {
						cur = cur.rightChild;
					} // end traversal

					// return cur regardless of type
					return cur;
				} // end predecessor case
			} // end case 1
				// case 2
			else if (value == cur.value2) {

				// go centerRight (right)
				cur = cur.centerRightChild;

				while (!cur.isLeaf) {
					cur = cur.leftChild;
				} // end traversal

				if (!cur.isTwoNode()) {
					return cur;
				} // else check predecessor
				else {
					// go centerLeft (left)
					cur = node.centerLeftChild;

					while (!cur.isLeaf) {
						cur = cur.rightChild;
					} // end traversal

					// return regardless of type
					return cur;
				} // end predecessor case
			} // end case 2
			else if (value == cur.value3) {

				// go right
				cur = node.rightChild;

				while (!cur.isLeaf) {
					cur = cur.leftChild;
				} // end traversal

				if (!cur.isTwoNode()) {
					return cur;
				} // end suc case
					// find pred
				else {
					// go centerRight (left)
					cur = node.centerRightChild;

					while (!cur.isLeaf) {
						cur = cur.rightChild;
					} // end traversal

					return cur;
				} // end pred case

			} // end case 3

		} // end 4node case
		
		return null;
	}// end find suc/pred

	// This method fuses 3 2nodes into a singular 4node root, and returns that new
	// root.
	TwoFourTreeItem mergeRoot(TwoFourTreeItem oldRoot) {

		TwoFourTreeItem newRoot = new TwoFourTreeItem(oldRoot.leftChild.value1, oldRoot.value1,
				oldRoot.rightChild.value1);

		if (!oldRoot.leftChild.isLeaf) {
			newRoot.isLeaf = false;
			oldRoot.leftChild.leftChild.parent = newRoot;
			oldRoot.leftChild.rightChild.parent = newRoot;
			oldRoot.rightChild.leftChild.parent = newRoot;
			oldRoot.rightChild.rightChild.parent = newRoot;
		} // end if

		newRoot.leftChild = oldRoot.leftChild.leftChild;
		newRoot.centerLeftChild = oldRoot.leftChild.rightChild;
		newRoot.centerRightChild = oldRoot.rightChild.leftChild;
		newRoot.rightChild = oldRoot.rightChild.rightChild;

		return newRoot;

	}// end fuse root case

	// two assumptions, this isn't a 2node and it has value to delete
	public void devolveLeafNode(TwoFourTreeItem node, int value) {

		// root 2node case
		if (node == root && root.isTwoNode()) {
			root = null;
		} // root leaf case

		if (node.isThreeNode()) {
			if (node.value1 == value) {
				node.value1 = node.value2;
				node.value2 = 0;
				node.values = 1;

			} // end v1 case
			else {
				node.value2 = 0;
				node.values = 1;
			} // end v2 case
		} // end 3node case
		else {

			if (node.value1 == value) {
				node.value1 = node.value2;
				node.value2 = node.value3;
				node.value3 = 0;
				node.values = 2;
			} // end v1 case
			else if (node.value2 == value) {
				node.value2 = node.value3;
				node.value3 = 0;
				node.values = 2;
			} // end v2 case
			else {
				node.value3 = 0;
				node.values = 2;
			} // end v3 case
		} // end 4node case
	}// end devolve leaf node

	// all merge cases

	// this method merges a 2node with a 3node parent
	public void merge2NodeFrom3Node(TwoFourTreeItem node) {

		if (node == node.parent.leftChild) {
			// leaf case no parent pointers need to be reassigned for children of node
			if (node.isLeaf) {

				// evolving node
				node.values = 3;
				node.value2 = node.parent.value1;
				node.value3 = node.parent.centerChild.value1;

				// devolving parent
				node.parent.value1 = node.parent.value2;
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.centerChild = null;
			} // end leaf case
			else {

				// evolve node
				node.values = 3;
				node.value2 = node.parent.value1;
				node.value3 = node.parent.centerChild.value1;

				// maintain children
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.centerChild.leftChild;
				node.rightChild = node.parent.centerChild.rightChild;
				node.parent.centerChild.leftChild.parent = node;
				node.parent.centerChild.rightChild.parent = node;

				// devolve parent
				node.parent.value1 = node.parent.value2;
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.centerChild = null;

			} // end non leaf case
		} // end left merge case
			// middle merge case
		else if (node == node.parent.centerChild) {

			// leaf case no parent pointers need to be reassigned for children of node
			if (node.isLeaf) {

				// evolve node
				node.values = 3;
				node.value2 = node.parent.value2;
				node.value3 = node.parent.rightChild.value1;

				// devolve parent
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.rightChild = node;
				node.parent.centerChild = null;
			} // end leaf case
				// leaf case
			else {

				// evolve node
				node.values = 3;
				node.value2 = node.parent.value2;
				node.value3 = node.parent.rightChild.value1;

				// maintain children
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.rightChild.leftChild;
				node.rightChild = node.parent.rightChild.rightChild;
				node.parent.rightChild.rightChild.parent = node;
				node.parent.rightChild.leftChild.parent = node;
				
				// devolve parent
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.rightChild = node;
				node.parent.centerChild = null;

			} // end non leaf case

		} // end middle merge case
			// right merge case
		else {

			if (node.isLeaf) {

				// evolve node
				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value2;
				node.value1 = node.parent.centerChild.value1;

				// devolve parent
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.centerChild = null;

			} // end leaf case
			else {

				// evolve node
				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value2;
				node.value1 = node.parent.centerChild.value1;

				// maintain children
				node.parent.centerChild.leftChild.parent = node;
				node.parent.centerChild.rightChild.parent = node;
				node.centerRightChild = node.leftChild;
				node.leftChild = node.parent.centerChild.leftChild;
				node.centerLeftChild = node.parent.centerChild.rightChild;

				// devolve parent
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.centerChild = null;

			} // end non leaf case
		} // end right merge case
	}// end merge 3node

	// this method merges a 2node with a 4node parent
	public void merge2NodeFrom4Node(TwoFourTreeItem node) {

		if (node == node.parent.leftChild) {

			if (node.isLeaf) {

				// evolve node
				node.values = 3;
				node.value2 = node.parent.value1;
				node.value3 = node.parent.centerLeftChild.value1;

				// devolve parent
				node.parent.value1 = node.parent.value2;
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node.parent.centerRightChild;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end leaf case
			else {

				// evolve node
				node.values = 3;
				node.value2 = node.parent.value1;
				node.value3 = node.parent.centerLeftChild.value1;

				// maintain children
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.centerLeftChild.leftChild;
				node.rightChild = node.parent.centerLeftChild.rightChild;
				node.parent.centerLeftChild.leftChild.parent = node;
				node.parent.centerLeftChild.rightChild.parent = node;

				// devolve parent
				node.parent.value1 = node.parent.value2;
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node.parent.centerRightChild;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end non leaf case
		} // end left merge case
			// centerLeft case
		else if (node == node.parent.centerLeftChild) {

			if (node.isLeaf) {

				// evolve node
				node.values = 3;
				node.value2 = node.parent.value2;
				node.value3 = node.parent.centerRightChild.value1;

				// devolve leaf
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end leaf case
			else {

				// evolve node
				node.values = 3;
				node.value2 = node.parent.value2;
				node.value3 = node.parent.centerRightChild.value1;

				// maintain children
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.centerRightChild.leftChild;
				node.rightChild = node.parent.centerRightChild.rightChild;
				node.parent.centerRightChild.leftChild.parent = node;
				node.parent.centerRightChild.rightChild.parent = node;

				// devolve parent
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end non leaf case
		} // end center left case
			// centerRight case
		else if (node == node.parent.centerRightChild) {

			// leaf case
			if (node.isLeaf) {
				
				// evolve node
				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value2;
				node.value1 = node.parent.centerLeftChild.value1;

				// devolve parent
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node.parent.centerRightChild;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;

			} // end leaf case
				// non leaf case
			else {

				// evolve node
				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value2;
				node.value1 = node.parent.centerLeftChild.value1;

				// maintain children
				node.centerRightChild = node.leftChild;
				node.centerLeftChild = node.parent.centerLeftChild.rightChild;
				node.leftChild = node.parent.centerLeftChild.leftChild;
				node.parent.centerLeftChild.rightChild.parent = node;
				node.parent.centerLeftChild.leftChild.parent = node;

				// devolve parent
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end non leaf case
		} // end centerRight case

		// right case
		else if (node == node.parent.rightChild) {

			// leaf
			if (node.isLeaf) {

				// evolve node
				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value3;
				node.value1 = node.parent.centerRightChild.value1;

				// devolve parent
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node.parent.centerLeftChild;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end leaf case
				// non leaf case
			else {
				
				// evolve node
				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value3;
				node.value1 = node.parent.centerRightChild.value1;

				// maintain children
				node.centerRightChild = node.leftChild;
				node.centerLeftChild = node.parent.centerRightChild.rightChild;
				node.leftChild = node.parent.centerRightChild.leftChild;
				node.parent.centerRightChild.rightChild.parent = node;
				node.parent.centerRightChild.leftChild.parent = node;

				// devolve parent
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node.parent.centerLeftChild;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end non leaf case
		} // end right merge case
	}// 4node merge case

	// all rotation cases

	// this method is responsible for the rotation case when the parent is a two
	// node and the sibling is a 3node
	public void rotate2Node3Node(TwoFourTreeItem node) {

		// we are in a left rotate case
		if (node == node.parent.leftChild) {

			// first the node current is at will grow from a 2node to a 3node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;

			// now change the value at the parent
			node.parent.value1 = node.parent.rightChild.value1;

			// now devolve the sibling into a 2node
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = 0;
			node.parent.rightChild.values = 1;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
			node.parent.rightChild.centerChild = null;

			// if we arent a leaf we will need to updatechildren's parent
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end parent update case

		} // end case left
			// we are at a right rotate case
		else {

			// first the node current is at will grow from a 2node to a 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;

			// now change the value at the parent
			node.parent.value1 = node.parent.leftChild.value2;

			// now devolve the sibling into a 2node
			node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
			node.parent.leftChild.value2 = 0;
			node.parent.leftChild.values = 1;
			node.parent.leftChild.centerChild = null;

			// if we arent a leaf we will need to updatechildren's parent
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end parent update case

		} // end case right
	}// end rotate 2node from 3Node

	// this method rotates a value in to a 2node from a 4node sibling, when the
	// parent is a 2node
	public void rotate2Node4Node(TwoFourTreeItem node) {

		// rotating to the left
		if (node == node.parent.leftChild) {

			// evolve node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;

			// edit parent value
			node.parent.value1 = node.parent.rightChild.value1;

			// devolve sibling
			node.parent.rightChild.values = 2;
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = node.parent.rightChild.value3;
			node.parent.rightChild.value3 = 0;
			node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
			node.parent.rightChild.centerLeftChild = null;
			node.parent.rightChild.centerRightChild = null;

			// node isnt a leaf, thus parents need to be updated.
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end leaf case

		} // end rotate left
			// rotating from the right
		else {

			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;

			// update parent value
			node.parent.value1 = node.parent.leftChild.value3;

			// devolve sibling
			node.parent.leftChild.values = 2;
			node.parent.leftChild.value3 = 0;
			node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
			node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
			node.parent.leftChild.centerLeftChild = null;
			node.parent.leftChild.centerRightChild = null;

			// node isnt a leaf, thus parents need to be updated.
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end leaf case
		} // end rotate right
	}// end rotate 2node from 4node

	// this method rotates a value from a 3node to a 2node with a 3node parent
	public void rotate3Node3Node(TwoFourTreeItem node) {

		// left rotate case
		if (node == node.parent.leftChild) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerChild.leftChild;

			// update parent value
			node.parent.value1 = node.parent.centerChild.value1;

			// devolve sibling
			node.parent.centerChild.values = 1;
			node.parent.centerChild.value1 = node.parent.centerChild.value2;
			node.parent.centerChild.value2 = 0;
			node.parent.centerChild.leftChild = node.parent.centerChild.centerChild;

			// update children's parent
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end parent update

		} // end left case
		else if (node == node.parent.centerChild && node.parent.leftChild.isThreeNode()) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;

			// update parent value
			node.parent.value1 = node.parent.leftChild.value2;

			// devolve sibling
			node.parent.leftChild.values = 1;
			node.parent.leftChild.value2 = 0;
			node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
			node.parent.leftChild.centerChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end parent update

		} // end centerleft case
		else if (node == node.parent.centerChild && node.parent.rightChild.isThreeNode()) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.parent.value2;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;

			// update parent value
			node.parent.value2 = node.parent.rightChild.value1;

			// devolve sibling
			node.parent.rightChild.values = 1;
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = 0;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
			node.parent.rightChild.centerChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end parent update

		} // end centerRight case
		else if (node == node.parent.rightChild) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value2;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.centerChild.rightChild;

			// update parent value
			node.parent.value2 = node.parent.centerChild.value2;

			// devolve sibling
			node.parent.centerChild.values = 1;
			node.parent.centerChild.value2 = 0;
			node.parent.centerChild.rightChild = node.parent.centerChild.centerChild;
			node.parent.centerChild.centerChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end parent update

		} // end right case
	}// end rotate3Node3Node

	// this method rotates a value from a 4node sibling with a 3node parent
	public void rotate3Node4Node(TwoFourTreeItem node) {

		// left rotate case
		if (node == node.parent.leftChild) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerChild.leftChild;

			// update parent value
			node.parent.value1 = node.parent.centerChild.value1;

			// devolve sibling
			node.parent.centerChild.values = 2;
			node.parent.centerChild.value1 = node.parent.centerChild.value2;
			node.parent.centerChild.value2 = node.parent.centerChild.value3;
			node.parent.centerChild.centerChild = node.parent.centerChild.centerRightChild;
			node.parent.centerChild.leftChild = node.parent.centerChild.centerLeftChild;
			node.parent.centerChild.centerLeftChild = null;
			node.parent.centerChild.centerRightChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end parent update

		} // end left case
		else if (node == node.parent.centerChild && node.parent.leftChild.isFourNode()) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;

			// update parent value
			node.parent.value1 = node.parent.leftChild.value3;

			// devolve sibling
			node.parent.leftChild.values = 2;
			node.parent.leftChild.value3 = 0;
			node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
			node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
			node.parent.leftChild.centerLeftChild = null;
			node.parent.leftChild.centerRightChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end parent update

		} // end centerleft case
		else if (node == node.parent.centerChild && node.parent.rightChild.isFourNode()) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.parent.value2;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;

			// update parent value
			node.parent.value2 = node.parent.rightChild.value1;

			// devolve sibling
			node.parent.rightChild.values = 2;
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = node.parent.rightChild.value3;
			node.parent.rightChild.value3 = 0;
			node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
			node.parent.rightChild.centerLeftChild = null;
			node.parent.rightChild.centerRightChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end parent update

		} // end centerRight case
		else if (node == node.parent.rightChild) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value2;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.centerChild.rightChild;

			// update parent value
			node.parent.value2 = node.parent.centerChild.value3;

			// devolve sibling
			node.parent.centerChild.values = 2;
			node.parent.centerChild.value3 = 0;
			node.parent.centerChild.centerChild = node.parent.centerChild.centerLeftChild;
			node.parent.centerChild.rightChild = node.parent.centerChild.centerRightChild;
			node.parent.centerChild.centerLeftChild = null;
			node.parent.centerChild.centerRightChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end parent update
		} // end right case
	}// end rotate3node4node

	// this method rotates a value in from a 3node to a 2node with a 4node parent
	public void rotate4Node3Node(TwoFourTreeItem node) {

		if (node == node.parent.leftChild) {

			// evolve node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerLeftChild.leftChild;

			// update parent value
			node.parent.value1 = node.parent.centerLeftChild.value1;

			// devlove sibling
			node.parent.centerLeftChild.values = 1;
			node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
			node.parent.centerLeftChild.value2 = 0;
			node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerChild;
			node.parent.centerLeftChild.centerChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end leaf case

		} // rotate left case
		else if (node == node.parent.centerLeftChild && node.parent.leftChild.isThreeNode()) {

			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;

			// update parent value
			node.parent.value1 = node.parent.leftChild.value2;

			// devolve sibling
			node.parent.leftChild.values = 1;
			node.parent.leftChild.value2 = 0;
			node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
			node.parent.leftChild.centerChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end nonleaf case
		} // rotate mid left case1
		else if (node == node.parent.centerLeftChild && node.parent.centerRightChild.isThreeNode()) {

			// evolve node
			node.values = 2;
			node.value2 = node.parent.value2;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerRightChild.leftChild;

			// update parent value
			node.parent.value2 = node.parent.centerRightChild.value1;

			// devolve sibling
			node.parent.centerRightChild.values = 1;
			node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
			node.parent.centerRightChild.value2 = 0;
			node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerChild;
			node.parent.centerRightChild.centerChild = null;

			// nonleaf case
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end nonleaf case

		} // rotate mid left case2
		else if (node == node.parent.centerRightChild && node.parent.centerLeftChild.isThreeNode()) {

			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value2;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.centerLeftChild.rightChild;

			// update parent value
			node.parent.value2 = node.parent.centerLeftChild.value2;

			// devlove sibling
			node.parent.centerLeftChild.values = 1;
			node.parent.centerLeftChild.value2 = 0;
			node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerChild;
			node.parent.centerLeftChild.centerChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end non leaf

		} // rotate mid right case1
		else if (node == node.parent.centerRightChild && node.parent.rightChild.isThreeNode()) {

			// evolve node
			node.values = 2;
			node.value2 = node.parent.value3;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;

			// update parent value
			node.parent.value3 = node.parent.rightChild.value1;

			// devlove sibling
			node.parent.rightChild.values = 1;
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = 0;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
			node.parent.rightChild.centerChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end nonleaf case
		} // rotate mid right case2
		else if (node == node.parent.rightChild) {

			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value3;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.centerRightChild.rightChild;

			// update parent value
			node.parent.value3 = node.parent.centerRightChild.value2;

			// devlove sibling
			node.parent.centerRightChild.values = 1;
			node.parent.centerRightChild.value2 = 0;
			node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerChild;
			node.parent.centerRightChild.centerChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end nonleaf case
		} // rotate right case
	}// end rotate4Node3node

	// this method rotates a value in from a 4node to a 2node with a 4node parent
	public void rotate4Node4Node(TwoFourTreeItem node) {
		
		// left case
		if (node == node.parent.leftChild) {

			// evolve node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerLeftChild.leftChild;

			// update parent value
			node.parent.value1 = node.parent.centerLeftChild.value1;

			// devlove sibling
			node.parent.centerLeftChild.values = 2;
			node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
			node.parent.centerLeftChild.value2 = node.parent.centerLeftChild.value3;
			node.parent.centerLeftChild.value3 = 0;
			node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerLeftChild;
			node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerRightChild;
			node.parent.centerLeftChild.centerLeftChild = null;
			node.parent.centerLeftChild.centerRightChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // nonleaf case
		} // rotate left case
		else if (node == node.parent.centerLeftChild && node.parent.leftChild.isFourNode()) {

			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;

			// update parent value
			node.parent.value1 = node.parent.leftChild.value3;

			// devlove sibling
			node.parent.leftChild.values = 2;
			node.parent.leftChild.value3 = 0;
			node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
			node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
			node.parent.leftChild.centerLeftChild = null;
			node.parent.leftChild.centerRightChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end nonleaf case
		} // rotate mid left case1
		else if (node == node.parent.centerLeftChild && node.parent.centerRightChild.isFourNode()) {

			// evolve node
			node.values = 2;
			node.value2 = node.parent.value2;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerRightChild.leftChild;

			// update parent value
			node.parent.value2 = node.parent.centerRightChild.value1;

			// devlove sibling
			node.parent.centerRightChild.values = 2;
			node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
			node.parent.centerRightChild.value2 = node.parent.centerRightChild.value3;
			node.parent.centerRightChild.value3 = 0;
			node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerRightChild;
			node.parent.centerLeftChild.leftChild = node.parent.centerRightChild.centerLeftChild;
			node.parent.centerRightChild.centerLeftChild = null;
			node.parent.centerRightChild.centerRightChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end nonleaf case
		} // rotate mid left case2
		else if (node == node.parent.centerRightChild && node.parent.centerLeftChild.isFourNode()) {

			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value2;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.centerLeftChild.rightChild;

			// update parent value
			node.parent.value2 = node.parent.centerLeftChild.value3;

			// devlove sibling
			node.parent.centerLeftChild.values = 2;
			node.parent.centerLeftChild.value3 = 0;
			node.parent.centerLeftChild.centerChild = node.parent.centerLeftChild.centerLeftChild;
			node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerRightChild;
			node.parent.centerLeftChild.centerLeftChild = null;
			node.parent.centerLeftChild.centerRightChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end nonleaf case
		} // rotate mid right case1
		else if (node == node.parent.centerRightChild && node.parent.rightChild.isFourNode()) {

			// evolve node
			node.values = 2;
			node.value2 = node.parent.value3;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;

			// update parent value
			node.parent.value3 = node.parent.rightChild.value1;

			// devlove sibling
			node.parent.rightChild.values = 2;
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = node.parent.rightChild.value3;
			node.parent.rightChild.value3 = 0;
			node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
			node.parent.rightChild.centerLeftChild = null;
			node.parent.rightChild.centerRightChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.rightChild.parent = node;
			} // end nonleaf case
		} // rotate mid right case2
		else if (node == node.parent.rightChild) {

			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value3;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.centerRightChild.rightChild;

			// update parent value
			node.parent.value3 = node.parent.centerRightChild.value3;

			// devlove sibling
			node.parent.centerRightChild.values = 2;
			node.parent.centerRightChild.value3 = 0;
			node.parent.centerRightChild.centerChild = node.parent.centerRightChild.centerLeftChild;
			node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerRightChild;
			node.parent.centerRightChild.centerLeftChild = null;
			node.parent.centerRightChild.centerRightChild = null;

			// non leafcase
			if (!node.isLeaf) {
				node.leftChild.parent = node;
			} // end nonleaf case
		} // rotate right case

	}// end rotate4Node4node

	// this method is takes in a node and returns true if we are in a rotation case,
	// or false if we are in a merge case
	public boolean rotateOrMerge(TwoFourTreeItem node) {

		// there are three base cases, either our parent is a two node, a three node, or
		// four node. each case will have additional sibling cases
		
		// 2node parent case
		if (node.parent.isTwoNode()) {

			// which child are we?
			if (node == node.parent.leftChild) {
				if (!node.parent.rightChild.isTwoNode()) {
					return true;
				} // rotate
				else {
					return false;
				} // merge
			} // end left case
			else if (node == node.parent.rightChild) {
				if (!node.parent.leftChild.isTwoNode()) {
					return true;
				} // rotate
				else {
					return false;
				} // merge
			} // end right case
		} // end 2node case
		else if (node.parent.isThreeNode()) {
			// which child are we?

			// left
			if (node == node.parent.leftChild) {
				if (!node.parent.centerChild.isTwoNode()) {
					return true;
				} // rotate
				else {
					return false;
				} // merge
			} // end left subcase
				// middle child
			else if (node == node.parent.centerChild) {

				if (!node.parent.leftChild.isTwoNode() || !node.parent.rightChild.isTwoNode()) {

					return true;
				} // rotate
				else {
					return false;
				} // merge
			} // end middle case
				// right child
			else if (node == node.parent.rightChild) {
				if (!node.parent.centerChild.isTwoNode()) {
					return true;
				} // end rotate
				else {
					return false;
				} // end merge
			} // end right case
		} // end 3node case
		else if (node.parent.isFourNode()) {
			// which child are we?

			// left child
			if (node == node.parent.leftChild) {
				if (!node.parent.centerLeftChild.isTwoNode()) {
					return true;
				} // rotate
				else {
					return false;
				} // merge
			} // end left case
				// centerLeft child
			else if (node == node.parent.centerLeftChild) {
				if (!node.parent.leftChild.isTwoNode() || !node.parent.centerRightChild.isTwoNode()) {
					return true;
				} // rotate
				else {
					return false;
				} // merge
			} // end centerLeft case
				// centerRight child
			else if (node == node.parent.centerRightChild) {
				if (!node.parent.centerLeftChild.isTwoNode() || !node.parent.rightChild.isTwoNode()) {
					return true;
				} // rotate
				else {
					return false;
				} // merge
			} // end centerRight case
				// right child
			else if (node == node.parent.rightChild) {
				if (!node.parent.centerRightChild.isTwoNode()) {
					return true;
				} // rotate case
				else {
					return false;
				} // merge
			} // end right case
		} // end 4node case
		return false;
	}// end rotateOrMerge
	
	public void traverseCur(TwoFourTreeItem cur, int value) {
		
	}
	
	public boolean deleteValue(int value) {

		// check if the value is present
		if (hasValue(value)) {
			
			// the tree only has one node (root), this can return null if root is a 2node
			if (root.isLeaf) {
				
				devolveLeafNode(root, value);
				return true;
			} // end leaf root case

			// the value we want to delete is contained in a 3node or 4node leaf node. We
			// can simply delete the value and maintain a valid b-tree
			if (delNode.isLeaf && !delNode.isTwoNode()) {
				
				devolveLeafNode(delNode, value);
				return true;
			} // end easy leaf case

			// we know the value exists and we want to delete it. Currently delNode is
			// referencing the node the value is to be deleted in.
			// we should first ask if we are in the special root merge case.
			if (root.leftChild.isTwoNode() && root.rightChild.isTwoNode() && root.isTwoNode()) {

				// merge the root
				root = mergeRoot(root);

				// we might have just created a new leaf root
				if (root.isLeaf) {
					devolveLeafNode(root, value);
					return true;
				} // end newly created root leaf case

				// update delNode reference
				hasValue(value);

			} // end merge root case

			// Let's create a traversing reference and merge or rotate any 2nodes on the way
			// towards delNode or suc/Pred.
			TwoFourTreeItem cur = root;

			// next thing we need to know is if we are in a 2node leaf case or not.
			if (delNode.isLeaf) {

				// we know the root is not a leaf node and that the value we want to delete is
				// at a 2node leaf case
				// since we dont want to start at the root and prematurely exit our loop, lets
				// traverse to a child of the root, and start traversing from there.

				if (root.isTwoNode()) {
					if (value < root.value1) {

						cur = root.leftChild;
					} // go left
					else {
						cur = root.rightChild;
						
					} // go right
				} // end 2node case
				else if (root.isThreeNode()) {
					if (value < root.value1) {

						cur = root.leftChild;

					} // go left
					else if (value < root.value2) {

						cur = root.centerChild;
					} // go mid
					else {

						cur = root.rightChild;

					} // go right
				} // end 3node case
					// 4node case
				else if(root.isFourNode()){
					if (value < root.value1) {
						cur = root.leftChild;
					} // go left
					else if (value < root.value2) {

						cur = root.centerLeftChild;

					} // go left mid
					else if (value < root.value3) {

						cur = root.centerRightChild;
					} // go right mid
					else {

						cur = root.rightChild;
					} // go right
				} // end 4node case

				// traversing until we hit delNode
				while (cur != delNode) {

					// traverse as normal if we are not at a 2node
					if (!cur.isTwoNode()) {
						// 3node case
						if (cur.isThreeNode()) {
							if (value < cur.value1) {
								cur = cur.leftChild;

							} // go left
							else if (value < cur.value2) {
								cur = cur.centerChild;
							} // go mid
							else {
								cur = cur.rightChild;

							} // go right
						} // end 3node case
						else if (cur.isFourNode()) {

							if (value < cur.value1) {
								cur = cur.leftChild;
							} // go left
							else if (value < cur.value2) {
								cur = cur.centerLeftChild;

							} // go mid left
							else if (value < cur.value3) {

								cur = cur.centerRightChild;

							} // go mid right
							else {
								cur = cur.rightChild;
							} // go right
						} // end 4node case
					} // end !2node case

					// we've hit a 2node, we must either merge or rotate.
					else {

						// we need to decide whether to rotate or merge. First let's ask a sibling if
						// they have a value to rotate with
						// true if rotation case, false if merge case
						if (rotateOrMerge(cur)) {

							// we are in a 2node parent rotation case
							if (cur.parent.isTwoNode()) {

								// what child are we?
								if (cur == cur.parent.leftChild) {

									// check what type of 2node rotation it is
									if (cur.parent.rightChild.isThreeNode()) {

										rotate2Node3Node(cur);
									} // 3node case
									else {

										rotate2Node4Node(cur);
									} // 4node case
								} // end left child case
								else {
									if (cur.parent.leftChild.isThreeNode()) {
										rotate2Node3Node(cur);
									} // 3node case
									else {
										rotate2Node4Node(cur);
									} // end 4node case
								} // end right child case

							} // end 2node parent case
							else if (cur.parent.isThreeNode()) {

								// which cild are we?

								// left child
								if (cur == cur.parent.leftChild) {

									// check what type of 3node rotate case it is
									if (cur.parent.centerChild.isThreeNode()) {
										rotate3Node3Node(cur);
									} // 3node rotation case
									else {
										rotate3Node4Node(cur);
									} // 4node rotation case
								} // end left case
									// middle child
								else if (cur == cur.parent.centerChild) {

									if (cur.parent.leftChild.isThreeNode() || cur.parent.rightChild.isThreeNode()) {

										rotate3Node3Node(cur);
									} // 3node rotation case
									else {
										rotate3Node4Node(cur);
									} // 4node rotation case
								} // end middle child case
									// right child
								else {
									if (cur.parent.centerChild.isThreeNode()) {
										rotate3Node3Node(cur);
									} // 3node rotatiion case
									else {
										rotate3Node4Node(cur);
									} // 4node rotation case
								} // end right child case
							} // end 3node parent case
								// 4node parent case
							else {
								// which child are we?

								// left child
								if (cur == cur.parent.leftChild) {

									// check sibling type
									if (cur.parent.centerLeftChild.isThreeNode()) {
										rotate4Node3Node(cur);
									} // 3node rotation case
									else {
										rotate4Node4Node(cur);
									} // 4node rotation case
								} // end left child case
									// centerLeft child
								else if (cur == cur.parent.centerLeftChild) {
									if (cur.parent.leftChild.isThreeNode() || cur.parent.centerRightChild.isThreeNode()) {

										rotate4Node3Node(cur);
									} // 3node rotation case
									else {
										rotate4Node4Node(cur);
									} // 4node rotation case
								} // end centerLeft case
								else if (cur == cur.parent.centerRightChild) {
									if (cur.parent.centerLeftChild.isThreeNode() || cur.parent.rightChild.isThreeNode()) {
										rotate4Node3Node(cur);
									} // 3node rotation case
									else {

										rotate4Node4Node(cur);
									} // 4node rotation case
								} // end centerRight child case
									// right child
								else {
									if (cur.parent.centerRightChild.isThreeNode()) {
										rotate4Node3Node(cur);
									} // 3node rotation case
									else {
										rotate4Node4Node(cur);
									} // 4node rotation case
								} // end right child case
							} // end 4node parent case
						} // end rotation cases
							// else nothing to borrow so we need to run merge cases
						else {

							if (cur.parent.isThreeNode()) {

								merge2NodeFrom3Node(cur);
							} // end 3node parent case
							else {

								merge2NodeFrom4Node(cur);
							} // end 4node parent case

						} // end merge cases
					} // end encountering a 2node

				} // end while ( traversal )
					// cur is at delNode, but before we delete we need to merge or rotate
					// we need to decide whether to rotate or merge. First let's ask a sibling if
					// they have a value to rotate with
					// true if rotation case, false if merge case
				if (rotateOrMerge(cur)) {

					// we are in a 2node parent rotation case
					if (cur.parent.isTwoNode()) {

						// what child are we?
						if (cur == cur.parent.leftChild) {

							// check what type of 2node rotation it is
							if (cur.parent.rightChild.isThreeNode()) {
								rotate2Node3Node(cur);
							} // 3node case
							else {
								rotate2Node4Node(cur);
							} // 4node case
						} // end left child case
						else {
							if (cur.parent.leftChild.isThreeNode()) {
								rotate2Node3Node(cur);
							} // 3node case
							else {
								rotate2Node4Node(cur);
							} // end 4node case
						} // end right child case

					} // end 2node parent case
					else if (cur.parent.isThreeNode()) {

						// which cild are we?

						// left child
						if (cur == cur.parent.leftChild) {

							// check what type of 3node rotate case it is
							if (cur.parent.centerChild.isThreeNode()) {

								rotate3Node3Node(cur);
							} // 3node rotation case
							else {
								rotate3Node4Node(cur);
							} // 4node rotation case
						} // end left case
							// middle child
						else if (cur == cur.parent.centerChild) {
							if (cur.parent.leftChild.isThreeNode() || cur.parent.rightChild.isThreeNode()) {

								rotate3Node3Node(cur);
							} // 3node rotation case
							else {
								rotate3Node4Node(cur);
							} // 4node rotation case
						} // end middle child case
							// right child
						else {
							if (cur.parent.centerChild.isThreeNode()) {
								rotate3Node3Node(cur);
							} // 3node rotatiion case
							else {
								rotate3Node4Node(cur);
							} // 4node rotation case
						} // end right child case
					} // end 3node parent case
						// 4node parent case
					else {
						// which child are we?

						// left child
						if (cur == cur.parent.leftChild) {

							// check sibling type
							if (cur.parent.centerLeftChild.isThreeNode()) {
								rotate4Node3Node(cur);
							} // 3node rotation case
							else {
								rotate4Node4Node(cur);
							} // 4node rotation case
						} // end left child case
							// centerLeft child
						else if (cur == cur.parent.centerLeftChild) {
							if (cur.parent.leftChild.isThreeNode() || cur.parent.centerRightChild.isThreeNode()) {

								rotate4Node3Node(cur);
							} // 3node rotation case
							else {

								rotate4Node4Node(cur);
							} // 4node rotation case
						} // end centerLeft case
						else if (cur == cur.parent.centerRightChild) {

							if (cur.parent.centerLeftChild.isThreeNode() || cur.parent.rightChild.isThreeNode()) {

								rotate4Node3Node(cur);
							} // 3node rotation case
							else {
								rotate4Node4Node(cur);
							} // 4node rotation case
						} // end centerRight child case
							// right child
						else {
							if (cur.parent.centerRightChild.isThreeNode()) {
								rotate4Node3Node(cur);
							} // 3node rotation case
							else {
								rotate4Node4Node(cur);
							} // 4node rotation case
						} // end right child case
					} // end 4node parent case
				} // end rotation cases
					// else nothing to borrow so we need to run merge cases
				else {

					if (cur.parent.isThreeNode()) {
						merge2NodeFrom3Node(cur);
					} // end 3node parent case
					else {

						merge2NodeFrom4Node(cur);
					} // end 4node parent case

				} // end merge cases

				// delete value

				devolveLeafNode(cur, value);
				return true;

			} // end leaf case
				// else we are an internal node
			else {

				// the value we want to delete is contained within an internal node
				// let's find its suc/pred. if it's suc/pred is a 2node, traverse to it, merging
				// any 2nodes along the way. if the suc/pred is not at a 2node, simply swap
				// values with it.
				TwoFourTreeItem sucPred = findSucPred(delNode, value);

				// check if we have to traverse or not
				if (sucPred.isTwoNode()) {

					// initialize cur reference
					if (root.isTwoNode()) {
						if (value <= root.value1) {

							cur = root.leftChild;
						} // go left
						else {
							cur = root.rightChild;
		
						} // go right
					} // end 2node case
					else if (root.isThreeNode()) {
						if (value <= root.value1) {
							cur = root.leftChild;
						} // go left
						else if (value <= root.value2) {

							cur = root.centerChild;
							
						} // go mid
						else {
							cur = root.rightChild;
						} // go right
					} // end 3node case
					else {
						if (value <= root.value1) {
							cur = root.leftChild;
						} // go left
						else if (value <= root.value2) {
							cur = root.centerLeftChild;
						} // go left mid
						else if (value <= root.value3) {
							cur = root.centerRightChild;
						} // go right mid
						else {
							cur = root.rightChild;
						} // go right
					} // end 4node case

					// traverse to sucPred, then swap values with value to delete.
					// traversing until we hit sucPred
					while (cur != sucPred) {

						// traverse as normal if we are not at a 2node
						if (!cur.isTwoNode()) {
							// 3node case
							if (cur.isThreeNode()) {
								if (value <= cur.value1) {
									cur = cur.leftChild;
								} // go left
								else if (value <= cur.value2) {
									cur = cur.centerChild;
	
								} // go mid
								else {
									cur = cur.rightChild;

								} // go right
							} // end 3node case
							else if (cur.isFourNode()) {
								if (value <= cur.value1) {
									cur = cur.leftChild;
								} // go left
								else if (value <= cur.value2) {
									cur = cur.centerLeftChild;
								} // go mid left
								else if (value <= cur.value3) {
									cur = cur.centerRightChild;
								} // go mid right
								else {
									cur = cur.rightChild;

								} // go right
							} // end 4node case
						} // end !2node case
							// we've hit a 2node, we must either merge or rotate.
						else {

							// we need to decide whether to rotate or merge. First let's ask a sibling if
							// they have a value to rotate with
							// true if rotation case, false if merge case
							if (rotateOrMerge(cur)) {
								// we are in a 2node parent rotation case
								if (cur.parent.isTwoNode()) {

									// what child are we?
									if (cur == cur.parent.leftChild) {

										// check what type of 2node rotation it is
										if (cur.parent.rightChild.isThreeNode()) {
											rotate2Node3Node(cur);
										} // 3node case
										else {
											rotate2Node4Node(cur);
										} // 4node case
									} // end left child case
									else {
										if (cur.parent.leftChild.isThreeNode()) {
											rotate2Node3Node(cur);
										} // 3node case
										else {
											rotate2Node4Node(cur);
										} // end 4node case
									} // end right child case

								} // end 2node parent case
									// 3node parent case
								else if (cur.parent.isThreeNode()) {

									// which cild are we?

									// left child
									if (cur == cur.parent.leftChild) {

										// check what type of 3node rotate case it is
										if (cur.parent.centerChild.isThreeNode()) {
											rotate3Node3Node(cur);
										} // 3node rotation case
										else {
											rotate3Node4Node(cur);
										} // 4node rotation case
									} // end left case
										// middle child
									else if (cur == cur.parent.centerChild) {
										if (cur.parent.leftChild.isThreeNode() || cur.parent.rightChild.isThreeNode()) {
											rotate3Node3Node(cur);
										} // 3node rotation case
										else {
											rotate3Node4Node(cur);
										} // 4node rotation case
									} // end middle child case
										// right child
									else {
										if (cur.parent.centerChild.isThreeNode()) {
											rotate3Node3Node(cur);
										} // 3node rotatiion case
										else {
											rotate3Node4Node(cur);
										} // 4node rotation case
									} // end right child case
								} // end 3node parent case
									// 4node parent case
								else {
									// which child are we?

									// left child
									if (cur == cur.parent.leftChild) {

										// check sibling type
										if (cur.parent.centerLeftChild.isThreeNode()) {
											rotate4Node3Node(cur);
										} // 3node rotation case
										else {
											rotate4Node4Node(cur);
										} // 4node rotation case
									} // end left child case
										// centerLeft child
									else if (cur == cur.parent.centerLeftChild) {
										if (cur.parent.leftChild.isThreeNode() || cur.parent.centerRightChild.isThreeNode()) {
											rotate4Node3Node(cur);
										} // 3node rotation case
										else {
											rotate4Node4Node(cur);
										} // 4node rotation case
									} // end centerLeft case
									else if (cur == cur.parent.centerRightChild) {
										if (cur.parent.centerLeftChild.isThreeNode() || cur.parent.rightChild.isThreeNode()) {
											rotate4Node3Node(cur);
										} // 3node rotation case
										else {
											rotate4Node4Node(cur);
										} // 4node rotation case
									} // end centerRight child case
										// right child
									else {
										if (cur.parent.centerRightChild.isThreeNode()) {
											rotate4Node3Node(cur);
										} // 3node rotation case
										else {
											rotate4Node4Node(cur);
										} // 4node rotation case
									} // end right child case
								} // end 4node parent case
							} // end rotation cases
								// else nothing to borrow so we need to run merge cases
							else {

								if (cur.parent.isThreeNode()) {
									merge2NodeFrom3Node(cur);
								} // end 3node parent case
								else {
									merge2NodeFrom4Node(cur);
								} // end 4node parent case

							} // end merge cases
						} // end encountering a 2node

					} // end while ( traversal )

					// before we delete me must rotate or merge one last time
					if (rotateOrMerge(cur)) {

						// we are in a 2node parent rotation case
						if (cur.parent.isTwoNode()) {

							// what child are we?
							if (cur == cur.parent.leftChild) {

								// check what type of 2node rotation it is
								if (cur.parent.rightChild.isThreeNode()) {
									rotate2Node3Node(cur);
								} // 3node case
								else {
									rotate2Node4Node(cur);
								} // 4node case
							} // end left child case
							else {
								if (cur.parent.leftChild.isThreeNode()) {
									rotate2Node3Node(cur);
								} // 3node case
								else {
									rotate2Node4Node(cur);
								} // end 4node case
							} // end right child case

						} // end 2node parent case
						else if (cur.parent.isThreeNode()) {

							// which cild are we?

							// left child
							if (cur == cur.parent.leftChild) {

								// check what type of 3node rotate case it is
								if (cur.parent.centerChild.isThreeNode()) {
									rotate3Node3Node(cur);
								} // 3node rotation case
								else {
									rotate3Node4Node(cur);
								} // 4node rotation case
							} // end left case
								// middle child
							else if (cur == cur.parent.centerChild) {
								if (cur.parent.leftChild.isThreeNode() || cur.parent.leftChild.isThreeNode()) {
			
									rotate3Node3Node(cur);
								} // 3node rotation case
								else {
									rotate3Node4Node(cur);
								} // 4node rotation case
							} // end middle child case
								// right child
							else {
								if (cur.parent.centerChild.isThreeNode()) {
									rotate3Node3Node(cur);
								} // 3node rotatiion case
								else {
									rotate3Node4Node(cur);
								} // 4node rotation case
							} // end right child case
						} // end 3node parent case
							// 4node parent case
						else {
							// which child are we?

							// left child
							if (cur == cur.parent.leftChild) {

								// check sibling type
								if (cur.parent.centerLeftChild.isThreeNode()) {
									rotate4Node3Node(cur);
								} // 3node rotation case
								else {
									rotate4Node4Node(cur);
								} // 4node rotation case
							} // end left child case
								// centerLeft child
							else if (cur == cur.parent.centerLeftChild) {
								if (cur.parent.leftChild.isThreeNode() || cur.parent.centerRightChild.isThreeNode()) {
									rotate4Node3Node(cur);
								} // 3node rotation case
								else {
									rotate4Node4Node(cur);
								} // 4node rotation case
							} // end centerLeft case
							else if (cur == cur.parent.centerRightChild) {
								if (cur.parent.centerLeftChild.isThreeNode() || cur.parent.rightChild.isThreeNode()) {
									rotate4Node3Node(cur);
								} // 3node rotation case
								else {
									rotate4Node4Node(cur);
								} // 4node rotation case
							} // end centerRight child case
								// right child
							else {
								if (cur.parent.centerRightChild.isThreeNode()) {
									rotate4Node3Node(cur);
								} // 3node rotation case
								else {

									rotate4Node4Node(cur);
								} // 4node rotation case
							} // end right child case
						} // end 4node parent case
					} // end rotation cases
						// else nothing to borrow so we need to run merge cases
					else {

						if (cur.parent.isThreeNode()) {
							merge2NodeFrom3Node(cur);
						} // end 3node parent case
						else {
							merge2NodeFrom4Node(cur);
						} // end 4node parent case

					} // end merge cases

					// update delnode
					hasValue(value);

					// delete value
					swapValues(delNode, cur, value);
					return true;
				} // end traversal case
				else {

					// the sucPred can just be swapped without traversing to it.
					// updating delNode ref before swap value
					hasValue(value);
					swapValues(delNode, sucPred, value);
					return true;
				} // end non traversal case

			} // end internal node case

		} // end if has value

		return false;
	}// end del node
	
	public void swapValues(TwoFourTreeItem deleteNode, TwoFourTreeItem spNode, int valueToDelete) {

		// check if they're at the same node
		if (deleteNode == spNode) {
			devolveLeafNode(deleteNode, valueToDelete);
		} // end references at the same node

		// delNode is at a 2node
		else if (deleteNode.isTwoNode()) {

			// now determine what type of node succ/pred is a 3node or 4node
			if (spNode.isThreeNode()) {

				// are we successor or predecessor?
				if (deleteNode.value1 > spNode.value2) {

					// swap, no reordering
					deleteNode.value1 = spNode.value2;

					// devolve leaf
					devolveLeafNode(spNode, spNode.value2);
				} // end predecessor
				else {

					// swap with reordering
					deleteNode.value1 = spNode.value1;

					// devolve the leaf we pulled from
					devolveLeafNode(spNode, spNode.value1);
				} // end successor
			} // end sp 3node case
			else if (spNode.isFourNode()) {
				if (deleteNode.value1 > spNode.value3) {
					deleteNode.value1 = spNode.value3;
					devolveLeafNode(spNode, spNode.value3);
				} // end predecessor case
				else {
					deleteNode.value1 = spNode.value1;
					devolveLeafNode(spNode, spNode.value1);
				} // end successor case
			} // end sp 4node case
		} // end 2node case
			// delNode is at a 3node
		else if (deleteNode.isThreeNode()) {

			// v1 case
			if (deleteNode.value1 == valueToDelete) {

				// now let's determine if sucPred node is at a 3node or a 4node
				if (spNode.isThreeNode()) {

					if (deleteNode.value1 > spNode.value2) {
						deleteNode.value1 = spNode.value2;
						devolveLeafNode(spNode, spNode.value2);
					} // end predecessor case
					else {
						deleteNode.value1 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case
				} // end 3node case
				else if (spNode.isFourNode()) {
					if (deleteNode.value1 > spNode.value3) {
						deleteNode.value1 = spNode.value3;
						devolveLeafNode(spNode, spNode.value3);
					} // end precessor case
					else {
						deleteNode.value1 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case
				} // end 4node case

			} // end v1 case
				// v2 case
			else {

				// now let's determine if sucPred node is at a 3node or a 4node
				if (spNode.isThreeNode()) {

					if (deleteNode.value2 > spNode.value2) {
						deleteNode.value2 = spNode.value2;
						devolveLeafNode(spNode, spNode.value2);
					} // end predecessor case
					else {
						deleteNode.value2 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case
				} // end 3node case
				else if (spNode.isFourNode()) {
					if (deleteNode.value2 > spNode.value3) {
						deleteNode.value2 = spNode.value3;
						devolveLeafNode(spNode, spNode.value3);
					} // end precessor case
					else {
						deleteNode.value2 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case
				} // end 4node case

			} // end v2 case

		} // end 3node case
			// delNode is at a 4node
		else if (deleteNode.isFourNode()) {

			// 3 cases v1,v2,v3 in each case spNode could be a 3node or a 4node, 6 cases
			// total

			// v1
			if (deleteNode.value1 == valueToDelete) {

				// what type is sp?
				// 3node case
				if (spNode.isThreeNode()) {
					if (deleteNode.value1 > spNode.value2) {
						deleteNode.value1 = spNode.value2;
						devolveLeafNode(spNode, spNode.value2);
					} // end predecessor case
					else {
						deleteNode.value1 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case
				} // end 3node case
					// 4node case
				else if (spNode.isFourNode()) {

					if (deleteNode.value1 > spNode.value3) {
						deleteNode.value1 = spNode.value3;
						devolveLeafNode(spNode, spNode.value3);
					} // end predecessor case
					else {
						deleteNode.value1 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case
				} // end 4node case
			} // end v1 case
				// v2
			else if (deleteNode.value2 == valueToDelete) {

				// what type is sp?
				// 3node case
				if (spNode.isThreeNode()) {

					if (deleteNode.value2 > spNode.value2) {
						deleteNode.value2 = spNode.value2;
						devolveLeafNode(spNode, spNode.value2);
					} // end predecessor case
					else {
						deleteNode.value2 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case

				} // end 3node case
					// 4node case
				else if (spNode.isFourNode()) {

					if (deleteNode.value2 > spNode.value3) {
						deleteNode.value2 = spNode.value3;
						devolveLeafNode(spNode, spNode.value3);
					} // end predecessor case
					else {
						deleteNode.value2 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case
				} // end 4node case
			} // end v2 case
				// v3
			else if (deleteNode.value3 == valueToDelete) {

				// what type is sp?
				// 3node case
				if (spNode.isThreeNode()) {

					if (deleteNode.value3 > spNode.value2) {
						deleteNode.value3 = spNode.value2;
						devolveLeafNode(spNode, spNode.value2);
					} // end predecessor case
					else {
						deleteNode.value3 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case

				} // end 3node case
					// 4node case
				else if (spNode.isFourNode()) {

					if (deleteNode.value3 > spNode.value3) {
						deleteNode.value3 = spNode.value3;
						devolveLeafNode(spNode, spNode.value3);
					} // end predecessor case
					else {
						deleteNode.value3 = spNode.value1;
						devolveLeafNode(spNode, spNode.value1);
					} // end successor case

				} // end 4node case

			} // end v3 case

		} // end delNode 4node case

	}// end swap values

	public void printInOrder() {
		if (root != null)
			root.printInOrder(0);
	}

	public TwoFourTree() {

	}
}

