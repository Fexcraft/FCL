package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.IDLManager;

import java.util.HashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class IDLM implements IDLManager {

	public static HashMap<String, IDL> CACHE = new HashMap<>();

	public IDL get(String str, boolean cache, boolean named){
		IDL idl = CACHE.get(str);
		if(idl != null) return idl;
		idl = named ? new NaResLoc(str) : new ResLoc(str);
		if(cache) CACHE.put(str, idl);
		return idl;
	}

}