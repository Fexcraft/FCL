package net.fexcraft.mod.uni.item;

import net.fexcraft.mod.uni.Appendable;
import net.fexcraft.mod.uni.Appended;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.tag.TagCW;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public abstract class StackWrapper {

	public static StackWrapper EMPTY = null;
	public static Function<Object, StackWrapper> SUPPLIER = null;
	//
	protected static ArrayList<Appendable<StackWrapper>> appendables = new ArrayList<>();
	public final Appended<StackWrapper> appended = new Appended<>(this);
	//
	protected ItemWrapper item;

	public StackWrapper(ItemWrapper item){
		this.item = item;
	}

	public abstract <IS> IS local();

	public abstract void set(Object obj);

	public abstract Object direct();

	public abstract StackWrapper setTag(TagCW tag);

	public abstract TagCW getTag();

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

	public abstract void createTagIfMissing();

	public abstract boolean isItemOf(ItemType type);

	public boolean isItemOfAny(ItemType... types){
		for(ItemType type : types){
			if(isItemOf(type)) return true;
		}
		return false;
	}

	/** FVTM specific. */
	public abstract <C> C getContent(Object contenttype);

	/** For ItemWrapper or ItemStacks. */
	public static StackWrapper wrap(Object obj){
		return SUPPLIER.apply(obj);
	}

	public static <A> A wrapAndGetApp(Object stack, Class<A> clazz){
		StackWrapper wrapper = SUPPLIER.apply(stack);
		return wrapper == null ? null : wrapper.appended.get(clazz);
	}

	public abstract IDL getIDL();

	public abstract String getID();

	public static void register(Appendable<StackWrapper> app){
		appendables.add(app);
	}

}
