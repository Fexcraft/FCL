package net.fexcraft.lib.frl.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class ValueMap extends HashMap<String, Object> {

	public boolean has(String key){
		return containsKey(key);
	}

	public <T> void addArray(String key, Class<T> clazz){
		put(key, new ArrayList<T>());
	}

	public <T> List<T> getArray(String key, int size, T def){
		if(!containsKey(key)){
			List<T> list = new ArrayList<>();
			for(int i = 0; i < size; i++) list.add(def);
			put(key, list);
		}
		return (List<T>)get(key);
	}

	public <T> List<T> getArray(String key){
		return (List<T>)get(key);
	}

	public <T> List<T> getArray(String key, Class<T> clazz){
		return (List<T>)get(key);
	}
	
	public <T> T getValue(String key){
		return (T)get(key);
	}
	
	public <T> T getValue(String key, T def){
		return containsKey(key) ? (T)get(key) : def;
	}

}
