package net.fexcraft.lib.frl;

import java.util.function.Supplier;

public class GLO {

	public static Supplier<GLO> SUPPLIER = GLObject::new;
	public Material material = Material.NONE;

	public void copy(GLO from, boolean full){
		material = from.material;
	}

}
