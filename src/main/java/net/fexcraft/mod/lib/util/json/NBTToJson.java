package net.fexcraft.mod.lib.util.json;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class NBTToJson {
	
	public static final JsonObject getJsonFromTag(NBTTagCompound compound){
		return convert(compound, true);
	}
	
	public static final JsonObject getJsonFromTag(NBTTagCompound compound, boolean sort){
		return convert(compound, sort);
	}

	private static final JsonObject convert(NBTTagCompound compound, boolean sort){
		JsonObject obj = new JsonObject();
		ArrayList<String> list = new ArrayList<String>(compound.getKeySet());
		if(sort){
			Collections.sort(list);
		}
		for(String str : list){
			JsonElement elm = element(compound.getTag(str), sort);
			if(!(elm == null)){ obj.add(str, elm); }
		}
		return obj;
	}
	
	private static final JsonElement element(NBTBase base, boolean sort){
		if(base instanceof NBTTagByte){
			return new JsonPrimitive(((NBTTagByte)base).getByte());
		}
		if(base instanceof NBTTagByteArray){
			byte[] arr = ((NBTTagByteArray)base).getByteArray();
			JsonArray array = new JsonArray();
			for(int i = 0; i < arr.length; i++){
				array.add(arr[i]);
			}
			return array;
		}
		if(base instanceof NBTTagCompound){
			return convert((NBTTagCompound)base, sort);
		}
		if(base instanceof NBTTagDouble){
			return new JsonPrimitive(((NBTTagDouble)base).getDouble());
		}
		if(base instanceof NBTTagFloat){
			return new JsonPrimitive(((NBTTagFloat)base).getFloat());
		}
		if(base instanceof NBTTagInt){
			return new JsonPrimitive(((NBTTagInt)base).getInt());
		}
		if(base instanceof NBTTagIntArray){
			int[] arr = ((NBTTagIntArray)base).getIntArray();
			JsonArray array = new JsonArray();
			for(int i = 0; i < arr.length; i++){
				array.add(arr[i]);
			}
			return array;
		}
		if(base instanceof NBTTagList){
			JsonArray array = new JsonArray();
			NBTTagList list = (NBTTagList)base;
			for(NBTBase nbt : list){
				if(nbt instanceof NBTTagCompound){
					array.add(convert((NBTTagCompound)nbt, sort));
				}
				else{
					array.add(element(nbt, sort));
				}
			}
			return array;
		}
		if(base instanceof NBTTagLong){
			return new JsonPrimitive(((NBTTagLong)base).getLong());
		}
		if(base instanceof NBTTagShort){
			return new JsonPrimitive(((NBTTagShort)base).getShort());
		}
		if(base instanceof NBTTagString){
			return new JsonPrimitive(((NBTTagString)base).getString());
		}
		return null;
	}
	
}