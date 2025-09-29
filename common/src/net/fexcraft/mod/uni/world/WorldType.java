package net.fexcraft.mod.uni.world;

import net.fexcraft.mod.uni.EnvInfo;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class WorldType {

	protected final Object key;
	protected final String skey;
	protected final boolean client;

	public WorldType(Object lkey, boolean remote){
		key = lkey;
		client = remote;
		skey = key + (client ? "c" : "s");
	}

	public WorldType(String skey){
		String rkey = skey.substring(0, skey.length() - 1);
		key = EnvInfo.is112() ? Integer.parseInt(rkey) : rkey;
		client = skey.endsWith("c");
		this.skey = skey;
	}

	public Object key(){
		return key;
	}

	public int int_key(){
		return (int)key;
	}

	public String side_key(){
		return skey;
	}

	public boolean client(){
		return client;
	}

	public boolean server(){
		return !client;
	}

}
