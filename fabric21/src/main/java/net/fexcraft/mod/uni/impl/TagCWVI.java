package net.fexcraft.mod.uni.impl;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.tag.TagType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;

import java.util.Collection;
import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TagCWVI implements TagCW {

	private ValueInput in;

	public TagCWVI(ValueInput input){
		in = input;
	}

	@Override
	public String getString(String key){
		return in.getStringOr(key, "");
	}

	@Override
	public float getFloat(String key){
		return in.getFloatOr(key, 0f);
	}

	@Override
	public double getDouble(String key){
		return in.getDoubleOr(key, 0d);
	}

	@Override
	public int getInteger(String key){
		return in.getIntOr(key, 0);
	}

	@Override
	public long getLong(String key){
		return in.getLongOr(key, 0l);
	}

	@Override
	public boolean getBoolean(String key){
		return in.getBooleanOr(key, false);
	}

	@Override
	public byte getByte(String key){
		return in.getByteOr(key, (byte)0);
	}

	@Override
	public TagCW getCompound(String key){
		var res = in.read(key, CompoundTag.CODEC);
		return new TagCWI(res.isPresent() ? res.get() : new CompoundTag());
	}

	@Override
	public TagLW getList(String key){
		return null;
	}

	@Override
	public V3I getV3I(String key){
		return new V3I(getIntArray(key), 0);
	}

	@Override
	public int[] getIntArray(String key){
		return in.getIntArray(key).get();
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

	}

	@Override
	public void set(String key, float val){

	}

	@Override
	public void set(String key, double val){

	}

	@Override
	public void set(String key, int val){

	}

	@Override
	public void set(String key, long val){

	}

	@Override
	public void set(String key, boolean val){

	}

	@Override
	public void set(String key, TagCW val){

	}

	@Override
	public void set(String key, TagLW val){

	}

	@Override
	public void set(String key, int[] val){

	}

	@Override
	public void set(String key, byte[] val){

	}

	@Override
	public void set(String key, V3I vec, boolean packed){

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

	}

	@Override
	public TagType getType(String key){
		return null;
	}

}
