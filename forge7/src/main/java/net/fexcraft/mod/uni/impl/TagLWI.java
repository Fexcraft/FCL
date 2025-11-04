package net.fexcraft.mod.uni.impl;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.fexcraft.mod.uni.tag.TagType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.*;
import net.minecraft.network.NetHandlerPlayServer;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class TagLWI implements TagLW {

	private List<NBTBase> tags;
	private NBTTagList list;

	public TagLWI(){
		list = new NBTTagList();
		tags = ObfuscationReflectionHelper.getPrivateValue(NBTTagList.class, list, "field_74747_a");
	}

	public TagLWI(Object obj){
		if(obj instanceof NBTTagList){
			list = (NBTTagList)obj;
		}
		else list = new NBTTagList();
		tags = ObfuscationReflectionHelper.getPrivateValue(NBTTagList.class, list, "field_74747_a");
	}

	@Override
	public String getString(int idx){
		return list.getStringTagAt(idx);
	}

	@Override
	public float getFloat(int idx){
		return list.func_150308_e(idx);
	}

	@Override
	public double getDouble(int idx){
		return list.func_150309_d(idx);
	}

	@Override
	public int getInteger(int idx){
		return ((NBTTagInt)tags.get(idx)).func_150287_d();
	}

	@Override
	public TagCW getCompound(int idx){
		return new TagCWI(list.getCompoundTagAt(idx));
	}

	@Override
	public TagLW getList(int idx){
		return new TagLWI(tags.get(idx));
	}

	@Override
	public void add(String value){
		list.appendTag(new NBTTagString(value));
	}

	@Override
	public void add(float value){
		list.appendTag(new NBTTagFloat(value));
	}

	@Override
	public void add(double value){
		list.appendTag(new NBTTagDouble(value));
	}

	@Override
	public void add(int value){
		list.appendTag(new NBTTagInt(value));
	}

	@Override
	public void add(TagCW value){
		list.appendTag(value.local());
	}

	@Override
	public void add(TagLW value){
		list.appendTag(value.local());
	}

	@Override
	public void remove(int index){
		if(index < 0 || index >= list.tagCount()) return;
		list.removeTag(index);
	}

	@Override
	public int size(){
		return list.tagCount();
	}

	@Override
	public void forEach(Consumer<? super TagCW> cons){
		for(NBTBase base : tags){
			if(base instanceof NBTTagCompound == false) continue;
			cons.accept(TagCW.wrap(base));
		}
	}

	@Override
	public Iterator<TagCW> iterator(){
		return new Iterator<TagCW>(){
			int idx;
			@Override
			public boolean hasNext(){
				return idx < list.tagCount();
			}
			@Override
			public TagCW next(){
				NBTBase base = tags.get(idx++);
				if(base instanceof NBTTagCompound == false) return null;
				return TagCW.wrap(base);
			}
		};
	}

	@Override
	public <C> C local(){
		return (C)list;
	}

	@Override
	public Object direct(){
		return list;
	}

	@Override
	public boolean empty(){
		return list == null || tags.isEmpty();
	}

	@Override
	public TagType getType(int idx){
		NBTBase base = null;//list.get(idx);
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
		return list == null ? "null" : list.toString();
	}

}
