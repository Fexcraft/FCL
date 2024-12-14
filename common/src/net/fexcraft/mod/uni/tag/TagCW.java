package net.fexcraft.mod.uni.tag;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface TagCW {

	public String getString(String key);

	public float getFloat(String key);

	public double getDouble(String key);

	public int getInteger(String key);

	public long getLong(String key);

	public boolean getBoolean(String key);

	public byte getByte(String key);

	public TagCW getCompound(String key);

	public TagLW getList(String key);

	public default V3D getV3D(String key){
		if(!has(key)) return new V3D();
		TagLW list = getList(key);
		return new V3D(list.getDouble(0), list.getDouble(1), list.getDouble(2));
	}

	public V3I getV3I(String key);

	public int[] getIntArray(String key);

	public byte[] getByteArray(String key);

	public boolean has(String key);

	public void set(String key, String val);

	public void set(String key, float val);

	public void set(String key, double val);

	public void set(String key, int val);

	public void set(String key, long val);

	public void set(String key, boolean val);

	public void set(String key, TagCW val);

	public void set(String key, TagLW val);

	public void set(String key, int[] val);

	public void set(String key, byte[] val);

	public default void set(String key, V3D vec){
		TagLW list = TagLW.create();
		list.add(vec.x);
		list.add(vec.y);
		list.add(vec.z);
		set(key, list);
	}

	public void set(String key, V3I vec, boolean packed);

	public int size();

	//

	public static Supplier<TagCW>[] SUPPLIER = new Supplier[1];
	public static Function<Object, TagCW>[] WRAPPER = new Function[1];

	public static TagCW create(){
		return SUPPLIER[0].get();
	}

	public static TagCW wrap(Object com){
		if(com instanceof TagCW) return (TagCW)com;
		return WRAPPER[0].apply(com);
	}

	public abstract <T> T local();

	public abstract Object direct();

	public boolean empty();

	public Collection<String> keys();

	public TagCW copy();

	public void rem(String key);

	public TagType getType(String key);

}
