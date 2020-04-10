package eg.edu.alexu.csd.filestructure.redblacktree;
import java.util.*;


public class MyRBT<T extends Comparable<T>,V> implements IRedBlackTree<T, V> {
	private INode<T,V> root;
	private int size;
	private INode<T,V> nullNode;
	
	public MyRBT(){
		size = 0 ;
		nullNode = new RBTNode();
		root = nullNode;
	}
	
	public INode<T, V> getRoot() {
		if (size>0)
		return root;
		else throw new IllegalAccessError("Tree is empty,no root to fetch");
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
		INode<T,V> node = Find(key,root);
		if (node.isNull())
		return null;
		else return node.getValue();
	}

	@Override
	public boolean contains(T key) {
		INode<T,V> node= Find(key,root);
		if(node.isNull())
			return false;
		else return true;
		
	}

	@Override
	public void insert(T key, V value) {
		INode<T,V> node=new RBTNode<T,V>(key,value); 
		if (size==0) {
			node.setColor(INode.BLACK);
			root=node;
			size++;
		}
		else {
			addTo(node,root);
		}
		node.setLeftChild(nullNode);
		node.setRightChild(nullNode);
	}

	@Override
	public boolean delete(T key) {
		INode<T,V> node = Find(key,root);
		
		if (node.isNull())
			return false;
		
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
		
		return true;
	}
	
	private void deleteBST(INode<T, V> node) {

		INode<T,V> left = node.getLeftChild(), right = node.getRightChild();
		INode<T,V> replacedNode = null;
		
		if (left.isNull() && right.isNull()) // Node has no children
		{
			node.setParent(null);
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
				INode<T,V> parent = node.getParent();
				parent.setRightChild(right);
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
				INode<T,V> parent = node.getParent();
				parent.setLeftChild(left);
				node.setParent(null);
				node.setLeftChild(null);
			}
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
		INode<T,V> sibling = null;
		if (parent.getLeftChild() == doubleBlack) // If the double black node is the left child
			sibling = parent.getRightChild();
		else // Double Black node is the right child
			sibling = parent.getLeftChild();
		
		
		
	}

	private INode<T, V> getSuccessor(INode<T, V> node) {
		
		INode<T,V> newNode = node.getRightChild();
		
		while (!newNode.getLeftChild().isNull()) // While newNode has left child
		{
			newNode = newNode.getLeftChild();
		}
		
		return newNode;
	}

	private void addTo(INode<T,V> nodeToAdd,INode<T,V> parentNode) {
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
		int check = node.getKey().compareTo(key);
		if (check==0)
			return node;
		else if (check < 0) {
			if (node.getRightChild().isNull())
				return null;
			else return Find(key,node.getRightChild());
		}
		else {
			if (node.getLeftChild().isNull())
				return null;
			else return Find(key,node.getLeftChild());
		}
	}
	
	private void fixTree() {
		
	}
	private void checkViolation() {}
	
	private void LeftRotation(INode<T,V> node) {
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
		return;
	}
	
    private void RightRotation(INode<T,V> node) {
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
		return;
	}
	
    private void printTree(INode<T,V> node) {
		if (node.isNull())
			return;
		else {
			/*
			System.out.println(node.getKey());
			printTree(node.getLeftChild());
			printTree(node.getRightChild());
			*/
			printTree(node.getLeftChild());
			System.out.println(node.getKey());
			printTree(node.getRightChild());
		}
	}
	
    
    
    public static void main(String[] args) {
	    MyRBT<Integer,String> tree = new MyRBT<Integer,String>();
	    tree.insert(10, "hi");
	    tree.insert(15, "ho");
	    tree.insert(2, "he");
	    tree.insert(300, "ha");
	    tree.insert(4, "hu");
	    tree.insert(14, "hu");
	    tree.insert(16, "hu");
	    tree.insert(9, "hu");
	    tree.insert(1, "hu");
	    tree.insert(0, "hu");
	    
	    System.out.println("Before Rotation");
	    tree.printTree(tree.getRoot());
	    
	    tree.LeftRotation(tree.Find(2,tree.getRoot()));
	    tree.RightRotation(tree.Find(15,tree.getRoot()));
	    
	    System.out.println();
	    System.out.println("After Rotation");
	    tree.printTree(tree.getRoot());

	}
    
}
