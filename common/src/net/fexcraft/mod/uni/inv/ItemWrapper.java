package net.fexcraft.mod.uni.inv;

import java.util.LinkedHashMap;
import java.util.function.Function;

public abstract class ItemWrapper {

	public static Function<String, Object> GETTER;
	public static Function<Object, ItemWrapper> SUPPLIER = null;
	public static LinkedHashMap<String, ItemWrapper> WRAPPERS = new LinkedHashMap<>();

	public static ItemWrapper get(String id){
		if(WRAPPERS.containsKey(id)) return WRAPPERS.get(id);
		Object obj = GETTER.apply(id);
		if(obj == null) return null;
		ItemWrapper wrapper = SUPPLIER.apply(obj);
		WRAPPERS.put(id, wrapper);
		return wrapper;
	}

	public void linkContainer(){}

	public void regToDict(){}

	public abstract <LI> LI local();

	public abstract Object direct();

	public static ItemWrapper wrap(Object obj){
		return SUPPLIER.apply(obj);
	}

}
