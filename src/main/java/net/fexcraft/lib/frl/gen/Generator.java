package net.fexcraft.lib.frl.gen;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.frl.Polyhedron;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 * @param <GLO>
 */
public abstract class Generator<GLO> {
	
	protected Polyhedron<GLO> poly;
	protected JsonMap map;
	
	public Generator(Polyhedron<GLO> poly){
		this.poly = poly;
	}

	public Polyhedron<GLO> get(){
		return poly;
	}
	
	public abstract Polyhedron<GLO> make();
	
	public Generator<GLO> removePolygon(int index){
		if(!map.has("rem_poly")) map.addArray("rem_poly");
		map.getArray("rem_poly").add(index);
		return this;
	}
	
	public Generator<GLO> removePolygon(int... idxs){
		for(int i : idxs) removePolygon(i);
		return this;
	}

	public Generator<GLO> setMap(JsonMap map){
		this.map = map;
		return this;
	}

	public JsonMap getMap(){
		return map;
	}

}
