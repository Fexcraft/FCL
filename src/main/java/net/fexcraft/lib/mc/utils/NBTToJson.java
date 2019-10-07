package net.fexcraft.lib.mc.utils;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.*;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class NBTToJson {
	
	public static final JsonObject getJsonFromTag(CompoundTag compound){
		return convert(compound, true);
	}
	
	public static final JsonObject getJsonFromTag(CompoundTag compound, boolean sort){
		return convert(compound, sort);
	}

	private static final JsonObject convert(CompoundTag compound, boolean sort){
		JsonObject obj = new JsonObject();
		ArrayList<String> list = new ArrayList<String>(compound.getKeys());
		if(sort){
			Collections.sort(list);
		}
		for(String str : list){
			JsonElement elm = element(compound.getTag(str), sort);
			if(!(elm == null)){ obj.add(str, elm); }
		}
		return obj;
	}
	
	private static final JsonElement element(Tag base, boolean sort){
		if(base instanceof ByteTag){
			return new JsonPrimitive(((ByteTag)base).getByte());
		}
		if(base instanceof ByteArrayTag){
			byte[] arr = ((ByteArrayTag)base).getByteArray();
			JsonArray array = new JsonArray();
			for(int i = 0; i < arr.length; i++){
				array.add(arr[i]);
			}
			return array;
		}
		if(base instanceof CompoundTag){
			return convert((CompoundTag)base, sort);
		}
		if(base instanceof DoubleTag){
			return new JsonPrimitive(((DoubleTag)base).getDouble());
		}
		if(base instanceof FloatTag){
			return new JsonPrimitive(((FloatTag)base).getFloat());
		}
		if(base instanceof IntTag){
			return new JsonPrimitive(((IntTag)base).getInt());
		}
		if(base instanceof IntArrayTag){
			int[] arr = ((IntArrayTag)base).getIntArray();
			JsonArray array = new JsonArray();
			for(int i = 0; i < arr.length; i++){
				array.add(arr[i]);
			}
			return array;
		}
		if(base instanceof ListTag){
			JsonArray array = new JsonArray();
			ListTag list = (ListTag)base;
			for(Tag nbt : list){
				if(nbt instanceof CompoundTag){
					array.add(convert((CompoundTag)nbt, sort));
				}
				else{
					array.add(element(nbt, sort));
				}
			}
			return array;
		}
		if(base instanceof LongTag){
			return new JsonPrimitive(((LongTag)base).getLong());
		}
		if(base instanceof ShortTag){
			return new JsonPrimitive(((ShortTag)base).getShort());
		}
		if(base instanceof StringTag){
			return new JsonPrimitive(((StringTag)base).asString());
		}
		return null;
	}
	
}