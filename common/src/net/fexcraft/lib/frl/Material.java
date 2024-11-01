package net.fexcraft.lib.frl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class Material {

	public static LinkedHashMap<String, Material> REGISTRY = new LinkedHashMap<>();
	public static Class<? extends Material> IMPL = Material.class;
	public static final Material NONE = new Material("fcl:none");
	static{
		REGISTRY.put(NONE.id, NONE);
	}
	public final String id;
	public Object texture;

	public Material(String uid){
		id = uid;
	}

	public static Material get(String id, boolean create){
		if(REGISTRY.containsKey(id)) return REGISTRY.get(id);
		if(create){
			try{
				Material mat = IMPL.getConstructor(String.class).newInstance(id);
				REGISTRY.put(mat.id, mat);
				return mat;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return NONE;
	}

	public boolean none(){
		return this == NONE;
	}

	public void applyTexture(){}

}
