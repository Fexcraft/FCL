package net.fexcraft.lib.frl;

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

}
