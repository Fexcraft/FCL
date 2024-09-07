package net.fexcraft.mod.uni;

import net.fexcraft.mod.uni.impl.IDLM;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public interface IDLManager {
	
	public static IDLManager INSTANCE = new IDLM();

	public IDL get(String idl, boolean cache, boolean named);
	
	public static IDL getIDL(String idl, boolean cache, boolean named){
		return INSTANCE.get(idl, cache, named);
	}
	
	public static IDL getIDLCached(String idl, boolean named){
		return INSTANCE.get(idl, true, named);
	}
	
	public static IDL getIDLNamed(String idl, boolean cache){
		return INSTANCE.get(idl, cache, true);
	}
	
	/** Cached and unnamed. */
	public static IDL getIDLCached(String idl){
		return INSTANCE.get(idl, true, false);
	}

	/** Cached and named. */
	public static IDL getIDLNamed(String idl){
		return INSTANCE.get(idl, true, true);
	}

	/** Not cached and unnamed. */
	public static IDL getIDL(String idl){
		return INSTANCE.get(idl, false, false);
	}

}
