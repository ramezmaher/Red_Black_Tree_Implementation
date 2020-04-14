package eg.edu.alexu.csd.filestructure.redblacktree;
import java.util.*;

import javax.management.RuntimeErrorException;


public class MyRBT<T extends Comparable<T>,V> implements IRedBlackTree<T, V> {
	private INode<T,V> root;
	private int size;
	private INode<T,V> nullNode;
	private T smallest, largest;
	private Error ex;
	
	public MyRBT(){
		size = 0 ;
		nullNode = new RBTNode<T,V>();
		root = nullNode;
		
	}
	
	public INode<T, V> getRoot() {
		if (root.isNull() || root == null)
			return null;
		
		return root;
	}

	public boolean isEmpty() {
		if (size == 0)
		    return true;
		else 
			return false;
	}
	
	@Override
	public void clear() {

		root=nullNode;
		size=0;
		return;
		
	}

	@Override
	public V search(T key) {
		if (key==null)
			throw new RuntimeErrorException(ex);
		INode<T,V> node = Find(key,root);
		if (node.isNull())
		return null;
		else return node.getValue();
	}

	@Override
	public boolean contains(T key) {
		if (key == null)
			throw new RuntimeErrorException(ex);
		INode<T,V> node= Find(key,root);
		if(node.isNull())
			return false;
		else return true;
		
	}

	@Override
	public void insert(T key, V value) {
		if (key==null || value==null)
			throw new RuntimeErrorException(ex);
		INode<T,V> node=new RBTNode<T,V>(key,value);
		node.setLeftChild(nullNode);
		node.setRightChild(nullNode);
		if (size==0) {
			node.setColor(INode.BLACK);
			root=node;
			smallest = node.getKey();
			largest = node.getKey();
		}
		else {
			addTo(node);
			CheckViolation(node);
		int check = key.compareTo(smallest);
		if (check < 0)
			smallest = key;
		
		check = key.compareTo(largest);
		if (check > 0)
			largest = key;
		}
		size++;
	}

	@Override
	public boolean delete(T key) {
		if (key==null)
			throw new RuntimeErrorException(ex);
		INode<T,V> node = Find(key,root);
		
		if (node.isNull())
			return false;
		
		if (size == 1)
		{
			root = nullNode;
			largest = null;
			smallest = null;
			size = 0;
			return true;
		}
		
		if (node.getKey().compareTo(smallest) == 0)
		{
			INode<T,V> successor = getSuccessor(node);
			if (successor == null || successor.isNull())
				smallest = null;
			else
				smallest = successor.getKey();
		}
		if (node.getKey().compareTo(largest) == 0)
		{
			INode<T,V> predecessor = getPredecessor(node);
			if (predecessor == null || predecessor.isNull())
				largest = null;
			else
				largest = predecessor.getKey();
		}
		
		INode<T,V> left = node.getLeftChild();
		INode<T,V> right = node.getRightChild();
		
		if (!left.isNull() && !right.isNull()) // Node has 2 Children
		{
			INode<T,V> successor = getSuccessor(node);
			node.setValue(successor.getValue());
			node.setKey(successor.getKey());
			deleteBST(successor);
		}
		else
		{
			deleteBST(node);
		}

		size--;
		return true;
	}
	
	public int size()
	{
		return this.size;
	}
	
