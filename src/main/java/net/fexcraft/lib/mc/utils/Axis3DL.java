package net.fexcraft.lib.mc.utils;

import net.fexcraft.lib.common.math.AxisRotator;
import net.fexcraft.lib.common.math.Vec3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/** Taken from FVTM and adjusted/shortened. "Lite" Version. */
public class Axis3DL implements AxisRotator {
	
	private Matrix4f matrix;
	private float yaw, pitch, roll;

    public Axis3DL(){ matrix = new Matrix4f(); }
    
    @Override public String toString(){ return "[ " + yaw + "y, " + pitch + "p, " + roll + "r ]";  }
    
    public Vec3f getRelativeVector(Vec3f vec){
        Matrix4f mat = new Matrix4f();
        mat.m00 = vec.x; mat.m10 = (float)vec.y; mat.m20 = (float)vec.z;
        Matrix4f.rotate(roll  * 3.14159265F / 180, new Vector3f(1F, 0F, 0F), mat, mat);
        Matrix4f.rotate(pitch * 3.14159265F / 180, new Vector3f(0F, 0F, 1F), mat, mat);
        Matrix4f.rotate(yaw   * 3.14159265F / 180, new Vector3f(0F, 1F, 0F), mat, mat);
        return new Vec3f(mat.m00, mat.m10, mat.m20);
    }

    private final void convertMatrixToAngles(){
        yaw = (float)Math.atan2(matrix.m20, matrix.m00) * 180F / 3.14159265F;
        pitch = (float)Math.atan2(-matrix.m10, Math.sqrt(matrix.m12 * matrix.m12 + matrix.m11 * matrix.m11)) * 180F / 3.14159265F;
        roll = (float)Math.atan2(matrix.m12, matrix.m11) * 180F / 3.14159265F;
    }

    private final void convertToMatrix(boolean rad){
        matrix = new Matrix4f();
        matrix.rotate(rad ? roll : roll * 3.14159265F / 180F, new Vector3f(1F, 0F, 0F));
        matrix.rotate(rad ? pitch : pitch * 3.14159265F / 180F, new Vector3f(0F, 0F, 1F));
        matrix.rotate(rad ? yaw : yaw * 3.14159265F / 180F, new Vector3f(0F, 1F, 0F));
        convertMatrixToAngles();
    }

    public void setAngles(float yaw, float pitch, float roll){
        this.yaw = yaw; this.pitch = pitch; this.roll = roll; convertToMatrix(false);
    }

}
