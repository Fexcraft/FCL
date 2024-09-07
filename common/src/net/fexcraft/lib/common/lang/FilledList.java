package net.fexcraft.lib.common.lang;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * List that is always filled. Ignores `add` methods, use only `set` methods!
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class FilledList<E> implements List<E> {

	private Supplier<E> supplier;
	private Function<E, Boolean> empty;
	private Object[] array;

	public FilledList(int size, Supplier<E> supply, Function<E, Boolean> isempty){
		supplier = supply;
		array = new Object[size];
		empty = isempty;
	}

	@Override
	public int size(){
		return array.length;
	}

	@Override
	public boolean isEmpty(){
		for(Object e : array){
			if(!empty.apply((E)e)) return false;
		}
		return true;
	}

	@Override
	public boolean contains(Object o){
		for(Object e : array){
			if(e.equals(o)) return true;
		}
		return false;
	}

	@Override
	public Iterator<E> iterator(){
		return null;
	}

	@Override
	public Object[] toArray(){
		return Arrays.copyOf(array, array.length);
	}

	@Override
	public <T> T[] toArray(T[] a){
		return (T[])toArray();
	}

	@Override
	public boolean add(E e){
		return false;
	}

	@Override
	public boolean remove(Object o){
		for(int i = 0; i < array.length; i++){
			if(empty.apply((E)array[i])) continue;;
			if(array[i].equals(o)){
				array[i] = supplier.get();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c){
		for(Object o : c) if(!contains(o)) return false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c){
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c){
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c){
		for(Object o : c) remove(o);
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c){
		return false;
	}

	@Override
	public void clear(){
		for(int i = 0; i < array.length; i++){
			array[i] = supplier.get();
		}
	}

	@Override
	public E get(int index){
		return (E)array[index];
	}

	@Override
	public E set(int index, E element){
		Object prev = array[index];
		array[index] = element;
		return (E)prev;
	}

	@Override
	public void add(int index, E element){
		set(index, element);
	}

	@Override
	public E remove(int index){
		Object prev = array[index];
		array[index] = supplier.get();
		return (E)prev;
	}

	@Override
	public int indexOf(Object o){
		for(int i = 0; i < array.length; i++){
			if(empty.apply((E)array[i])) continue;
			if(array[i].equals(o)) return i;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o){
		int idx = -1;
		for(int i = 0; i < array.length; i++){
			if(empty.apply((E)array[i])) continue;
			if(array[i].equals(o)) idx = i;
		}
		return idx;
	}

	@Override
	public ListIterator<E> listIterator(){
		return new FLIterator<>(this, 0);
	}

	@Override
	public ListIterator<E> listIterator(int index){
		return new FLIterator<>(this, index);
	}

	@Override
	public List<E> subList(int from, int to){
		ArrayList<E> list = new ArrayList<>();
		for(int i = from; i < to && i < array.length; i++){
			list.add((E)array[i]);
		}
		return list;
	}

	private static class FLIterator<E> implements ListIterator<E> {

		private FilledList<E> list;
		private int index;

		public FLIterator(FilledList<E> filled, int startat){
			list = filled;
			index = startat;
		}

		@Override
		public boolean hasNext(){
			return index < list.size();
		}

		@Override
		public E next(){
			return list.get(index);
		}

		@Override
		public boolean hasPrevious(){
			return index > 0;
		}

		@Override
		public E previous(){
			return list.get(index - 1);
		}

		@Override
		public int nextIndex(){
			return index + 1;
		}

		@Override
		public int previousIndex(){
			return index - 1;
		}

		@Override
		public void remove(){
			list.remove(index);
		}

		@Override
		public void set(E e){
			list.set(index, e);
		}

		@Override
		public void add(E e){
			//
		}

	}

}
