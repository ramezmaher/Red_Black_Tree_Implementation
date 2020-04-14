package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.management.RuntimeErrorException;

import org.junit.Assert;

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
		
		T largestKey = tree.getLargest();
		int check = key.compareTo(largestKey);
		if (check > 0)
			return null;
		
		if (tree.contains(key))
		{
			V value = tree.search(key);
			Entry<T,V> result = new AbstractMap.SimpleEntry<T,V>(key,value);
			return result;
		}
		
		T smallest = tree.getSmallest();
		check = smallest.compareTo(key);
		while (check < 0)
		{
			smallest = tree.getSuccessor(tree.Find(smallest, tree.getRoot())).getKey();
			check = smallest.compareTo(key);
		}
		
		Entry<T,V> result = new AbstractMap.SimpleEntry<T,V>(smallest, tree.search(smallest));
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
		
		T key = tree.getSmallest();
		while (key != null)
		{
			Entry<T,V> entry = new AbstractMap.SimpleEntry<T,V>(key, tree.search(key));
			result.add(entry);
			key = tree.getSuccessor(tree.Find(key, tree.getRoot())).getKey();
		}
		return result;
	}

	@Override
	public Entry<T, V> firstEntry() {

		if (tree.isEmpty())
				return null;
		
		T smallest = tree.getSmallest();
		Map.Entry<T,V> entry = new AbstractMap.SimpleEntry<T,V>(smallest, tree.search(smallest));
		return entry;
	}

	@Override
	public T firstKey() {
		
		if (tree.isEmpty())
				return null;
		
		return tree.getSmallest();
	}

	@Override
	public Entry<T, V> floorEntry(T key) {
		if (key==null)
			throw new RuntimeErrorException(e);
		
		if (tree.isEmpty())
				throw new RuntimeErrorException(e);
		
		T smallestKey = tree.getSmallest();
		int check = key.compareTo(smallestKey);
		if (check < 0)
			return null;
		
		if (tree.contains(key))
		{
			V value = tree.search(key);
			Entry<T,V> result = new AbstractMap.SimpleEntry<T,V>(key,value);
			return result;
		}
		
		T largestKey = tree.getSmallest();
		INode<T,V> largest = tree.Find(largestKey, tree.getRoot());
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
		
		if (toKey==null)
			throw new RuntimeErrorException(e);
		
		T smallestKey = tree.getSmallest();
		INode<T,V> node = tree.Find(smallestKey, tree.getRoot());
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
		
		if (toKey==null)
			throw new RuntimeErrorException(e);
		
		if (!inclusive)
			return headMap(toKey);
		
		T smallestKey = tree.getSmallest();
		INode<T,V> node = tree.Find(smallestKey, tree.getRoot());
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
		
		T key = tree.getSmallest();
		INode<T,V> node = tree.Find(key, tree.getRoot());
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
				return null;
		
		T largest = tree.getLargest();
		Map.Entry<T,V> entry = new AbstractMap.SimpleEntry<T,V>(largest, tree.search(largest));
		return entry;
	}

	@Override
	public T lastKey() {
		
		if (tree.isEmpty())
				return null;
		
		return tree.getLargest();
	}

	@Override
	public Entry<T, V> pollFirstEntry() {
		
		if (tree.isEmpty())
				return null;
		
		Entry<T,V> entry = firstEntry();
		tree.delete(entry.getKey());
		return entry;
	}

	@Override
	public Entry<T, V> pollLastEntry() {
		
		if (tree.isEmpty())
				return null;
		
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
		
		if (map == null)
			throw new RuntimeErrorException(e);
		
		for (T key : map.keySet())
			tree.insert(key, map.get(key));
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
		
		T key = (T) tree.getSmallest();
		INode<T,V> node = tree.Find(key, tree.getRoot());
		while (!node.isNull())
		{
			result.add(node.getValue());
			node = tree.getSuccessor(node);
		}
		return result;
	}
	public static void main(String[] args) {
    ITreeMap<Integer, String> treemap = (ITreeMap<Integer, String>) TestRunner.getImplementationInstanceForInterface(ITreeMap.class);
			TreeMap<Integer, String> t = new TreeMap<>();
			Assert.assertEquals(t.lastEntry(), treemap.lastEntry());
			t.put(5, "soso" + 5);
			treemap.put(5, "soso" + 5);
			Assert.assertEquals(t.lastEntry(), treemap.lastEntry());	
			Random r = new Random();
			for (int i = 0; i < 1000; i++) {
				int key = r.nextInt(100000);
				t.put(key, "soso" + key);
				treemap.put(key, "soso" + key);
			}
		System.out.println(t.lastEntry()+ " " + treemap.lastEntry());
	}

}
