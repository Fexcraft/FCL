package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.tag.TagType;
import net.minecraft.nbt.*;

import java.util.Collection;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TagCWI implements TagCW {

	private CompoundTag compound;

	public TagCWI(){
		compound = new CompoundTag();
	}

	public TagCWI(CompoundTag com){
		compound = com == null ? new CompoundTag() : com;
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
		return compound.getInt(key);
	}

	@Override
	public long getLong(String key){
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
		return new TagCWI(compound.getCompound(key));
	}

	@Override
	public TagLW getList(String key){
		return new TagLWI((ListTag)compound.get(key));
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
		return compound.contains(key);
	}

	@Override
	public void set(String key, String val){
		compound.putString(key, val);
	}

	@Override
	public void set(String key, float val){
		compound.putFloat(key, val);
	}

	@Override
	public void set(String key, double val){
		compound.putDouble(key, val);
	}

	@Override
	public void set(String key, int val){
		compound.putInt(key, val);
	}

	@Override
	public void set(String key, long val){
		compound.putLong(key, val);
	}

	@Override
	public void set(String key, boolean val){
		compound.putBoolean(key, val);
	}

	@Override
	public void set(String key, TagCW val){
		compound.put(key, val.local());
	}

	@Override
	public void set(String key, TagLW val){
		compound.put(key, val.local());
	}

	@Override
	public void set(String key, int[] val){
		compound.putIntArray(key, val);
	}

	@Override
	public void set(String key, byte[] val){
		compound.putByteArray(key, val);
	}

	@Override
	public int size(){
		return compound.size();
	}

	@Override
	public <T> T local(){
		return (T)compound;
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
		return compound.getAllKeys();
	}

	@Override
	public TagCW copy(){
		return new TagCWI(compound.copy());
	}

	@Override
	public void rem(String key){
		compound.remove(key);
	}

	@Override
	public TagType getType(String key){
		Tag base = compound.get(key);
		if(base instanceof CompoundTag) return TagType.COMPOUND;
		if(base instanceof ListTag) return TagType.LIST;
		if(base instanceof StringTag) return TagType.STRING;
		if(base instanceof LongTag) return TagType.LONG;
		if(base instanceof IntTag) return TagType.INT;
		if(base instanceof IntArrayTag) return TagType.INT_ARRAY;
		if(base instanceof ShortTag) return TagType.SHORT;
		if(base instanceof ByteTag) return TagType.BYTE;
		if(base instanceof FloatTag) return TagType.FLOAT;
		if(base instanceof DoubleTag) return TagType.DOUBLE;
		return TagType.UNKNOWN;
	}

	@Override
	public String toString(){
		return compound.toString();
	}

}