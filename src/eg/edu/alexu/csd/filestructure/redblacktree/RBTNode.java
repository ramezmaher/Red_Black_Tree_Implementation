package eg.edu.alexu.csd.filestructure.redblacktree;

public class RBTNode<T extends Comparable<T>,V> implements INode<T, V> {
    private boolean Color;
	private T Key;
	private V Value;
	private INode<T,V> Parent;
	private INode<T,V> RightChild;
	private INode<T,V> LeftChild;
	@Override
	public void setParent(INode<T, V> parent) {
		this.Parent=parent; 
	}

	@Override
	public INode<T, V> getParent() {
		return this.Parent;
	}

	@Override
	public void setLeftChild(INode<T, V> leftChild) {
		this.LeftChild=leftChild;
	}

	@Override
	public INode<T, V> getLeftChild() {
		return this.LeftChild;
	}

	@Override
	public void setRightChild(INode<T, V> rightChild) {
		this.RightChild=rightChild;
	}

	@Override
	public INode<T, V> getRightChild() {
		return this.RightChild;
	}

	@Override
	public T getKey() {
		return this.Key;
	}

	@Override
	public void setKey(T key) {
		this.Key=key;
	}
	@Override
	public V getValue() {
		return this.Value;
	}

	@Override
	public void setValue(V value) {
		this.Value=value;
	}

	@Override
	public boolean getColor() {
		return this.Color;
	}

	@Override
	public void setColor(boolean color) {
		this.Color=color;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
