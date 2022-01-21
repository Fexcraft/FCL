package net.fexcraft.lib.frl.gen;

import java.util.ArrayList;

import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.frl.Polyhedron;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 * @param <GLO>
 */
public abstract class Generator<GLO> {

	protected static final Vec3f NULL_VEC = new Vec3f(0, 0, 0);
	
	protected Polyhedron<GLO> poly;
	protected ValueMap map = new ValueMap();
	
	public Generator(Polyhedron<GLO> poly, float texW, float texH){
		this.poly = poly;
		map.put("texture_width", texW);
		map.put("texture_height", texH);
	}

	public Polyhedron<GLO> get(){
		return poly;
	}
	
	public Polyhedron<GLO> make(){
		Type type = map.getValue("type", Type.CUBOID);
		switch(type){
			case CUBOID:
			default:{
				Generator_Cuboid.make(poly, map);
				break;
			}
		}
		return poly;
	}
	
	public void setValue(String key, Object value){
		map.put(key, value);
	}
	
	public Generator<GLO> removePolygon(int index){
		if(!map.has("rem_poly")) map.addArray("rem_poly", int.class);
		map.getArray("rem_poly").add(index);
		return this;
	}
	
	public Generator<GLO> removePolygon(int... idxs){
		for(int i : idxs) removePolygon(i);
		return this;
	}

	public ValueMap getMap(){
		return map;
	}
	
	public static enum Type {
		
		CUBOID
		
	}

	protected static boolean[] intToBoolArray(ArrayList<Boolean> array){
		boolean[] bool = new boolean[6];
		for(int i = 0; i < 6; i++){
			if(array.get(i) == true) bool[i] = true;
		}
		return bool;
	}

}
