package net.fexcraft.lib.frl;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.mod.uni.IDL;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public abstract class Renderer<GL extends GLO> {
	
	@SuppressWarnings("rawtypes")
	public static Renderer RENDERER = new DefaultRenderer();
	
	public static boolean TRIANGULATED_QUADS = true;

	public abstract void render(Polyhedron<GL> poly);

	public abstract void delete(Polyhedron<GL> poly);

	public abstract void push();

	public abstract void pop();

	public void translate(V3D vec){
		translate(vec.x, vec.y, vec.z);
	}

	public abstract void translate(double x, double y, double z);

	public abstract void rotate(float deg, int x, int y, int z);

	public abstract void rotate(double deg, int x, int y, int z);

	public abstract void scale(double x, double y, double z);

	public abstract void bind(IDL tex);

	public abstract void color(int rgb);

	public abstract void light(V3D pos);

}
