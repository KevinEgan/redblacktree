
//package com.adsg.tree;

/**
 * This is a "generic" Binary Search Tree - know the definition of what a BST is!
 * 
 * NOTE: To allow for our objects to be inserted (and found) properly they have to be COMPARED
 * to the objects in the tree. This is why we have <T extends Comparable<T>> instead of 
 * just <T> : We are effectively saying that the objects which can be stored MUST implement
 * the Comparable interface.
 * 
 * NOTE: Our Node class is an inner class in an inner class at the bottom of the code.
 * 
 * @author dermot.hegarty
 *
 * @param <T>
 */
public class RedBlackTree<T extends Comparable<T>> {
	/**
	 * Reference to the root of the tree
	 */
	public Node root;

	/**
	 * This is the public insert method, i.e. the one that the outside world will invoke.
	 * It then kicks off a recursive method to "walk" through down through the tree - this is 
	 * possible because each sub-tree is itself a tree.
	 * @param value Object to insert into the tree
	 */
	public void insert(T value){
		Node node = new Node(value); // Create the Node to add

		//Special case that cannot be handled recursively
		if ( root == null ) {
			root = node;

			//Remember that new nodes default to Red but
			//the root must always be black
			node.nodeColourRed = false;
			return;
		}

		//Initially we start at the root. Each subsequent recursive call will be to a 
		//left or right subtree.
		insertRec(root, node);

		//Now that we've inserted we need to make it Red-Black (if necessary)
		handleRedBlack(node);

	}

	/**
	 * 
	 * @param subTreeRoot The SubTree to insert into
	 * @param node The Node that we wish to insert
	 */
	protected void insertRec(Node subTreeRoot, Node node){

		//Note the call to the compareTo() method. This is only possible if our objects implement
		//the Comparable interface.
		if ( node.value.compareTo(subTreeRoot.value) < 0){

			//This is our terminal case for recursion. We should be going left but there is 
			//no leaf node there so that is obviously where we must insert
			if ( subTreeRoot.left == null ){
				subTreeRoot.left = node;
				node.parent = subTreeRoot;
				return; //return here is unnecessary
			}
			else{ // Note that this allows duplicates!
				
				//Now our new "root" is the left subTree
				insertRec(subTreeRoot.left, node);
			}
		}
		//Same logic for the right subtree
		else{
			if (subTreeRoot.right == null){
				subTreeRoot.right = node;
				node.parent = subTreeRoot;
				return;
			}
			else{
				insertRec(subTreeRoot.right, node);
			}
		}
	}
	
	
	/**
	 * Should traverse the tree "in-order." See the notes
	 */
	public void inOrderTraversal()
	{
		//start at the root and recurse
		recInOrderTraversal(root);
	}
	
	public void preOrderTraversal()
	{
		//start at the root and recurse
		recPreOrderTraversal(root);
	}
	
	public void postOrderTraversal()
	{
		//start at the root and recurse
		recPostOrderTraversal(root);
	}
	
	/**
	 * This allows us to recursively process the tree "in-order". Note that it is private
	 * @param subTreeRoot
	 */
	private void recInOrderTraversal(Node subTreeRoot)
	{
		if(subTreeRoot == null) return;
		
		recInOrderTraversal(subTreeRoot.left);
		processNode(subTreeRoot);
		recInOrderTraversal(subTreeRoot.right);
	}
	
	private void recPreOrderTraversal (Node subTreeRoot)
	{
		if(subTreeRoot == null) return;
		
		processNode(subTreeRoot);
		recPreOrderTraversal(subTreeRoot.left);
		recPreOrderTraversal(subTreeRoot.right);
	}
	
	private void recPostOrderTraversal (Node subTreeRoot)
	{
		if(subTreeRoot == null) return;
		
		recPostOrderTraversal(subTreeRoot.left);
		recPostOrderTraversal(subTreeRoot.right);
		processNode(subTreeRoot);
	}
	
	/** 
	 * Do some "work" on the node - here we just print it out 
	 * @param currNode
	 */
	private void processNode(Node currNode)
	{
		System.out.println(currNode.toString());
	}
	
	/**
	 * 
	 * @return The number of nodes in the tree
	 */
	public int countNodes()
	{
		return recCountNodes(root);
	}
	
	
	/**
	 * Note: This is a practical example of a simple usage of pre-order traversal
	 * @param subTreeRoot
	 * @return
	 */
	private int recCountNodes(Node subTreeRoot)
	{
		if (subTreeRoot == null) return 0;
		
		//Look at the pre-order. "Count this node and THEN count the left and right 
		//subtrees recursively
		return 1 + recCountNodes(subTreeRoot.left) + recCountNodes(subTreeRoot.right);
	}

