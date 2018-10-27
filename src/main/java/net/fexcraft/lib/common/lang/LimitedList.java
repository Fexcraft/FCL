package net.fexcraft.lib.common.lang;

import java.util.ArrayList;
import java.util.Collection;
import net.fexcraft.lib.common.utils.Print;

/**
 * @author Ferdinand Calo' (FEX___96)
 *
 * General List with index size limit.
 * @param <E>
 */
public class LimitedList<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_SIZE_LIMIT = 10;
	private int maxsize;
	
	public LimitedList(){
		this(DEFAULT_SIZE_LIMIT);
	}
	
	public LimitedList(int size){
		maxsize = size;
	}
	
	@Override
	public boolean add(E object){
		return (size() + 1 > maxsize) ? Print.bool(false, "Array limit reached.") : add(object);
	}

	@Override
	public void add(int index, E element){
		if(size() + 1 > maxsize){
			Print.console("Array limit reached."); return;
		} else add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> coll){
		return (size() + coll.size() > maxsize) ? Print.bool(false, "Array limit reached.") : addAll(coll);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> coll){
		return (size() + coll.size() > maxsize) ? Print.bool(false, "Array limit reached.") : addAll(index, coll);
	}

	public int maxSize(){
		return maxsize;
	}
	
}