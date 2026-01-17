package net.fexcraft.lib.frl.gen;

import java.util.List;

import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.frl.GLO;
import net.fexcraft.lib.frl.Polyhedron;

import static net.fexcraft.lib.frl.gen.Generator.Values.*;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 * @param <GL>
 */
public class Generator<GL extends GLO> {

	protected static final Vec3f NULL_VEC = new Vec3f(0, 0, 0);
	
	protected Polyhedron<GL> poly;
	protected ValueMap map = new ValueMap();
	
	public Generator(Polyhedron<GL> poli){
		if(poli != null) poly = poli;
		else poly = new Polyhedron<>();
	}
	
	public Generator(Polyhedron<GL> poli, float texW, float texH){
		this(poli);
		map.put(TEXTURE_WIDTH, texW);
		map.put(TEXTURE_HEIGHT, texH);
	}
	
	public Generator(Polyhedron<GL> poli, float texW, float texH, Type type){
		this(poli, texW, texH);
		map.put(TYPE, type);
	}
	
	public Generator(Polyhedron<GL> poli, Type type){
		this(poli, 0, 0, type);
	}

	public Polyhedron<GL> get(){
		return poly;
	}
	
	public Polyhedron<GL> make(){
		Type type = map.getValue(TYPE, Type.NONE);
		switch(type){
			case CYLINDER:{
				Generator_Cylinder.make(poly, map);
				break;
			}
			case CUBOID:{
				Generator_Cuboid.make(poly, map);
				break;
			}
			default: break;
		}
		return poly;
	}
	
	public Generator<GL> setValue(Enum<?> key, Object value){
		map.put(key, value);
		return this;
	}
	
	public Generator<GL> set(Enum<?> key, Object value){
		map.put(key, value);
		return this;
	}
	
	public Generator<GL> removePolygon(int index){
		if(!map.has(REMOVE_POLYGONS)) map.addArray(REMOVE_POLYGONS, int.class);
		map.getArray(REMOVE_POLYGONS).add(index);
		return this;
	}
	
	public Generator<GL> removePolygon(int... idxs){
		for(int i : idxs) removePolygon(i);
		return this;
	}

	public ValueMap getMap(){
		return map;
	}
	
	public static enum Type {
		
		NONE, CUBOID, CYLINDER
		
	}

	protected static boolean[] intToBoolArray(List<Integer> array, int size){
		boolean[] bool = new boolean[size];
		if(array == null || array.size() == 0) return bool;
		for(int i = 0; i < array.size(); i++){
			int j = array.get(i);
			if(j >= 0 && j < bool.length) bool[j] = true;
		}
		return bool;
	}

	protected static boolean detached(boolean[] rems, boolean[] deuv, int i){
		return rems[i] || deuv[i];
	}

	public static enum Values {
		OFF_X, OFF_Y, OFF_Z, TYPE,
		REMOVE_POLYGONS, DETACHED_UV, UV,
		TEXTURE_WIDTH, TEXTURE_HEIGHT,
		EXPANSION, SCALE, ORDERED,

		WIDTH, HEIGHT, DEPTH, CENTERED, CORNERS,

		LENGTH, RADIUS1, RADIUS2, RADIUS3, RADIUS4,
		AXIS_DIR, SEGMENTS, SEG_LIMIT, SEG_OFFSET, TOP_SCALE, BASE_SCALE,
		RADIAL, SEG_WIDTH, SEG_HEIGHT, TOP_OFFSET, TOP_ROTATION,
	}

}
