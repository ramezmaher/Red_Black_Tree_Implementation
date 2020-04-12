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
		node.setLeftChild(nullNode);
		node.setRightChild(nullNode);
		if (size==0) {
			node.setColor(INode.BLACK);
			root=node;
			size++;
		}
		else {
			addTo(node,root);
			checkViolation(root);
			size++;
		}
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
				System.out.println("Uncle red " + node.getKey());
				reColor(node);
				checkViolation(root);
			}
			else {
				System.out.println("Uncle black " + node.getKey());
			 if (isLeftChild(node)) {
				if (isLeftChild(node.getParent())) {
					LeftLeftCase(node);
					checkViolation(root);
				}
				else {
					LeftRightCase(node);
					checkViolation(root);
				}
			 }
			 else {
				 if (isLeftChild(node.getParent())) {
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
	    tree.printTree(tree.getRoot());
	}
}