	private void deleteBST(INode<T, V> node) {

		INode<T,V> left = node.getLeftChild(), right = node.getRightChild(), parent = node.getParent();
		INode<T,V> replacedNode = nullNode;
		
		if (left.isNull() && right.isNull()) // Node has no children
		{
			node.setParent(null);
			if (parent == null) //Deleting root node
			{
				root = nullNode;
				return;
			}
			else
			{
				if (node == parent.getRightChild()) // Node is a right child
					parent.setRightChild(nullNode);
				else
					parent.setLeftChild(nullNode);
			}
		}
		else if (left.isNull()) // Node has only right child
		{
			replacedNode = right;
			
			if (root.equals(node)) // Deleting root node
			{
				root = right;
				root.setParent(null);
			}
			else
			{
				if (node == parent.getLeftChild()) //Deleted Node is a left child
					parent.setLeftChild(right);
				else
					parent.setRightChild(right);		
				
				right.setParent(parent);
				node.setParent(null);
				node.setRightChild(nullNode);
			}
		}
		else if (right.isNull()) // Node has only left child
		{
			replacedNode = left;
			
			if (root.equals(node)) // Deleting root node
			{
				root = left;
				root.setParent(null);
			}
			else
			{
				if (node == parent.getLeftChild()) // Deleted node is a left child
					parent.setLeftChild(left);
				else
					parent.setRightChild(left);
					
				left.setParent(parent);
				node.setParent(null);
				node.setLeftChild(nullNode);
			}
		}
		
		if (replacedNode.isNull()) // Deleting a leaf node
		{
			if (parent.getLeftChild().isNull() && parent.getRightChild().isNull()) //Deletion makes parent leaf node
			{
				if (node.getColor() == INode.BLACK)
				{
					if (parent.getColor() == INode.BLACK)
					{
						fixDoubleBlack(parent);
						return;
					}
					else
					{
						parent.setColor(INode.BLACK);
						return;
					}
				}
				else
					return;
			}
			else
				return;
		}
		
		if (replacedNode.getColor() == INode.RED || node.getColor() == INode.RED)
		{
			replacedNode.setColor(INode.BLACK);
		}
		else
		{
			INode<T,V> doubleBlack = replacedNode;
			fixDoubleBlack(doubleBlack);
		}
		
		
	}
	
	private void fixDoubleBlack(INode<T, V> doubleBlack) {
		if (doubleBlack == root)
			return;
		
		if (doubleBlack.getLeftChild() == null)
			doubleBlack.setLeftChild(nullNode);
		if (doubleBlack.getRightChild() == null)
			doubleBlack.setRightChild(nullNode);
		
		INode<T,V> parent = doubleBlack.getParent();
		INode<T,V> sibling ;
		if (parent.getLeftChild() == doubleBlack) // If the double black node is the left child
			sibling = parent.getRightChild();
		else // Double Black node is the right child
			sibling = parent.getLeftChild();
		
		if (sibling.getColor() == INode.RED)
		{
			if (doubleBlack == parent.getLeftChild())
			{
				LeftRotation(parent);
				fixDoubleBlack(doubleBlack);
			}
			else
			{
				RightRotation(parent);
				fixDoubleBlack(doubleBlack);
			}
		}
		else // Black Sibling
		{
			if (sibling.getLeftChild().getColor() == INode.BLACK && sibling.getRightChild().getColor() == INode.BLACK)
			{
				sibling.setColor(INode.RED);
				
				if (parent.getColor() == INode.RED)
				{
					parent.setColor(INode.BLACK);
					return;
				}
				else // Black Parent
				{
					fixDoubleBlack(parent); // Pop the Double Black Node up
				}
			}
			else // At least one red child
			{
				INode<T,V> red;
				if (sibling.getRightChild().getColor() == INode.RED) // XR case
				{
					red = sibling.getRightChild();
					if (sibling == parent.getRightChild()) // RR case
					{
						LeftRotation(parent);
						red.setColor(parent.getColor());
						return;
					}
					else // LR case
					{
						LeftRotation(sibling);
						red.setColor(INode.BLACK);
						sibling.setColor(INode.RED);
						fixDoubleBlack(doubleBlack);
					}
				}
					
				else // XL case
				{
					red = sibling.getLeftChild();
					if (sibling == parent.getRightChild()) // RL case
					{
						RightRotation(sibling);
						red.setColor(INode.BLACK);
						sibling.setColor(INode.RED);
						fixDoubleBlack(doubleBlack);
					}
					else // LL case
					{
						RightRotation(parent);
						red.setColor(parent.getColor());
						return;
					}
				}
			}
		}
		
	}