    public void findMinimum(){
        Node result = root;
        while(result.left != null){
            result = result.left;
        }

        System.out.println("Minimum value is: " + result.value);
    }

	public T findMinimumRecursive()
	{
		return findMinimumRecursive(root);
	}

	private T findMinimumRecursive(Node subTreeRoot)
	{
		if (subTreeRoot.left == null) {
			return subTreeRoot.value;
		}
		return findMinimumRecursive(subTreeRoot.left);
	}

	public T find(T searchVal)
	{
		//start at tge root and recurse
		return recFind(root, searchVal);
	}

	//recursive find method
	//subtree root is the root of the subtree being searched
	//searchVal is the value being searched for
	private T recFind(Node subTreeRoot, T searchVal){
		//null node means value not found
		if (subTreeRoot == null) return null;

		//if the search value is less than the current node value, go left
		if (searchVal.compareTo(subTreeRoot.value) < 0){
			return recFind(subTreeRoot.left, searchVal);
		}
		//if the search value is greater than the current node value, go right
		else if (searchVal.compareTo(subTreeRoot.value) > 0){
			return recFind(subTreeRoot.right, searchVal);
		}
		//value found
		else{
			return subTreeRoot.value;
		}
	}

	//performs a left rotation on a subtree with a root of 'subRoot'. This fixes violations
	public Node rotateSubTreeLeft(Node subRoot){

		//1. Identify the nodes that need to change positions
		//the right child of the subRoot becomes the new root of the subtree
		Node newRoot = subRoot.right;
		//the left child of the newRoot will become the right child of the subRoot
		Node newChildOfOldRoot = newRoot.left;

		//2. Rotation of nodes
		newRoot.left = subRoot;
		subRoot.right = newChildOfOldRoot;

		//3. Update parent pointers
		//newRoot's parent  now becomes the subRoot's parent
		newRoot.parent = subRoot.parent;
		//subRoot's parent is now newRoot
		subRoot.parent = newRoot;
		//if the new child is not null, update its parent to be the subRoot
		if (newChildOfOldRoot != null){
			newChildOfOldRoot.parent = subRoot;
		}

		//4.Update global root if necessary or reconnect to the parent
		if (newRoot.parent == null){
			//this is when the subRoot was the global root
			root = newRoot;
		}
		else if (newRoot.parent.left == subRoot){
			//this means subRoot was a left child
				newRoot.parent.left = newRoot;
			}
		else{
			//this means subRoot was a right child
				newRoot.parent.right = newRoot;
			}
		
		//return the new subtree root
		return newRoot;
	}

	//performs a right rotation on a subtree with a root of 'subRoot'. This fixes violations
	public Node rotateSubTreeRight(Node subRoot){
		//1. Identify the nodes that need to change positions
		//the left child of the subRoot becomes the new root of the subtree
		Node newRoot = subRoot.left;
		//the right child of the newRoot will become the left child of the subRoot
		Node newChildOfOldRoot = newRoot.right;

		//2. Rotation of nodes
		newRoot.right = subRoot;
		subRoot.left = newChildOfOldRoot;

		//3. Update parent pointers
		//newRoot's parent  now becomes the subRoot's parent
		newRoot.parent = subRoot.parent;
		//subRoot's parent is now newRoot
		subRoot.parent = newRoot;
		//if the new child is not null, update its parent to be the subRoot
		if(newChildOfOldRoot != null){
			newChildOfOldRoot.parent = subRoot;
		}

		//4. Update global root if necessary or reconnect to the parent
		if (newRoot.parent == null){
			//this is when the subRoot was the global root
			root = newRoot;
		}
		else if (newRoot.parent.left == subRoot){
			//this means subRoot was a left child
			newRoot.parent.left = newRoot;
		}
		else{

			//this means subRoot was a right child
			newRoot.parent.right = newRoot;
		}
		//return the new subtree root
		return newRoot;
	}

	//these methods rotate the entire tree left or right at the root and update the global root
	public void rotateTreeLeft(){
		root = rotateSubTreeLeft(root);
	}
	public void rotateTreeRight(){
		root = rotateSubTreeRight(root);
	}
	
