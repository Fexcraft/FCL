package net.fexcraft.mod.uni.inv;

import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.tag.TagCW;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class StackWrapper {

	/** Function's Type is a local Item */
	public static HashMap<String, Function<Object, Boolean>> ITEM_TYPES = new HashMap<>();
	public static HashMap<String, Function<StackWrapper, Object>> CONTENT_TYPES = new HashMap<>();
	public static StackWrapper EMPTY = null;
	public static String IT_LEAD = "lead";
	public static String IT_FOOD = "food";
	//
	protected ItemWrapper item;

	public StackWrapper(ItemWrapper item){
		this.item = item;
	}

	public abstract <IS> IS local();

	public abstract void set(Object obj);

	public abstract Object direct();

	public abstract StackWrapper updateTag(TagCW tag);

	public void updateTag(Consumer<TagCW> cons){
		TagCW com = copyTag();
		cons.accept(com);
		updateTag(com);
	}

	public abstract TagCW directTag();

	public abstract TagCW copyTag();

	public abstract boolean hasTag();

	public ItemWrapper getItem(){
		return item;
	}

	public abstract String getName();

	public abstract int maxsize();

	public abstract int damage();

	public abstract void damage(int val);

	public abstract int count();

	public abstract void count(int am);

	public void decr(int am){
		count(count() - am);
	}

	public void incr(int am){
		count(count() + am);
	}

	public abstract StackWrapper copy();

	public abstract void save(TagCW com);

	public abstract boolean empty();

	public boolean isItemOf(String type){
		Function<Object, Boolean> func = ITEM_TYPES.get(type);
		return func != null && func.apply(getItem().direct());
	}

	public boolean isItemOfAny(String... types){
		for(String type : types) if(isItemOf(type)) return true;
		return false;
	}

	public boolean isItemOfAny(Collection<String> types){
		for(String type : types) if(isItemOf(type)) return true;
		return false;
	}

	public <C> C getContent(String type){
		Function<StackWrapper, Object> func = CONTENT_TYPES.get(type);
		return func == null ? null : (C)func.apply(this);
	}

	public abstract IDL getIDL();

	public abstract String getID();

}
