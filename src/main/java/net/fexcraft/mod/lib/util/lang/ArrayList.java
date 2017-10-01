package net.fexcraft.mod.lib.util.lang;

import java.util.List;

public class ArrayList<T> extends java.util.ArrayList<T> {
	
	private static final long serialVersionUID = 6551014441630799597L;

	public ArrayList(List<T> list){
		super(list);
	}

	public ArrayList(){
		super();
	}

	@Override
	public T get(int i){
		if(i > this.size()){
			return super.get(0);
		}
		return i < 0 ? null : super.get(i);
	}
	
}