	/**
* Note: This method may be called recursively but only for the case
* where the Uncle is red (assuming that the parent node is red - which
* is a violation, of course)
* @param newNode
*/
void handleRedBlack(Node newNode)
{
	//terminating case for "back" recursion - e.g. case 3 (video)
	if(newNode == root)
	{
		newNode.nodeColourRed = false;
		return;
	}
	Node uncle;
	Node parent = newNode.parent;
	Node grandParent = parent.parent;
	//Now that it's inserted we try to ensure that it's a RedBlack Tree
	//Check if parent is red. This is a violation. I (the new node) am red
	//so my parent cannot also be red!
	if(parent.nodeColourRed)
	{
		//important that we figure out where the uncle is
		//relative to the current node
		/*If the parent value is less than the grandparent value, then the parent is on the left to the grandParent
		so the uncle must be on the right of the grandParent.*/
		if(parent.value.compareTo(grandParent.value) < 0)
		{
			uncle = grandParent.right;
		}
		else
		{
			uncle = grandParent.left;
		}
		//Now we need to check if x's uncle is RED (Grandparent must
		//have been black)
		//This is case 3 according to the video
		//(https://www.youtube.com/watch?v=g9SaX0yeneU)
		if((uncle != null) && (uncle.nodeColourRed))
		{
			//this case is not too bad.
			//it involves recolouring and then recursing
			//CODE OMITTED - it's only 4 lines!
			grandParent.nodeColourRed = true;
			parent.nodeColourRed = false;
			uncle.nodeColourRed = false;
			handleRedBlack(grandParent); //recurse up the tree
		}

		//Check if uncle is Black (four subcases to handle)
		//http://www.geeksforgeeks.org/red-black-tree-set-2-insert/
		
		//if uncle is black (or null)
		else if((uncle == null) || !uncle.nodeColourRed)
		{
			Node newRoot = null;
			
			//if the parent is the left child of the grandparent
			if (grandParent.left == parent){
				//Left Right case
				if (parent.right == newNode){
					System.out.println("Left-Right case detected");
					newRoot = applyLeftRightCase(grandParent, parent);

					//Left Left case
				} else {
					System.out.println("Left-Left case detected");
					newRoot = applyLeftLeftCase(grandParent);
				}
			}
			//if the parent if the right child of the grandparent
			else {
				//Right Left case
				if (parent.left == newNode){
					System.out.println("Right-Left case detected");
					newRoot = applyRightLeftCase(grandParent, parent);
					
					//Right Right case
				} else {
					System.out.println("Right-Right case detected");
					newRoot = applyRightRightCase(grandParent);
				}
			}

			
		}
	}
	//debug info if neither case applies (maybe unneccesary?)
	else
	{
		System.out.println("No Red-Black violation detected");
	}
}
	

	//handles the Left Left case violation.
	public Node applyLeftLeftCase(Node grandparent){

		//rotate right around the grandparent (the root of the subtree)
		Node newRoot = rotateSubTreeRight(grandparent);
		
		//recolour nodes after rotation. The new root becomes black and the old root becomes red
		newRoot.nodeColourRed = false;
		if (newRoot.right != null){
			newRoot.right.nodeColourRed = true;
		}
		return newRoot;
	}

	//handles Right Right case violation.
	public Node applyRightRightCase(Node grandparent){
		//rotate left around the grandparent (the root of the subtree)
		Node newRoot = rotateSubTreeLeft(grandparent);

		//recolour nodes after rotation. The new root becomes black and the old root becomes red
		newRoot.nodeColourRed = false;
		if (newRoot.left != null){
			newRoot.left.nodeColourRed = true;
		}
		return newRoot;
	}

	//handle Left Right case violation
	public Node applyLeftRightCase(Node grandparent, Node parent){
		//first rotate left around the parent which converts it to a Left Left case
		rotateSubTreeLeft(parent);
		//finally handle the Left Left case
		return applyLeftLeftCase(grandparent);
	}

	//handle Right Left case violation
	public Node applyRightLeftCase(Node grandparent, Node parent){
		//first rotate right around the parent which converts it to a Right Right case
		rotateSubTreeRight(parent);
		//finally handle the Right Right case
		return applyRightRightCase(grandparent);
	}

	

	/////////////////////////////////////////////////////////////////
	/**
	 * Our Node contains a value and a reference to the left and right subtrees (initially null)
	 * @author dermot.hegarty
	 *
	 */
	protected class Node {
		public T value; //value is the actual object that we are storing
		public Node left;
		public Node right;
		public Node parent;
		public boolean nodeColourRed; //only used for red-black trees

		public Node(T value) {
			this.value = value;
			this.left = null;
			this.right = null;
			this.parent = null;
			this.nodeColourRed = true; // new nodes default to red
		}

		@Override
		public String toString() {
			return "Node [value=" + value + " , Colour=" + (nodeColourRed ? "Red" : "Black") + "]";
		}
		
		

	}
}