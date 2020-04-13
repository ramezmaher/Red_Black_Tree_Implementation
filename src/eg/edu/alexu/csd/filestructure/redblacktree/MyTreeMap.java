package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.RuntimeErrorException;

public class MyTreeMap<T extends Comparable<T>,V> implements ITreeMap<T, V> {

	private MyRBT<T,V> tree;
	private Error e;
	
	public MyTreeMap()
	{
		tree = new MyRBT<T,V>();
	}
	
	@Override
	public Entry<T, V> ceilingEntry(T key) {
		
		if (key==null)
			throw new RuntimeErrorException(e);
		
		if (tree.isEmpty())
			throw new RuntimeErrorException(e);
		
		T largestKey = tree.getLargest().getKey();
		int check = key.compareTo(largestKey);
		if (check > 0)
			return null;
		
		if (tree.contains(key))
		{
			V value = tree.search(key);
			Entry<T,V> result = new AbstractMap.SimpleEntry<T,V>(key,value);
			return result;
		}
		
		INode<T,V> smallest = tree.getSmallest();
		check = smallest.getKey().compareTo(key);
		while (check < 0)
		{
			smallest = tree.getSuccessor(smallest);
			check = smallest.getKey().compareTo(key);
		}
		
		Entry<T,V> result = new AbstractMap.SimpleEntry<T,V>(smallest.getKey(), smallest.getValue());
		return result;
	}

	@Override
	public T ceilingKey(T key) {
		
		if (key==null)
			throw new RuntimeErrorException(e);
		
		return ceilingEntry(key).getKey();
	}

	@Override
	public void clear() {
		
		tree.clear();
		
	}

	@Override
	public boolean containsKey(T key) {
		
		if (key==null)
			throw new RuntimeErrorException(e);
		
		return tree.contains(key);
	}

	@Override
	public boolean containsValue(V value) {
		
		if (value==null)
			throw new RuntimeErrorException(e);
		
		Collection<V> values = values();
		return values.contains(value);
	}

	@Override
	public Set<Entry<T, V>> entrySet() {
		
		Set<Entry<T,V>> result = new HashSet<Entry<T,V>>();
		
		if (tree.isEmpty())
			return result;
		
		INode<T,V> node = tree.getSmallest();
		while (!node.isNull())
		{
			Entry<T,V> entry = new AbstractMap.SimpleEntry<T,V>(node.getKey(), node.getValue());
			result.add(entry);
			node = tree.getSuccessor(node);
		}
		return result;
	}

	@Override
	public Entry<T, V> firstEntry() {

		if (tree.isEmpty())
				throw new RuntimeErrorException(e);
		
		INode<T,V> smallest = tree.getSmallest();
		Map.Entry<T,V> entry = new AbstractMap.SimpleEntry<T,V>(smallest.getKey(), smallest.getValue());
		return entry;
	}

	@Override
	public T firstKey() {
		
		if (tree.isEmpty())
				throw new RuntimeErrorException(e);
		
		return tree.getSmallest().getKey();
	}

	@Override
	public Entry<T, V> floorEntry(T key) {
		if (key==null)
			throw new RuntimeErrorException(e);
		
		if (tree.isEmpty())
				throw new RuntimeErrorException(e);
		
		T smallestKey = tree.getSmallest().getKey();
		int check = key.compareTo(smallestKey);
		if (check < 0)
			return null;
		
		if (tree.contains(key))
		{
			V value = tree.search(key);
			Entry<T,V> result = new AbstractMap.SimpleEntry<T,V>(key,value);
			return result;
		}
		
		INode<T,V> largest = tree.getSmallest();
		check = largest.getKey().compareTo(key);
		while (check > 0)
		{
			largest = tree.getPredecessor(largest);
			check = largest.getKey().compareTo(key);
		}
		
		Entry<T,V> result = new AbstractMap.SimpleEntry<T,V>(largest.getKey(), largest.getValue());
		return result;
	}

