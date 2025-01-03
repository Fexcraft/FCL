package net.fexcraft.mod.uni.impl;

import java.util.ArrayList;
import java.util.Collection;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.tag.TagType;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TagCWI implements TagCW {

	private NBTTagCompound compound;

	public TagCWI(){
		compound = new NBTTagCompound();
	}

	public TagCWI(NBTTagCompound com){
		compound = com;
	}

	public TagCWI(Object com){
		if(com instanceof NBTTagCompound){
			compound = (NBTTagCompound)com;
		}
		else compound = new NBTTagCompound();
	}

	@Override
	public String getString(String key){
		return compound.getString(key);
	}

	@Override
	public float getFloat(String key){
		return compound.getFloat(key);
	}

	@Override
	public double getDouble(String key){
		return compound.getDouble(key);
	}

	@Override
	public int getInteger(String key){
		return compound.getInteger(key);
	}

	@Override
	public long getLong(String key) {
		return compound.getLong(key);
	}

	@Override
	public boolean getBoolean(String key){
		return compound.getBoolean(key);
	}

	@Override
	public byte getByte(String key){
		return compound.getByte(key);
	}

	@Override
	public TagCW getCompound(String key){
		return new TagCWI(compound.getCompoundTag(key));
	}

	@Override
	public TagLW getList(String key){
		return new TagLWI(compound.getTag(key));
	}

	@Override
	public V3I getV3I(String key){
		if(compound.getTag(key) instanceof NBTTagLong){
			BlockPos blk = BlockPos.fromLong(compound.getLong(key));
			return new V3I(blk.getX(), blk.getY(), blk.getZ());
		}
		else return new V3I(getIntArray(key), 0);
	}

	@Override
	public int[] getIntArray(String key){
		return compound.getIntArray(key);
	}

	@Override
	public byte[] getByteArray(String key){
		return compound.getByteArray(key);
	}

	@Override
	public boolean has(String key){
		return compound.hasKey(key);
	}

	@Override
	public void set(String key, String val){
		compound.setString(key, val);
	}

	@Override
	public void set(String key, float val){
		compound.setFloat(key, val);
	}

	@Override
	public void set(String key, double val){
		compound.setDouble(key, val);
	}

	@Override
	public void set(String key, int val){
		compound.setInteger(key, val);
	}

	@Override
	public void set(String key, long val){
		compound.setLong(key, val);
	}

	@Override
	public void set(String key, boolean val){
		compound.setBoolean(key, val);
	}

	@Override
	public void set(String key, TagCW val){
		compound.setTag(key, val.local());
	}

	@Override
	public void set(String key, TagLW val){
		compound.setTag(key, val.local());
	}

	@Override
	public void set(String key, int[] val){
		compound.setIntArray(key, val);
	}

	@Override
	public void set(String key, byte[] val){
		compound.setByteArray(key, val);
	}

	@Override
	public void set(String key, V3I vec, boolean packed){
		if(packed) set(key, new BlockPos(vec.x, vec.y, vec.z).toLong());
		else set(key, vec.toIntegerArray());
	}

	@Override
	public int size(){
		return compound.getSize();
	}

	@Override
	public <C> C local(){
		return (C)compound;
	}

	@Override
	public Object direct(){
		return compound;
	}

	@Override
	public boolean empty(){
		return compound.isEmpty();
	}

	@Override
	public Collection<String> keys(){
		return new ArrayList<>(compound.getKeySet());
	}

	@Override
	public TagCW copy(){
		return new TagCWI(compound.copy());
	}

	@Override
	public void rem(String key){
		compound.removeTag(key);
	}

	@Override
	public TagType getType(String key){
		NBTBase base = compound.getTag(key);
		if(base instanceof NBTTagCompound) return TagType.COMPOUND;
		if(base instanceof NBTTagList) return TagType.LIST;
		if(base instanceof NBTTagString) return TagType.STRING;
		if(base instanceof NBTTagLong) return TagType.LONG;
		if(base instanceof NBTTagInt) return TagType.INT;
		if(base instanceof NBTTagIntArray) return TagType.INT_ARRAY;
		if(base instanceof NBTTagShort) return TagType.SHORT;
		if(base instanceof NBTTagByte) return TagType.BYTE;
		if(base instanceof NBTTagFloat) return TagType.FLOAT;
		if(base instanceof NBTTagDouble) return TagType.DOUBLE;
		return TagType.UNKNOWN;
	}

	@Override
	public String toString(){
		return compound == null ? "null" : compound.toString();
	}

}
