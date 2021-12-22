package net.fexcraft.lib.frl.gen;

import net.fexcraft.lib.frl.Polyhedron;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 * @param <GLO>
 */
public class Cuboid<GLO> implements Generator<GLO>{
	
	private Polyhedron<GLO> poly;

	public Cuboid(Polyhedron<GLO> poly){
		this.poly = poly;
	}

	@Override
	public Polyhedron<GLO> get(){
		return poly;
	}

	@Override
	public Cuboid<GLO> removePolygon(int index){
		//TODO
		return this;
	}

	@Override
	public Polyhedron<GLO> make(){
		//TODO
		return poly;
	}

}