	@Override
	public T floorKey(T key) {
		
		if (key==null)
			throw new RuntimeErrorException(e);
		
		return floorEntry(key).getKey();
	}

	@Override
	public V get(T key) {
		
		if (key==null)
			throw new RuntimeErrorException(e);
		
		return tree.search(key);
	}

	@Override
	public ArrayList<Entry<T, V>> headMap(T toKey) {
		
		INode<T,V> node = tree.getSmallest();
		ArrayList<Entry<T,V>> result = new ArrayList<Entry<T,V>>();
		
		if (tree.isEmpty())
			return result;
		
		int check = node.getKey().compareTo(toKey);
		while (check < 0)
		{
			Map.Entry<T,V> entry = new AbstractMap.SimpleEntry<T,V>(node.getKey(), node.getValue());
			result.add(entry);
			node = tree.getSuccessor(node);
			check = node.getKey().compareTo(toKey);
		}
		return result;
	}

	@Override
	public ArrayList<Entry<T, V>> headMap(T toKey, boolean inclusive) {
		
		if (!inclusive)
			return headMap(toKey);
		
		INode<T,V> node = tree.getSmallest();
		ArrayList<Entry<T,V>> result = new ArrayList<Entry<T,V>>();
		
		if (tree.isEmpty())
			return result;
		
		int check = node.getKey().compareTo(toKey);
		while (check < 0 || check == 0)
		{
			Map.Entry<T,V> entry = new AbstractMap.SimpleEntry<T,V>(node.getKey(), node.getValue());
			result.add(entry);
			node = tree.getSuccessor(node);
			check = node.getKey().compareTo(toKey);
		}
		return result;
	}

	@Override
	public Set<T> keySet() {
		
		Set<T> result = new HashSet<T>();
		
		if (tree.isEmpty())
			return result;
		
		INode<T,V> node = tree.getSmallest();
		while (!node.isNull())
		{
			result.add(node.getKey());
			node = tree.getSuccessor(node);
		}
		return result;
	}

	@Override
	public Entry<T, V> lastEntry() {
		
		if (tree.isEmpty())
				throw new RuntimeErrorException(e);
		
		INode<T,V> largest = tree.getLargest();
		Map.Entry<T,V> entry = new AbstractMap.SimpleEntry<T,V>(largest.getKey(), largest.getValue());
		return entry;
	}

	@Override
	public T lastKey() {
		
		if (tree.isEmpty())
				throw new RuntimeErrorException(e);
		
		return tree.getLargest().getKey();
	}

	@Override
	public Entry<T, V> pollFirstEntry() {
		
		if (tree.isEmpty())
				throw new RuntimeErrorException(e);
		
		Entry<T,V> entry = firstEntry();
		tree.delete(entry.getKey());
		return entry;
	}

	@Override
	public Entry<T, V> pollLastEntry() {
		
		if (tree.isEmpty())
				throw new RuntimeErrorException(e);
		
		Entry<T,V> entry = lastEntry();
		tree.delete(entry.getKey());
		return entry;
	}

	@Override
	public void put(T key, V value) {
		
		if (key==null || value==null)
			throw new RuntimeErrorException(e);
		
		tree.insert(key, value);
	}

	@Override
	public void putAll(Map<T, V> map) {
		
		ArrayList<Entry<T,V>> mappings = headMap(lastKey(), true);
		for (int i = 0; i < mappings.size(); i++)
			map.put(mappings.get(i).getKey(), mappings.get(i).getValue());
		
	}

	@Override
	public boolean remove(T key) {
		
		if (key==null)
			throw new RuntimeErrorException(e);
		
		return tree.delete(key);
	}

	@Override
	public int size() {
		
		return tree.size();
	}

	@Override
	public Collection<V> values() {
		
		Collection<V> result = new LinkedList<V>();
		
		if (tree.isEmpty())
			return result;
		
		INode<T,V> node = tree.getSmallest();
		while (!node.isNull())
		{
			result.add(node.getValue());
			node = tree.getSuccessor(node);
		}
		return result;
	}

}
