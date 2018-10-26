package net.fexcraft.lib.common.lang;

import java.util.TreeMap;

/** 
 * @author Ferdinand Calo' (FEX___96)
 *
 * Generic (Tree)Map which will return a "default" value if key is not present.
 * @param <K>
 * @param <V>
 */
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