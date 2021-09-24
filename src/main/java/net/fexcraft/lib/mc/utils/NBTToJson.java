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
	
	public static final JsonObject getJsonFromTag(NbtCompound compound){
		return convert(compound, true);
	}
	
	public static final JsonObject getJsonFromTag(NbtCompound compound, boolean sort){
		return convert(compound, sort);
	}

	private static final JsonObject convert(NbtCompound compound, boolean sort){
		JsonObject obj = new JsonObject();
		ArrayList<String> list = new ArrayList<String>(compound.getKeys());
		if(sort){
			Collections.sort(list);
		}
		for(String str : list){
			JsonElement elm = element(compound.get(str), sort);
			if(!(elm == null)){ obj.add(str, elm); }
		}
		return obj;
	}
	
	private static final JsonElement element(NbtElement base, boolean sort){
		if(base instanceof NbtByte){
			return new JsonPrimitive(((NbtByte)base).getType());
		}
		if(base instanceof NbtByteArray){
			byte[] arr = ((NbtByteArray)base).getByteArray();
			JsonArray array = new JsonArray();
			for(int i = 0; i < arr.length; i++){
				array.add(arr[i]);
			}
			return array;
		}
		if(base instanceof NbtCompound){
			return convert((NbtCompound)base, sort);
		}
		if(base instanceof NbtDouble){
			return new JsonPrimitive(((NbtDouble)base).getType());
		}
		if(base instanceof NbtFloat){
			return new JsonPrimitive(((NbtFloat)base).getType());
		}
		if(base instanceof NbtInt){
			return new JsonPrimitive(((NbtInt)base).getType());
		}
		if(base instanceof NbtIntArray){
			int[] arr = ((NbtIntArray)base).getIntArray();
			JsonArray array = new JsonArray();
			for(int i = 0; i < arr.length; i++){
				array.add(arr[i]);
			}
			return array;
		}
		if(base instanceof NbtList){
			JsonArray array = new JsonArray();
			NbtList list = (NbtList)base;
			for(NbtElement nbt : list){
				if(nbt instanceof NbtCompound){
					array.add(convert((NbtCompound)nbt, sort));
				}
				else{
					array.add(element(nbt, sort));
				}
			}
			return array;
		}
		if(base instanceof NbtLong){
			return new JsonPrimitive(((NbtLong)base).getType());
		}
		if(base instanceof NbtShort){
			return new JsonPrimitive(((NbtShort)base).getType());
		}
		if(base instanceof NbtString){
			return new JsonPrimitive(((NbtString)base).getType());
		}
		return null;
	}
	
}