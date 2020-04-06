package eg.edu.alexu.csd.filestructure.redblacktree;

public class MyRBT<T extends Comparable<T>,V> implements IRedBlackTree<T, V> {
	private INode<T,V> root;
	private int size;
	
	public MyRBT(){
		size = 0 ;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public V search(T key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(T key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(T key, V value) {
		INode<T,V> node=new RBTNode<T,V>(key,value); 
		if (size==0) {
			node.setColor(INode.BLACK);
			root=node;
			size++;
			return;
		}
		else {
			
		}
	}

	@Override
	public boolean delete(T key) {
		// TODO Auto-generated method stub
		return false;
	}
	private void addTo(INode<T,V> nodeToAdd,INode<T,V> parentNode) {
		
	}

}
