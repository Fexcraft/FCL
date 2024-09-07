package net.fexcraft.mod.uni.tag;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public interface TagLW extends Iterable<TagCW> {

	public String getString(int idx);

	public float getFloat(int idx);

	public double getDouble(int idx);

	public int getInteger(int idx);

	public TagCW getCompound(int idx);

	public TagLW getList(int idx);

	public void add(String value);

	public void add(float value);

	public void add(double value);

	public void add(int value);

	public void add(TagCW value);

	public void add(TagLW value);

	public int size();

	//

	public static Supplier<TagLW>[] SUPPLIER = new Supplier[1];

	public static TagLW create(){
		return SUPPLIER[0].get();
	}

	public abstract <T> T local();

	public abstract Object direct();

	public abstract boolean empty();

	public TagType getType(int idx);
}
