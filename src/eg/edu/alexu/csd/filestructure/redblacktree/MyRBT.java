package eg.edu.alexu.csd.filestructure.redblacktree;
import java.util.*;

import javax.management.RuntimeErrorException;


public class MyRBT<T extends Comparable<T>,V> implements IRedBlackTree<T, V> {
	private INode<T,V> root;
	private int size;
	private INode<T,V> nullNode;
	private INode<T,V> smallest,largest ;
	private Error ex;
	
	public MyRBT(){
		size = 0 ;
		nullNode = new RBTNode<T,V>();
		smallest = null;
		largest = null;
		root = nullNode;
		
	}
	
	public INode<T, V> getRoot() {
		if (size>0)
		return root;
		else throw new RuntimeErrorException(ex);
	}

	public boolean isEmpty() {
		if (size == 0)
		    return true;
		else 
			return false;
	}
	
	@Override
	public void clear() {
		if (root.isNull())
			return;
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
			smallest = node;
			largest = node;
		}
		else {
			addTo(node,root);
			checkViolation(root);
		}

		int check = key.compareTo(smallest.getKey());
		if (check < 0)
			smallest = node;
		
		check = key.compareTo(largest.getKey());
		if (check > 0)
			largest = node;
		
		size++;

	}

	@Override
	public boolean delete(T key) {
		if (key==null)
			throw new RuntimeErrorException(ex);
		INode<T,V> node = Find(key,root);
		
		if (node.isNull())
			return false;
		
		if (node == smallest)
		{
			INode<T,V> successor = getSuccessor(node);
			if (!successor.isNull())
				smallest = successor;
			else
				smallest = null;
		}
		if (node == largest)
		{
			INode<T,V> predecessor = getPredecessor(node);
			if (!predecessor.isNull())
				largest = predecessor;
			else
				largest = null;
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
				size = 0;
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
				node.setRightChild(null);
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
				node.setLeftChild(null);
			}
		}
		
		if (replacedNode.isNull()) // Deleting a leaf node
		{
			if (parent.getLeftChild().isNull() && parent.getRightChild().isNull()) //Deletion makes parent leaf node
			{
				if (parent.getColor() == INode.BLACK)
				{
					fixDoubleBlack(parent);
					return;
				}
				else
				{
					parent.setColor(INode.RED);
					return;
				}
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
		
		if (node == largest)
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
		if (node == smallest)
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

	public INode<T,V> getSmallest()
	{
		return this.smallest;
	}
	
	public INode<T,V> getLargest()
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

 	private INode<T,V> Find(T key,INode<T,V> node) {
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
		if (node.isNull()) {
			return;
		}
		if ( node.getParent()==null && node.getColor() == INode.RED) {
			node.setColor(INode.BLACK);
		    checkViolation(node.getLeftChild());
		    checkViolation(node.getRightChild());
		}
		else if (node.getColor()==INode.RED && node.getParent().getColor()==INode.RED) {
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
    		dummy.setColor(INode.RED);
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
	    System.out.println("Smallest here is: "+tree.smallest.getKey()+", Largest here is: "+tree.largest.getKey());
	    
	    System.out.println("Checking successor method");
	    INode node = tree.getSmallest();
	    while (!node.isNull())
	    {
	    	System.out.println(node.getKey());
	    	node = tree.getSuccessor(node);
	    }
	    
	    System.out.println("Check predecessor method");
	    INode node2 = tree.getLargest();
	    while (!node2.isNull())
	    {
	    	System.out.println(node2.getKey());
	    	node2 = tree.getPredecessor(node2);
	    }
	    /*
	    System.out.println("Before Rotation");

	    tree.printTree(tree.getRoot());
	    
	    while (!tree.isEmpty()) {
	    	tree.delete(tree.getRoot().getKey());
	    	System.out.println("");
	    	tree.printTree(tree.getRoot());
	    }

	    System.out.println();
	    System.out.println("After Rotation");
	    tree.printTree(tree.getRoot());
		*/
	    
	    


	}
}
