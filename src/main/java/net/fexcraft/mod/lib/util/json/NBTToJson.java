package net.fexcraft.mod.lib.util.json;

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
		return convert(compound);
	}

	private static final JsonObject convert(NBTTagCompound compound){
		JsonObject obj = new JsonObject();
		for(String str : compound.getKeySet()){
			JsonElement elm = element(compound.getTag(str));
			if(!(elm == null)){ obj.add(str, elm); }
		}
		return obj;
	}
	
	private static final JsonElement element(NBTBase base){
		if(base instanceof NBTTagByte){
			return new JsonPrimitive(((NBTTagByte)base).getByte() + "b");
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
			return convert((NBTTagCompound)base);
		}
		if(base instanceof NBTTagDouble){
			return new JsonPrimitive(((NBTTagDouble)base).getDouble() + "d");
		}
		if(base instanceof NBTTagFloat){
			return new JsonPrimitive(((NBTTagFloat)base).getFloat() + "f");
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
					array.add(convert((NBTTagCompound)nbt));
				}
				else{
					array.add(element(nbt));
				}
			}
			return array;
		}
		if(base instanceof NBTTagLong){
			return new JsonPrimitive(((NBTTagLong)base).getLong() + "l");
		}
		if(base instanceof NBTTagShort){
			return new JsonPrimitive(((NBTTagShort)base).getShort() + "s");
		}
		if(base instanceof NBTTagString){
			return new JsonPrimitive(((NBTTagString)base).getString());
		}
		return null;
	}
	
}