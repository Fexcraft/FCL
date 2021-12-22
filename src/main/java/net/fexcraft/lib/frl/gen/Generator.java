package net.fexcraft.lib.frl.gen;

import net.fexcraft.lib.frl.Polyhedron;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 * @param <GLO>
 */
public interface Generator<GLO> {

	public Polyhedron<GLO> get();
	
	public Polyhedron<GLO> make();
	
	public Generator<GLO> removePolygon(int index);

}
