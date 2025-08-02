package net.fexcraft.mod.uni.impl;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.tag.TagType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.Collection;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TagCWVO implements TagCW {

	private ValueOutput out;

	public TagCWVO(ValueOutput output){
		out = output;
	}

	@Override
	public String getString(String key){
		return "";
	}

	@Override
	public float getFloat(String key){
		return 0;
	}

	@Override
	public double getDouble(String key){
		return 0;
	}

	@Override
	public int getInteger(String key){
		return 0;
	}

	@Override
	public long getLong(String key){
		return 0;
	}

	@Override
	public boolean getBoolean(String key){
		return false;
	}

	@Override
	public byte getByte(String key){
		return 0;
	}

	@Override
	public TagCW getCompound(String key){
		return null;
	}

	@Override
	public TagLW getList(String key){
		return null;
	}

	@Override
	public V3I getV3I(String key){
		return null;
	}

	@Override
	public int[] getIntArray(String key){
		return new int[0];
	}

	@Override
	public byte[] getByteArray(String key){
		return new byte[0];
	}

	@Override
	public boolean has(String key){
		return true;
	}

	@Override
	public void set(String key, String val){
		out.putString(key, val);
	}

	@Override
	public void set(String key, float val){
		out.putFloat(key, val);
	}

	@Override
	public void set(String key, double val){
		out.putDouble(key, val);
	}

	@Override
	public void set(String key, int val){
		out.putInt(key, val);
	}

	@Override
	public void set(String key, long val){
		out.putLong(key, val);
	}

	@Override
	public void set(String key, boolean val){
		out.putBoolean(key, val);
	}

	@Override
	public void set(String key, TagCW val){
		out.store(key, CompoundTag.CODEC, val.local());
	}

	@Override
	public void set(String key, TagLW val){

	}

	@Override
	public void set(String key, int[] val){
		out.putIntArray(key, val);
	}

	@Override
	public void set(String key, byte[] val){

	}

	@Override
	public void set(String key, V3I vec, boolean packed){
		set(key, vec.toIntegerArray());
	}

	@Override
	public int size(){
		return 0;
	}

	@Override
	public <T> T local(){
		return null;
	}

	@Override
	public Object direct(){
		return null;
	}

	@Override
	public boolean empty(){
		return false;
	}

	@Override
	public Collection<String> keys(){
		return List.of();
	}

	@Override
	public TagCW copy(){
		return null;
	}

	@Override
	public void rem(String key){
		out.discard(key);
	}

	@Override
	public TagType getType(String key){
		return null;
	}
	
}
