package net.fexcraft.mod.lib.util.math;

import javax.annotation.Nullable;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * @author Ferdinand
 * Based off the Vanilla Vec3d;
 */
public class Vec3f {
	
    public static final Vec3f ZERO = new Vec3f(0.0F, 0.0F, 0.0F);
    public final float xCoord;
    public final float yCoord;
    public final float zCoord;

    public Vec3f(float x, float y, float z){
        if(x == -0.0F){
            x = 0.0F;
        }
        if(y == -0.0F){
            y = 0.0F;
        }
        if(z == -0.0F){
            z = 0.0F;
        }
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public Vec3f(Vec3i vector){
        this(vector.getX(), vector.getY(), vector.getZ());
    }
    
    public Vec3f(Vec3d vector){
    	this(vector.x, vector.y, vector.z);
    }
    
    public Vec3f(double xVec, double yVec, double zVec) {
		this((float)xVec, (float)yVec, (float)zVec);
	}

	public Vec3f subtractReverse(Vec3f vec){
        return new Vec3f(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }
    
    public Vec3f normalize(){
        float d0 = MathHelper.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return d0 < 1.0E-4D ? ZERO : new Vec3f(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
    }

    public float dotProduct(Vec3f vec){
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }
    
    public Vec3f crossProduct(Vec3f vec){
        return new Vec3f(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public Vec3f subtract(Vec3f vec){
        return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Vec3f subtract(float x, float y, float z){
        return this.addVector(-x, -y, -z);
    }

    public Vec3f add(Vec3f vec){
        return this.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
    }
    
    public Vec3f addVector(float x, float y, float z){
        return new Vec3f(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }
    
    public float distanceTo(Vec3f vec){
        float d0 = vec.xCoord - this.xCoord;
        float d1 = vec.yCoord - this.yCoord;
        float d2 = vec.zCoord - this.zCoord;
        return MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }
    
    public float squareDistanceTo(Vec3f vec){
        float d0 = vec.xCoord - this.xCoord;
        float d1 = vec.yCoord - this.yCoord;
        float d2 = vec.zCoord - this.zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public float squareDistanceTo(float xIn, float yIn, float zIn){
        float d0 = xIn - this.xCoord;
        float d1 = yIn - this.yCoord;
        float d2 = zIn - this.zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public Vec3f scale(float scale){
        return new Vec3f(this.xCoord * scale, this.yCoord * scale, this.zCoord * scale);
    }
    
    public float lengthVector(){
        return MathHelper.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public float lengthSquared(){
        return this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord;
    }
    
    @Nullable
    public Vec3f getIntermediateWithXValue(Vec3f vec, float x){
        float d0 = vec.xCoord - this.xCoord;
        float d1 = vec.yCoord - this.yCoord;
        float d2 = vec.zCoord - this.zCoord;
        if(d0 * d0 < 1.0000000116860974E-7D){
            return null;
        }
        else{
            float d3 = (x - this.xCoord) / d0;
            return d3 >= 0.0F && d3 <= 1.0F ? new Vec3f(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
        }
    }
    
    @Nullable
    public Vec3f getIntermediateWithYValue(Vec3f vec, float y){
        float d0 = vec.xCoord - this.xCoord;
        float d1 = vec.yCoord - this.yCoord;
        float d2 = vec.zCoord - this.zCoord;
        if(d1 * d1 < 1.0000000116860974E-7D){
            return null;
        }
        else{
            float d3 = (y - this.yCoord) / d1;
            return d3 >= 0.0F && d3 <= 1.0F ? new Vec3f(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
        }
    }

    /**
     * Returns a new vector with z value equal to the second parameter, along the line between this vector and the
     * passed in vector, or null if not possible.
     */
    @Nullable
    public Vec3f getIntermediateWithZValue(Vec3f vec, float z){
        float d0 = vec.xCoord - this.xCoord;
        float d1 = vec.yCoord - this.yCoord;
        float d2 = vec.zCoord - this.zCoord;
        if(d2 * d2 < 1.0000000116860974E-7D){
            return null;
        }
        else{
            float d3 = (z - this.zCoord) / d2;
            return d3 >= 0.0F && d3 <= 1.0F ? new Vec3f(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
        }
    }

    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        else if(!(obj instanceof Vec3f)){
            return false;
        }
        else{
            Vec3f vec3d = (Vec3f)obj;
            return Float.compare(vec3d.xCoord, this.xCoord) != 0 ? false : (Float.compare(vec3d.yCoord, this.yCoord) != 0 ? false : Float.compare(vec3d.zCoord, this.zCoord) == 0);
        }
    }

    public int hashCode(){
        long j = Float.floatToIntBits(this.xCoord);
        int i = (int)(j ^ j >>> 32);
        j = Float.floatToIntBits(this.yCoord);
        i = 31 * i + (int)(j ^ j >>> 32);
        j = Float.floatToIntBits(this.zCoord);
        i = 31 * i + (int)(j ^ j >>> 32);
        return i;
    }

    public String toString(){
        return "[" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + "]";
    }

    public Vec3f rotatePitch(float pitch){
        float f = MathHelper.cos(pitch);
        float f1 = MathHelper.sin(pitch);
        float d0 = this.xCoord;
        float d1 = this.yCoord * f + this.zCoord * f1;
        float d2 = this.zCoord * f - this.yCoord * f1;
        return new Vec3f(d0, d1, d2);
    }

    public Vec3f rotateYaw(float yaw){
        float f = MathHelper.cos(yaw);
        float f1 = MathHelper.sin(yaw);
        float d0 = this.xCoord * f + this.zCoord * f1;
        float d1 = this.yCoord;
        float d2 = this.zCoord * f - this.xCoord * f1;
        return new Vec3f(d0, d1, d2);
    }
    
    public static Vec3f fromPitchYawVector(Vec2f vec){
        return fromPitchYaw(vec.x, vec.y);
    }
    
    public static Vec3f fromPitchYaw(float fl, float fo){
        float f = MathHelper.cos((float)(-fo * 0.017453292F - Math.PI));
        float f1 = MathHelper.sin((float)(-fo * 0.017453292F - Math.PI));
        float f2 = -MathHelper.cos(-fl * 0.017453292F);
        float f3 = MathHelper.sin(-fl * 0.017453292F);
        return new Vec3f((f1 * f2), f3, (f * f2));
    }
    
    public Vec3d toDouble(){
    	return new Vec3d(xCoord, yCoord, zCoord);
    }
    
}