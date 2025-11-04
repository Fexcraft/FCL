package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

import java.util.HashMap;

public class IDLM implements IDLManager {

	public static HashMap<String, IDL> CACHE = new HashMap<>();

	@Override
	public IDL get(String str, boolean cache, boolean named){
		IDL idl = CACHE.get(str);
		if(idl != null) return idl;
		if(cache) CACHE.put(str, idl = named ? new NaResLoc(str) : new ResLoc(str));
		else return named ? new NaResLoc(str) : new ResLoc(str);
		return idl;
	}

}
