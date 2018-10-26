package net.fexcraft.lib.common.lang;

import java.util.ArrayList;

/**
 * @author Ferdinand Calo' (FEX___96)
 * 
 * Generic Sortable List
 * @param <E>
 */
@Deprecated
public class SortableList<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = 1L;

	@Override
	public E get(int index){
		return index >= size() ? null : get(index);
	}
	
	public void putAt(int i, int j){
		E elm = set(i, get(j)); set(j, elm);
	}
	
}