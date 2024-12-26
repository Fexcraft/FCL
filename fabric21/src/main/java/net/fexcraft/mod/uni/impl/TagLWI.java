package net.fexcraft.mod.uni.impl;

import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.tag.TagType;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TagLWI implements TagLW {

	private ListTag list;

	public TagLWI(){
		list = new ListTag();
	}

	public TagLWI(ListTag com){
		list = com;
	}

	@Override
	public String getString(int idx){
		return list.getString(idx);
	}

	@Override
	public float getFloat(int idx){
		return list.getFloat(idx);
	}

	@Override
	public double getDouble(int idx){
		return list.getDouble(idx);
	}

	@Override
	public int getInteger(int idx){
		return list.getInt(idx);
	}

	@Override
	public TagCW getCompound(int idx){
		return new TagCWI(list.getCompound(idx));
	}

	@Override
	public TagLW getList(int idx){
		return new TagLWI(list.getList(idx));
	}

	@Override
	public void add(String value){
		list.add(StringTag.valueOf(value));
	}

	@Override
	public void add(float value){
		list.add(FloatTag.valueOf(value));
	}

	@Override
	public void add(double value){
		list.add(DoubleTag.valueOf(value));
	}

	@Override
	public void add(int value){
		list.add(IntTag.valueOf(value));
	}

	@Override
	public void add(TagCW value){
		list.add(value.local());
	}

	@Override
	public void add(TagLW value){
		list.add(value.local());
	}

	@Override
	public int size(){
		return list.size();
	}

	@Override
	public <T> T local(){
		return (T)list;
	}

	@Override
	public Object direct(){
		return list;
	}

	@Override
	public boolean empty(){
		return list.isEmpty();
	}

	@Override
	public TagType getType(int idx){
		Tag base = list.get(idx);
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

	@NotNull
	@Override
	public Iterator<TagCW> iterator(){
		return new Iterator<TagCW>(){
			int idx;
			@Override
			public boolean hasNext(){
				return idx < list.size();
			}
			@Override
			public TagCW next(){
				Tag tag = list.get(idx++);
				if(tag instanceof CompoundTag == false) return null;
				return TagCW.wrap(tag);
			}
		};
	}

}