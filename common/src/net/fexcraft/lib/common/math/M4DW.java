package net.fexcraft.lib.common.math;

import net.fexcraft.lib.frl.RotationOrder;

import java.util.function.Supplier;

import static java.lang.Math.toRadians;

/**
 * Matrix 4x4 Double Wrapper
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class M4DW {

	public static Supplier<M4DW> SUPPLIER = null;
	protected RotationOrder order = RotationOrder.YXZ;
	public double yaw, pit, rol;

	public static M4DW create(){
		return SUPPLIER.get();
	}

	/** Returns the 2nd vector filled with the rotation result. */
	public V3D rotate(V3D vec, V3D des){
		reset(vec);
		rotateYPR();
		return fill(des);
	}

	/** Returns a new vector with the rotation result. */
	public V3D rotate(V3D vec){
		return rotate(vec, new V3D());
	}

	/** Returns a new mutable vector with the rotation result.  */
	public V3D rotateM(V3D vec){
		return rotate(vec, new MV3D());
	}

	/** Returns the 2nd vector filled with the rotation result. */
	public V3D rotate(double x, double y, double z, V3D des){
		reset(x, y, z);
		rotateYPR();
		return fill(des);
	}

	/** Returns a new vector with the rotation result. */
	public V3D rotate(double x, double y, double z){
		return rotate(x, y, z, new V3D());
	}

	/** Returns a new mutable vector with the rotation result. */
	public V3D rotateM(double x, double y, double z){
		return rotate(x, y, z, new MV3D());
	}

	/** Fills the vector with the current matrix position state. */
	protected abstract V3D fill(V3D vec);

	/** Rotates the matrix in radians on the given axe index. */
	public abstract void rotate(double am, int axe);

	/** Rotates the matrix along the 3 axes by the current values. */
	private void rotateYPR(){
		if(rol != 0D) rotate(rol, order.axid[2]);
		if(pit != 0D) rotate(pit, order.axid[1]);
		if(yaw != 0D) rotate(yaw, order.axid[0]);
	}

	/** Sets the matrix rotation values and updates/normalizes the matrix.  */
	public void setRadians(double y, double p, double r){
		reset(1, 0, 0);
		yaw = y;
		pit = p;
		rol = r;
		rotateYPR();
		norm();
	}

	/** Sets the matrix rotation values and updates/normalizes the matrix. */
	public void setDegrees(double y, double p, double r){
		setRadians(toRadians(y), toRadians(p), toRadians(r));
	}

	/** Adds to the matrix rotation values and updates/normalizes the matrix.  */
	public void addRadians(double y, double p, double r){
		reset(1, 0, 0);
		yaw += y;
		pit += p;
		rol += r;
		rotateYPR();
		norm();
	}

	/** Adds to the matrix rotation values and updates/normalizes the matrix. */
	public void addDegrees(double y, double p, double r){
		addRadians(toRadians(y), toRadians(p), toRadians(r));
	}

	/** Resets the matrix and sets to this position. */
	protected void reset(V3D vec){
		reset(vec.x, vec.y, vec.z);
	}

	/** Resets the matrix and sets to this position. */
	protected abstract void reset(double x, double y, double z);

	/** Updates/normalizes the rotation values to current matrix state. */
	protected abstract void norm();

	@Override
	public String toString(){
		return String.format("M4DW[ %sy, %sp, %sr ]", yaw, pit, rol);
	}

	/** From FMT / Prototype */
	public void pointing(V3D fm, V3D to){
		double dx = to.x - fm.x, dy = to.y - fm.y, dz = to.z - fm.z;
		double dxz = Math.sqrt(dx * dx + dz * dz);
		yaw = (float)Math.atan2(dz, dx);
		pit = (float)-Math.atan2(dy, dxz);
		rol = 0;
		reset(1, 0, 0);
		rotateYPR();
		norm();
	}

}