	public INode<T, V> getSuccessor(INode<T, V> node) {
		
		if (node.getKey().compareTo(largest) == 0)
			return nullNode;
		
		INode<T,V> newNode;
		if (!node.getRightChild().isNull()) // Node has right child
		{
			newNode = node.getRightChild();
			while (!newNode.getLeftChild().isNull()) // While newNode has left child
			{
				newNode = newNode.getLeftChild();
			}
		}
		else
		{
			newNode = node.getParent();
			while (node == newNode.getRightChild()) // While node is the right child
			{
				node = newNode;
				newNode = newNode.getParent();
			}
		}
		
		return newNode;
	}
	
	public INode<T,V> getPredecessor(INode<T,V> node)
	{
		if (node == null || node.getKey() == null || smallest == null )
			return null;
		if (node.isNull() || (node.getKey().compareTo(smallest) == 0))
			return nullNode;
		
		INode<T,V> newNode;
		if (!node.getLeftChild().isNull()) // Node has left child
		{
			newNode = node.getLeftChild();
			while (!newNode.getRightChild().isNull()) // While newNode has right child
			{
				newNode = newNode.getRightChild();
			}
		}
		else
		{
			newNode = node.getParent();
			while (node == newNode.getLeftChild()) // While node is the left child
			{
				node = newNode;
				newNode = newNode.getParent();
			}
		}
		return newNode;
	}

	public T getSmallest()
	{
		return this.smallest;
	}
	
	public T getLargest()
	{
		return this.largest;
	}
	
	private void addTo(INode<T,V> nodeToAdd,INode<T,V> parentNode) {
		if (nodeToAdd.isNull() || parentNode.isNull())
			return;
		int check = nodeToAdd.getKey().compareTo(parentNode.getKey());
		if (check < 0) {
			if (parentNode.getLeftChild().isNull()) {
				parentNode.setLeftChild(nodeToAdd);
				nodeToAdd.setParent(parentNode);
			}
			else addTo(nodeToAdd,parentNode.getLeftChild());
		}
		else if(check > 0) {
			if (parentNode.getRightChild().isNull()) {
				parentNode.setRightChild(nodeToAdd);
				nodeToAdd.setParent(parentNode);
			}
			else addTo(nodeToAdd,parentNode.getRightChild());
		}
		else {
			parentNode.setValue(nodeToAdd.getValue());
		}
	}
	
	private void addTo(INode<T,V> node) {
		if(node.isNull())
			return;
		int check;
		INode<T,V> ref = root;
		while(true) {
			check = node.getKey().compareTo(ref.getKey());
			if (check < 0) {
				if (ref.getLeftChild().isNull()) {
					ref.setLeftChild(node);
					node.setParent(ref);
					break;
				}
				else ref=ref.getLeftChild();
			}
			else if(check > 0) {
				if (ref.getRightChild().isNull()) {
					ref.setRightChild(node);
					node.setParent(ref);
					break;
				}
				else ref=ref.getRightChild();
			}
			else {
				ref.setValue(node.getValue());
				break;
			}
		}
		
	}

 	INode<T,V> Find(T key,INode<T,V> node) {
 		if (node.isNull())
 			return nullNode;
		int check = node.getKey().compareTo(key);
		if (check==0)
			return node;
		else if (check < 0) {
			if (node.getRightChild().isNull())
				return nullNode;
			else return Find(key,node.getRightChild());
		}
		else {
			if (node.getLeftChild().isNull())
				return nullNode;
			else return Find(key,node.getLeftChild());
		}
	}
	
	private void checkViolation(INode<T,V> node) {
		if (node.isNull() || node==null)
			return;
		if (node.getColor()==INode.RED && node.getParent().getColor()==INode.RED) {
			if (getUncleColor(node)==INode.RED) {
				reColor(node);
				checkViolation(root);
			}
			else {
			 if (isLeftChild(node.getParent())) {
				if (isLeftChild(node)) {
					LeftLeftCase(node);
					checkViolation(root);
				}
				else {
					LeftRightCase(node);
					checkViolation(root);
				}
			 }
			 else {
				 if (isLeftChild(node)) {
					 RightLeftCase(node);
					 checkViolation(root);
				 }
				 else {
					 RightRightCase(node);
					 checkViolation(root);
				 }
			   }
		    }
		}
		else {
		checkViolation(node.getLeftChild());
		checkViolation(node.getRightChild());
		}
	}
	
