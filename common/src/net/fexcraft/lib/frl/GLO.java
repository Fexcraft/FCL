package net.fexcraft.lib.frl;

import java.util.function.Supplier;

public class GLO<SELF extends GLO> {

	public static Supplier<GLO> SUPPLIER = null;
	public Material material = Material.NONE;

	public void copy(SELF from, boolean full){
		material = from.material;
	}

}
