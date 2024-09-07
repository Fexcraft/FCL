package net.fexcraft.mod.uni.item;

import java.util.TreeMap;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.mod.uni.IDL;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface ClothMaterial {

	public static TreeMap<IDL, ClothMaterial> MATERIALS = new TreeMap<>();
	public static Manager[] MANAGER = new Manager[1];

	public static ClothMaterial create(IDL id, JsonMap map){
		return MANAGER[0].create(id, map);
	}

	public static ClothMaterial get(String str){
		return MANAGER[0].get(str);
	}

	public static ClothMaterial get(IDL id){
		return MANAGER[0].get(id);
	}

	public static interface Manager {

		public ClothMaterial create(IDL id, JsonMap map);

		public ClothMaterial get(String str);

		public ClothMaterial get(IDL id);

	}

	public Object local();

}
