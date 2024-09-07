package net.fexcraft.mod.uni.ui;

import java.util.ArrayList;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIKey {

	public static final ArrayList<UIKey> ALLKEYS = new ArrayList<>();

	public final int id;
	public final String key;

	public UIKey(int id12, String id20){
		id = id12;
		key = id20;
		ALLKEYS.add(this);
	}

	public static int get(String id){
		for(UIKey key : ALLKEYS){
			if(key.key.equals(id)) return key.id;
		}
		return 0;
	}

	public static UIKey find(String id){
		for(UIKey key : ALLKEYS){
			if(key.key.equals(id)) return key;
		}
		return null;
	}

	public static UIKey find(String mid, int id){
		for(UIKey key : ALLKEYS){
			if(key.key.startsWith(mid) && key.id == id) return key;
		}
		return null;
	}

	public static UIKey byId(int ui){
		for(UIKey key : ALLKEYS){
			if(key.id == ui) return key;
		}
		return null;
	}

	@Override
	public String toString(){
		return key + "/" + id;
	}

}
