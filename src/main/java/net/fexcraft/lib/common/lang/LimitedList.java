package net.fexcraft.lib.common.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.fexcraft.lib.common.utils.Print;

/**
 * @author Ferdinand Calo' (FEX___96)
 *
 * General List with index limit.
 * //TODO improve // see arraylist in this package
 * @param <E>
 */
public class LimitedList<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_SIZE_LIMIT = 10;
	private ArrayList<E> list;
	private int maxsize;
	
	public LimitedList(){
		this(DEFAULT_SIZE_LIMIT);
	}
	
	public LimitedList(int size){
		list = new ArrayList<E>(size);
		maxsize = size;
	}
	
	@Override
	public boolean add(E object){
		if(list.size() + 1 > maxsize){
			Print.log("Array limit reached.");
			return false;
		}
		return list.add(object);
	}

	@Override
	public void add(int index, E element){
		if(list.size() + 1 > maxsize){
			Print.log("Array limit reached.");
			return;
		}
		list.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> col){
		if(list.size() + col.size() > maxsize){
			Print.log("Array limit reached.");
			return false;
		}
		return list.addAll(col);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c){
		if(list.size() + c.size() > maxsize){
			Print.log("Array limit reached.");
			return false;
		}
		list.addAll(index, c);
		return true;
	}

	@Override
	public int size(){
		return list.size();
	}

	@Override
	public boolean isEmpty(){
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o){
		return list.contains(o);
	}

	@Override
	public Iterator<E> iterator(){
		return list.iterator();
	}

	@Override
	public Object[] toArray(){
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a){
		return list.toArray(a);
	}

	@Override
	public boolean remove(Object o){
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c){
		return list.containsAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c){
		list.removeAll(c);
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c){
		list.retainAll(c);
		return false;
	}

	@Override
	public void clear(){
		list.clear();
	}

	@Override
	public E get(int index){
		if(index >= list.size()){
			return null;
		}
		return list.get(index);
	}

	@Override
	public E set(int index, E element){
		return list.set(index, element);
	}

	@Override
	public E remove(int index){
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o){
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o){
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator(){
		return list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index){
		return list.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex){
		return list.subList(fromIndex, toIndex);
	}

	public int maxSize(){
		return maxsize;
	}
	
}