	private void CheckViolation(INode<T,V> node) {
	    	INode<T,V> dummy ,parent ;
	    	dummy = node;
			while (dummy.getParent() != null) {	
			parent = dummy.getParent();
			if (dummy.getColor()==INode.RED && dummy.getParent().getColor()==INode.RED) {
			if (getUncleColor(dummy)==INode.RED) {
				reColor(dummy);
				dummy = parent;
			}
			else {
			 if (isLeftChild(parent)) {
				if (isLeftChild(dummy)) {
					LeftLeftCase(dummy);
					dummy = parent;
				}
				else {
					LeftRightCase(dummy);
					dummy = parent;
				}
			 }
			 else {
				 if (isLeftChild(dummy)) {
					 RightLeftCase(dummy);
					 dummy = parent;
				 }
				 else {
					 RightRightCase(dummy);
					 dummy = parent ;
				 }
			 }
		 }
	   }
			else dummy = parent;
	 }
	root.setColor(INode.BLACK);		
	}
	
	private void ColorSwap(INode<T,V> node1,INode<T,V> node2) {
		if (node1.getColor()==INode.BLACK) {
			node1.setColor(INode.RED);
			node2.setColor(INode.BLACK);
		}
		else {
			node1.setColor(INode.BLACK);
			node2.setColor(INode.RED);
		}
		
	}
	
	private boolean getUncleColor(INode<T,V> node) {
		INode<T,V> grandParent = node.getParent().getParent();
		INode<T,V> Parent = node.getParent();
		if (grandParent.getLeftChild().isNull() || grandParent.getLeftChild().getKey().compareTo(Parent.getKey()) != 0) {
			return grandParent.getLeftChild().getColor();
		}
		else return grandParent.getRightChild().getColor();
	} 
	
	private boolean isLeftChild(INode<T,V> node) {
		INode<T,V> Parent  = node.getParent();
		if (node.getColor() == Parent.getLeftChild().getColor()) {
			if (node.getKey().compareTo(Parent.getLeftChild().getKey()) == 0)
				return true;
		}
		return false;
	}
	
	private void LeftRotation(INode<T,V> node) {
		if (node.isNull() || node.getRightChild().isNull())
			return;
		INode<T,V> dummy1 = node.getRightChild();
		INode<T,V> dummy2 = dummy1.getLeftChild();
		node.setRightChild(dummy2);
		dummy1.setLeftChild(node);
		if (node.getParent()!=null) {
			dummy1.setParent(node.getParent());
			if (dummy1.getKey().compareTo(dummy1.getParent().getKey()) > 0 )
		    	dummy1.getParent().setRightChild(dummy1);
		    else 
		    	dummy1.getParent().setLeftChild(dummy1);
		}
		else {
			dummy1.setParent(null);
			root = dummy1;
		}
		if (!dummy2.isNull())
		dummy2.setParent(node);
		
		node.setParent(dummy1);
		
		return;
	}
	
    private void RightRotation(INode<T,V> node) {
		if (node.isNull() || node.getLeftChild().isNull())
			return;
		INode<T,V> dummy1 = node.getLeftChild();
		INode<T,V> dummy2 = dummy1.getRightChild();
		node.setLeftChild(dummy2);
		dummy1.setRightChild(node);
		if (node.getParent()!=null) {
			dummy1.setParent(node.getParent());
		    if (dummy1.getKey().compareTo(dummy1.getParent().getKey()) > 0 )
		    	dummy1.getParent().setRightChild(dummy1);
		    else 
		    	dummy1.getParent().setLeftChild(dummy1);
		}
		else {
			dummy1.setParent(null);
			root = dummy1;
		}
		if (!dummy2.isNull())
		dummy2.setParent(node);
		
		node.setParent(dummy1);
		
		return;
	}
    
