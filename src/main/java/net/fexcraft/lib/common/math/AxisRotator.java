package net.fexcraft.lib.common.math;

public interface AxisRotator {
	
	public static Class<? extends AxisRotator> DEF_IMPL = Axis3DL.class;

	public void setAngles(float x, float y, float z);

	public Vec3f getRelativeVector(Vec3f vector);

	public static AxisRotator newDefInstance(){
		try{
			return DEF_IMPL.newInstance();
		}
		catch(InstantiationException | IllegalAccessException e){
			e.printStackTrace();
			return null;
		}
	}

}
