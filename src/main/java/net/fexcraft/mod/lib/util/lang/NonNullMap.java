package net.fexcraft.mod.lib.util.lang;

import java.util.TreeMap;

public class NonNullMap<K, V> extends TreeMap<K, V>{
	
	private static final long serialVersionUID = -6904819501960267639L;
	private final V def;
	
	public NonNullMap(V value){
		this.def = value;
	}
	
	@Override
	public V get(Object k){
		return containsKey(k) ? super.get(k) : def;
	}
	
}