    private void reColor(INode<T,V> node) {
    	if (node.getParent().isNull() || node.getParent().getParent().isNull())
    		return;
    		INode<T,V> dummy = node.getParent().getParent();
    		if (dummy.getParent() != null) {
    		dummy.setColor(INode.RED);
    		}
    		dummy.getLeftChild().setColor(INode.BLACK);
    		dummy.getRightChild().setColor(INode.BLACK);
    }
	
    private void printTree(INode<T,V> node) {
    	if (node==null) {
    		System.out.println("Nothing to print");
    		return;
    	}
		Queue<INode<T,V>> q = new LinkedList<INode<T,V>>();
		q.add(node);
		while (!q.isEmpty()) {
			INode<T,V> dummy = q.poll();
			if(!dummy.getLeftChild().isNull())
				q.add(dummy.getLeftChild());
			if (!dummy.getRightChild().isNull())
				q.add(dummy.getRightChild());
			System.out.println(dummy.getKey()+ " " + dummy.getColor());	
		}
		
	}
	
    private void LeftLeftCase(INode<T,V> node) {
    	INode<T,V> grandParent = node.getParent().getParent();
    	INode<T,V> parent = node.getParent();
    	
    	RightRotation(grandParent);
    	ColorSwap(grandParent,parent);
    	
    } 

    private void LeftRightCase(INode<T,V> node) {
    	INode<T,V> parent = node.getParent();
    	
    	LeftRotation(parent);
    	LeftLeftCase(parent);
    	
    }
    
    private void RightRightCase(INode<T,V> node) {
    	INode<T,V> grandParent = node.getParent().getParent();
    	INode<T,V> parent = node.getParent();
    	LeftRotation(grandParent);
    	ColorSwap(grandParent,parent);
    }
    
    private void RightLeftCase(INode<T,V> node) {
    	INode<T,V> parent = node.getParent();
    	
    	RightRotation(parent);
    	RightRightCase(parent);
    }
    
    public static void main(String[] args) {

	    MyRBT<Integer,String> redBlackTree = new MyRBT<Integer,String>();
	    Random r = new Random();
		for(int i = 0; i < 100; i++) {
			int key = r.nextInt(1000);
			redBlackTree.insert(key, "toto" + key);
			System.out.println(redBlackTree.getRoot().getKey() + " " + redBlackTree.getRoot().getColor());
		}

	    MyRBT<Integer,String> Tree = new MyRBT<Integer,String>();
	    Tree.insert(20, "soso");
		Tree.insert(15, "soso");
		Tree.insert(10, "soso");
		Tree.insert(7, "soso");
		Tree.insert(9, "soso");
		Tree.insert(12, "soso");
		Tree.insert(24, "soso");
		Tree.insert(22, "soso");
		Tree.insert(13, "soso");
		Tree.insert(11, "soso");
		Tree.printTree(Tree.getRoot());

	    /*
*/
//	    tree.printTree(tree.getRoot());

	    
/*	    for (int i=0; i<4 ; i++)
	    	tree.delete(tree.getRoot().getKey());
	    
	    System.out.println("My root right now is "+tree.getRoot().getKey());
	    System.out.println("My predecessor now is "+tree.getPredecessor(tree.getRoot()).getKey());
    	
	    while (!tree.isEmpty()) {
	    	tree.delete(tree.getRoot().getKey());
	    	System.out.println("");
	    	tree.printTree(tree.getRoot());
	    	System.out.println("MY SMALLEST IS "+tree.getSmallest().getKey()+", MY LARGEST IS "+tree.getLargest().getKey());
	    }
/*
	    System.out.println();
	    System.out.println("After Rotation");
	    tree.printTree(tree.getRoot());
		*/
	    
	

	}
}
