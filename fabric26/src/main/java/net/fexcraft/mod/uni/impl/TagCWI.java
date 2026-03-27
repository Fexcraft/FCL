package net.fexcraft.mod.uni.impl;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.tag.TagType;
import net.minecraft.core.BlockPos;
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
		return compound.getStringOr(key, "");
	}

	@Override
	public float getFloat(String key){
		return compound.getFloatOr(key, 0f);
	}

	@Override
	public double getDouble(String key){
		return compound.getDoubleOr(key, 0d);
	}

	@Override
	public int getInteger(String key){
		return compound.getIntOr(key, 0);
	}

	@Override
	public long getLong(String key){
		return compound.getLongOr(key, 0l);
	}

	@Override
	public boolean getBoolean(String key){
		return compound.getBooleanOr(key, false);
	}

	@Override
	public byte getByte(String key){
		return compound.getByteOr(key, (byte)0);
	}

	@Override
	public TagCW getCompound(String key){
		return new TagCWI(compound.getCompoundOrEmpty(key));
	}

	@Override
	public TagLW getList(String key){
		return new TagLWI(compound.getListOrEmpty(key));
	}

	@Override
	public V3I getV3I(String key){
		if(compound.get(key) instanceof LongTag){
			BlockPos blk = BlockPos.of(compound.getLongOr(key, 0l));
			return new V3I(blk.getX(), blk.getY(), blk.getZ());
		}
		else return new V3I(getIntArray(key), 0);
	}

	@Override
	public int[] getIntArray(String key){
		return compound.getIntArray(key).orElse(new int[0]);
	}

	@Override
	public byte[] getByteArray(String key){
		return compound.getByteArray(key).orElse(new byte[0]);
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
	public void set(String key, V3I vec, boolean packed){
		if(packed) set(key, new BlockPos(vec.x, vec.y, vec.z).asLong());
		else set(key, vec.toIntegerArray());
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
		return compound.keySet();
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