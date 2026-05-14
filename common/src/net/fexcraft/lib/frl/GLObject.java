package net.fexcraft.lib.frl;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class GLObject extends GLO {

	public boolean textured = true;

	@Override
	public void copy(GLO from, boolean full){
		super.copy(from, full);
		textured = ((GLObject)from).textured;
	}

}
