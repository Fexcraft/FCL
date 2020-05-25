package net.fexcraft.lib.common.lang;

import java.util.List;

import javax.annotation.Nullable;;

/**
 * @author Ferdinand Calo' (FEX___96)
 * 
 * "No Out Of Bounds" ArrayList
 * General ArrayList which will not cause "out of bounds" exceptions, but instead return null or first entry.
 * @param <T>
 */
public class NOOBArrayList<T> extends java.util.ArrayList<T> {
	
	private static final long serialVersionUID = 6551014441630799597L;

	public NOOBArrayList(List<T> list){
		super(list);
	}

	public NOOBArrayList(){
		super();
	}

	public NOOBArrayList(T[] arr){
		super();
		for(T e : arr){
			this.add(e);
		}
	}

	@Override
	@Nullable
	public T get(int i){
		return this.isEmpty() ? null : i > this.size() ? super.get(0) : i < 0 ? null : super.get(i);
	}
	
}