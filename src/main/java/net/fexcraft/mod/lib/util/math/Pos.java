package net.fexcraft.mod.lib.util.math;

import org.lwjgl.opengl.GL11;

import com.google.gson.JsonObject;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public class Pos {

    public float x, y, z;

    public Pos(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Pos(float[] pos){
        try{
            x = pos[0];
            y = pos[1];
            z = pos[2];
        }
        catch(Exception e){
            x = y = z = 0;
        }
    }

    public Pos copy(){
        return new Pos(x, y, z);
    }

    public Pos clone(){
        return copy();
    }

    @Override
    public String toString(){
        return "[" + x + "," + y + "," + z + "]";
    }

    public float to16FloatX(){
        return 0.0625f * x;
    }

    public float to16FloatY(){
        return 0.0625f * y;
    }

    public float to16FloatZ(){
        return 0.0625f * z;
    }

    public static Pos fromJSON(JsonObject obj){
        float x = obj.has("x") ? obj.get("x").getAsFloat() : 0f;
        float y = obj.has("y") ? obj.get("y").getAsFloat() : 0f;
        float z = obj.has("z") ? obj.get("z").getAsFloat() : 0f;
        return new Pos(x, y, z);
    }

    public JsonObject toJSON(){
        JsonObject obj = new JsonObject();
        obj.addProperty("x", x);
        obj.addProperty("y", y);
        obj.addProperty("z", z);
        return obj;
    }

    public static Pos fromNBT(String prefix, NBTTagCompound compound){
        String str = prefix == null ? "" : prefix + "_";
        return new Pos(compound.getFloat(str + "x"), compound.getFloat(str + "y"), compound.getFloat(str + "z"));
    }

    public NBTTagCompound toNBT(String prefix, NBTTagCompound compound){
        String str = prefix == null ? "" : prefix + "_";
        if(compound == null){
            compound = new NBTTagCompound();
        }
        compound.setFloat(str + "x", x);
        compound.setFloat(str + "y", y);
        compound.setFloat(str + "z", z);
        return compound;
    }

    public void translate(){
        GL11.glTranslatef(x == 0 ? 0 : this.to16FloatX(), y == 0 ? 0 : this.to16FloatY(), z == 0 ? 0 : this.to16FloatZ());
    }

    public void translateR(){
        GL11.glTranslatef(x == 0 ? 0 : -this.to16FloatX(), y == 0 ? 0 : -this.to16FloatY(), z == 0 ? 0 : -this.to16FloatZ());
    }

    public Vec3d to16Double(){
        return new Vec3d(this.to16FloatX(), this.to16FloatY(), this.to16FloatZ());
    }

	public Vec3f to16Float(){
		return new Vec3f(this.to16FloatX(), this.to16FloatY(), this.to16FloatZ());
	}

}
