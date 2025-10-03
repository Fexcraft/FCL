package net.fexcraft.mod.uni.world;

import net.fexcraft.mod.uni.EnvInfo;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class WorldType {

	protected final Object key;
	protected final String skey;
	protected final String okey;
	protected final boolean client;

	public WorldType(Object lkey, boolean remote){
		key = lkey;
		client = remote;
		skey = key + (client ? "-c" : "-s");
		okey = key + (client ? "-s" : "-c");
	}

	public WorldType(String sidekey){
		String rkey = sidekey.substring(0, sidekey.length() - 2);
		key = EnvInfo.is112() ? Integer.parseInt(rkey) : rkey;
		client = sidekey.endsWith("-c");
		skey = sidekey;
		okey = key + (client ? "-s" : "-c");
	}

	public Object key(){
		return key;
	}

	public int int_key(){
		return (int)key;
	}

	/** Local side key. */
	public String side_key(){
		return skey;
	}

	/** Receiver side key. */
	public String rec_key(){
		return okey;
	}

	public boolean client(){
		return client;
	}

	public boolean server(){
		return !client;
	}

}
