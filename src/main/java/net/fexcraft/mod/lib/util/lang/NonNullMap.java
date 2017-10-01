package net.fexcraft.mod.lib.util.lang;

import java.util.TreeMap;

public class NonNullMap<K, V> extends TreeMap<K, V>{
	
	private final V def;
	
	public NonNullMap(V value){
		this.def = value;
	}
	
	@Override
	public V get(Object k){
		return containsKey(k) ? super.get(k) : def;
	}
	
}